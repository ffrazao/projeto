package br.gov.df.emater.aterwebsrv.importador.apoio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.dao.pessoa.CidadeDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ProfissaoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.modelo.ater.OrganizacaoTipo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.ater.Setor;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ConfirmacaoDap;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.EstadoCivil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RegimeCasamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Profissao;

@Service
public class ImpUtil {

	@Autowired
	private CidadeDao cidadeDao;

	private Map<String, OrganizacaoTipo> organizacaoTipoMap = null;

	@Autowired
	private ProfissaoDao profissaoDao;

	private Map<String, Profissao> profissaoMap = new HashMap<String, Profissao>();

	private Map<String, Setor> setorMap = null;

	public Calendar captaData(Date data) {
		if (data == null) {
			return null;
		}
		Calendar result = new GregorianCalendar();
		result.setTime(data);
		return result;
	}

	public void chaveAterWebAtualizar(Connection con, Integer id, String tabelaSisater, String clausuaWhere, Object... parametroList) throws SQLException {
		PreparedStatement ps = con.prepareStatement(String.format("UPDATE %s SET CHAVE_ATER_WEB = ? WHERE %s", tabelaSisater, clausuaWhere));
		ps.setInt(1, id);
		int cont = 2;
		if (parametroList != null) {
			for (Object parametro : parametroList) {
				ps.setObject(cont++, parametro);
			}
		}
		ps.executeUpdate();
	}

	public String chaveBaciaHidrografica(DbSater base, String baciaHidrograficaNome) {
		return String.format("%s=[BACIA=%s]", base.name(), baciaHidrograficaNome);
	}

	public String chaveBeneficiario(DbSater base, String idund, String idbem) {
		return String.format("%s=[IDUND=%s,IDBEN=%s]", base.name(), idund, idbem);
	}

	public String chaveComunidade(DbSater base, String idund, String idcom) {
		return String.format("%s=[IDUND=%s,IDCOM=%s]", base.name(), idund, idcom);
	}

	public String chavePessoaEmail(DbSater base, String idund, String idbem) {
		return chaveBeneficiario(base, idund, idbem);
	}

	public String chavePessoaEndereco(DbSater base, String idund, String idbem, String[] nomeCampo) {
		return String.format("%s[CAMPO=%s][CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo[0], nomeCampo[1]);
	}

	public String chavePessoaGrupoSocial(DbSater base, String idund, String idbem, String nomeCampo) {
		return String.format("%s[CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo);
	}

	public String chavePessoaRelacionamento(DbSater base, String idund, String idbem, String nomeCampo) {
		return String.format("%s[CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo);
	}

	public String chavePessoaTelefone(DbSater base, String idund, String idbem, String nomeCampo) {
		return String.format("%s[CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo);
	}

	public void criarMarcaTabela(Connection con, String tabela) throws SQLException {
		try {
			try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(String.format("SELECT CHAVE_ATER_WEB FROM %s", tabela))) {
				return;
			}
		} catch (Exception e) {
			try (Statement st = con.createStatement();) {
				st.execute(String.format("ALTER TABLE %s ADD CHAVE_ATER_WEB INT", tabela));
				con.commit();
				return;
			}
		}
	}

	public String deNomeBaciaHidrograficaSisaterParaAterWeb(String registro) throws BoException {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "pedreira - contagem":
			return "Pedreira Contagem";
		case "cachoerinha":
		case "cachoeirinha - papuda":
			return "Cachoeirinha";
		default:
			return registro;
		}
	}

	public Cidade deNomeCidadeComunidadeSisaterParaAterWeb(String registro, DbSater base, Municipio[] municipioAtendimentoList) throws BoException {
		if (registro == null) {
			return null;
		}

		switch (base.getSiglaEstado()) {
		case "DF":
			switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
			case "scia":
				registro = "Guará";
				break;
			case "alexandre gusmao":
				registro = "Brazlândia";
				break;
			case "aguas claras":
				registro = "Taguatinga";
				break;
			case "riacho fundo i":
			case "riacho fundo ii":
				registro = "Riacho Fundo";
				break;
			case "park way":
				registro = "Núcleo Bandeirante";
				break;
			}
		case "GO":
			switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
			case "sao marcos":
				registro = "Cristalina";
				break;
			case "cocalzinho":
				registro = "Cocalzinho de Goiás";
				break;
			case "planaltina de goias":
				registro = "Planaltina";
				break;
			}
			break;
		}

		List<Cidade> cidadeList = null;
		for (Municipio municipio : municipioAtendimentoList) {
			if (municipio.getEstado().getSigla().equals(base.getSiglaEstado())) {
				cidadeList = cidadeDao.findByNomeLikeAndMunicipio(registro, municipio);
				if (cidadeList != null && cidadeList.size() > 0) {
					break;
				}
			}
		}

		if (cidadeList == null || cidadeList.size() == 0) {
			throw new BoException("Cidade não encontrada - %s", registro);
		} else if (cidadeList.size() > 1) {
			throw new BoException("Mais de uma cidade encontrada - %s", registro);
		}
		return cidadeList.get(0);
	}

	public String deNomeComunidadeSisaterParaAterWeb(String registro) throws BoException {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		default:
			return registro;
		}
	}

	public ConfirmacaoDap deParaDapSituacao(String registro) {
		if (registro == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "ndp":
			return ConfirmacaoDap.D;
		case "nao":
			return ConfirmacaoDap.N;
		case "sim":
			return ConfirmacaoDap.S;
		default:
			return null;
		}
	}

	public Escolaridade deParaEscolaridade(String registro) {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "analfabeto":
			return Escolaridade.AI;
		case "ensino fundamental":
		case "fundamental completo":
		case "primario completo":
			return Escolaridade.FC;
		case "primeiro grau incompleto":
		case "primeiro incompleto":
		case "fundamental incompleto":
		case "primario incompleto":
			return Escolaridade.FI;
		case "medio completo":
			return Escolaridade.MC;
		case "medio incompleto":
			return Escolaridade.MI;
		case "pos garduado":
			return Escolaridade.ES;
		case "superior completo":
			return Escolaridade.SC;
		case "superior incompleto":
			return Escolaridade.SI;
		default:
			return null;
		}
	}

	public EstadoCivil deParaEstadoCivil(String registro) {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "amasiado":
		case "uniao esta":
		case "juntado":
			return EstadoCivil.U;
		case "casada":
		case "casado":
			return EstadoCivil.C;
		case "desquitado":
		case "separado":
			return EstadoCivil.P;
		case "divorciado":
			return EstadoCivil.D;
		case "solteira":
		case "solteiro":
			return EstadoCivil.S;
		case "falecida":
		case "viuva":
		case "viuvo":
			return EstadoCivil.V;
		default:
			return null;
		}
	}

	public PessoaGenero deParaGenero(String registro) {
		if (registro == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "feminino":
			return PessoaGenero.F;
		case "masculino":
			return PessoaGenero.M;
		default:
			return PessoaGenero.M;
		}
	}

	public OrganizacaoTipo deParaOrganizacaoTipo(String registro) throws BoException {
		if (registro == null || registro.trim().length() == 0) {
			return null;
		}
		if (organizacaoTipoMap == null) {
			organizacaoTipoMap = new HashMap<String, OrganizacaoTipo>();
			organizacaoTipoMap.put("associacao", new OrganizacaoTipo(1, "Associação"));
			organizacaoTipoMap.put("condominio", new OrganizacaoTipo(2, "Condomínio"));
			organizacaoTipoMap.put("conselho", new OrganizacaoTipo(3, "Conselho"));
			organizacaoTipoMap.put("cooperativa", new OrganizacaoTipo(4, "Cooperativa"));
			organizacaoTipoMap.put("empresa", new OrganizacaoTipo(5, "Empresa"));
			organizacaoTipoMap.put("escola rural", new OrganizacaoTipo(6, "Escola Rural"));
			organizacaoTipoMap.put("escola urbana", new OrganizacaoTipo(7, "Escola Urbana"));
			organizacaoTipoMap.put("grupos", new OrganizacaoTipo(8, "Grupos"));
			organizacaoTipoMap.put("individual", new OrganizacaoTipo(9, "Individual"));
			organizacaoTipoMap.put("institucional", new OrganizacaoTipo(10, "Institucional"));
			organizacaoTipoMap.put("ong´s", new OrganizacaoTipo(11, "ONG\"s"));
			organizacaoTipoMap.put("outro", new OrganizacaoTipo(12, "Outro"));
		}
		OrganizacaoTipo result = organizacaoTipoMap.get(UtilitarioString.semAcento(registro).toLowerCase());
		if (result == null) {
			throw new BoException("Setor não identificado");
		}
		return result;

	}

	public Profissao deParaProfissao(String registro) {
		if (registro == null || registro.trim().length() == 0 || "null".equals(registro.trim().toLowerCase())) {
			return null;
		}
		if (profissaoMap.containsKey(registro)) {
			return profissaoMap.get(registro);
		}
		Profissao result = profissaoDao.findOneByNome(registro);
		if (result == null) {
			result = new Profissao();
			result.setNome(registro);
			result = profissaoDao.save(result);
		}
		profissaoMap.put(result.getNome(), result);
		return result;
	}

	public PublicoAlvoCategoria deParaPublicoAlvoCategoria(String registro) throws BoException {
		if (registro == null) {
			throw new BoException("Categoria não informada");
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "empreendedor":
			return PublicoAlvoCategoria.E;
		case "habitante":
			return PublicoAlvoCategoria.H;
		case "trabalhador":
			return PublicoAlvoCategoria.T;
		default:
			throw new BoException("Categoria não identificada [%s]", registro);
		}
	}

	public PublicoAlvoSegmento deParaPublicoAlvoSegmento(String registro) throws BoException {
		if (registro == null) {
			throw new BoException("Segmento não informado");
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "familiar":
			return PublicoAlvoSegmento.F;
		case "patronal":
			return PublicoAlvoSegmento.P;
		default:
			throw new BoException("Segmento não identificado [%s]", registro);
		}
	}

	public List<PublicoAlvoSetor> deParaPublicoAlvoSetorList(String setor1, String setor2) throws BoException {
		List<PublicoAlvoSetor> publicoAlvoSetorList = new ArrayList<PublicoAlvoSetor>();
		if (setor1 != null) {
			Setor setor = deParaSetor(setor1);
			if (setor != null) {
				publicoAlvoSetorList.add(new PublicoAlvoSetor(null, setor));
			}
		}
		if (setor2 != null && !setor2.equals(setor1)) {
			Setor setor = deParaSetor(setor2);
			if (setor != null) {
				publicoAlvoSetorList.add(new PublicoAlvoSetor(null, setor));
			}
		}
		return publicoAlvoSetorList;
	}

	public RegimeCasamento deParaRegimeCasamento(String registro) {
		if (registro == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "comunhao parcial":
			return RegimeCasamento.P;
		case "comunhao total":
			return RegimeCasamento.T;
		case "nao se aplica":
			return RegimeCasamento.N;
		case "separacao de bens":
			return RegimeCasamento.S;
		case "uniao estavel":
			return RegimeCasamento.U;
		default:
			return null;
		}
	}

	public Setor deParaSetor(String registro) throws BoException {
		if (registro == null || registro.trim().length() == 0) {
			return null;
		}
		if (setorMap == null) {
			setorMap = new HashMap<String, Setor>();
			setorMap.put("agroindustria", new Setor(1, "Agroindústria"));
			setorMap.put("agropecuaria", new Setor(2, "Agropecuária"));
			setorMap.put("artesanato", new Setor(3, "Artesanato"));
			setorMap.put("comercio", new Setor(4, "Comércio"));
			setorMap.put("proc. artesanal", new Setor(5, "Processamento Artesanal"));
			setorMap.put("servico", new Setor(6, "Serviço"));
			setorMap.put("turismo", new Setor(7, "Turismo Rural"));
			setorMap.put("outros", new Setor(8, "Outro"));
		}
		Setor result = setorMap.get(UtilitarioString.semAcento(registro).toLowerCase());
		if (result == null) {
			throw new BoException("Setor não identificado");
		}
		return result;
	}

	public void pessoaConfiguraNome(Pessoa pessoa, String nome, String apelidoSigla) {
		pessoa.setNome(UtilitarioString.formataNomeProprio(nome));
		pessoa.setApelidoSigla(apelidoSigla);
		if (pessoa.getNome() != null) {
			pessoa.setNome(pessoa.getNome().trim());
			if (pessoa.getNome().length() > 0 && pessoa.getApelidoSigla() == null || pessoa.getApelidoSigla().trim().length() == 0) {
				pessoa.setApelidoSigla(pessoa.getNome().split("\\s")[0]);
			}
		}
	}
}
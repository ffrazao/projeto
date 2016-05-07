package br.gov.df.emater.aterwebsrv.importador.apoio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.dao.pessoa.CidadeDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ProfissaoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.EstadoCivil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Profissao;

@Service
public class ImpUtil {

	@Autowired
	private CidadeDao cidadeDao;
	
	@Autowired
	private ProfissaoDao profissaoDao;

	public String chaveBaciaHidrografica(DbSater base, String baciaHidrograficaNome) {
		return String.format("%s=[BACIA=%s]", base.name(), baciaHidrograficaNome);
	}

	public String chaveBeneficiario(DbSater base, String idund, String idbem) {
		return String.format("%s=[IDUND=%s,IDBEN=%s]", base.name(), idund, idbem);
	}

	public String chaveComunidade(DbSater base, String idund, String idcom) {
		return String.format("%s=[IDUND=%s,IDCOM=%s]", base.name(), idund, idcom);
	}

	public String deNomeBaciaHidrograficaSisaterParaAterWeb(String baciaHidrografica) throws BoException {
		if (baciaHidrografica == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(baciaHidrografica.trim().toLowerCase())) {
		case "pedreira - contagem":
			return "Pedreira Contagem";
		case "cachoerinha":
		case "cachoeirinha - papuda":
			return "Cachoeirinha";
		default:
			return baciaHidrografica;
		}
	}

	public Cidade deNomeCidadeComunidadeSisaterParaAterWeb(String cidade, DbSater base, Municipio[] municipioAtendimentoList) throws BoException {
		if (cidade == null) {
			return null;
		}

		switch (base.getSiglaEstado()) {
		case "DF":
			switch (UtilitarioString.semAcento(cidade.trim().toLowerCase())) {
			case "scia":
				cidade = "Guará";
				break;
			case "alexandre gusmao":
				cidade = "Brazlândia";
				break;
			case "aguas claras":
				cidade = "Taguatinga";
				break;
			case "riacho fundo i":
			case "riacho fundo ii":
				cidade = "Riacho Fundo";
				break;
			case "park way":
				cidade = "Núcleo Bandeirante";
				break;
			}
		case "GO":
			switch (UtilitarioString.semAcento(cidade.trim().toLowerCase())) {
			case "sao marcos":
				cidade = "Cristalina";
				break;
			case "cocalzinho":
				cidade = "Cocalzinho de Goiás";
				break;
			case "planaltina de goias":
				cidade = "Planaltina";
				break;
			}
			break;
		}

		List<Cidade> cidadeList = null;
		for (Municipio municipio : municipioAtendimentoList) {
			if (municipio.getEstado().getSigla().equals(base.getSiglaEstado())) {
				cidadeList = cidadeDao.findByNomeLikeAndMunicipio(cidade, municipio);
				if (cidadeList != null && cidadeList.size() > 0) {
					break;
				}
			}
		}

		if (cidadeList == null || cidadeList.size() == 0) {
			throw new BoException("Cidade não encontrada - %s", cidade);
		} else if (cidadeList.size() > 1) {
			throw new BoException("Mais de uma cidade encontrada - %s", cidade);
		}
		return cidadeList.get(0);
	}

	public String deNomeComunidadeSisaterParaAterWeb(String comunidade) throws BoException {
		if (comunidade == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(comunidade.trim().toLowerCase())) {
		default:
			return comunidade;
		}
	}

	public Escolaridade deParaEscolaridade(String escolaridade) {
		if (escolaridade == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(escolaridade.trim().toLowerCase())) {
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

	public EstadoCivil deParaEstadoCivil(String estadoCivil) {
		if (estadoCivil == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(estadoCivil.trim().toLowerCase())) {
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

	public PessoaGenero deParaGenero(String genero) {
		if (genero == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(genero.trim().toLowerCase())) {
		case "feminino":
			return PessoaGenero.F;
		case "masculino":
			return PessoaGenero.M;
		default:
			return PessoaGenero.M;
		}
	}

	public Profissao deParaProfissao(String profissao) {
		if (profissao == null || profissao.trim().length() == 0) {
			return null;
		}
		Profissao result = profissaoDao.findOneByNome(profissao);
		if (result == null) {
			result = new Profissao();
			result.setNome(profissao);
			result = profissaoDao.save(result);
		}
		return result;
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

	public Calendar captaData(Date data) {
		if (data == null) {
			return null;
		}
		Calendar result = new GregorianCalendar();
		result.setTime(data);
		return result;
	}
}
package br.gov.df.emater.aterwebsrv.bo.pendencia;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralPendenciaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaPendenciaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralPendencia;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pendencia.Pendencia;
import br.gov.df.emater.aterwebsrv.modelo.pendencia.Pendenciavel;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaPendencia;

public abstract class VerificarPendenciasCmd extends _Comando {

	private Pendenciavel<?> pendenciavel;

	@Autowired
	protected PessoaPendenciaDao pessoaPendenciaDao;

	@Autowired
	protected PropriedadeRuralPendenciaDao propriedadeRuralPendenciaDao;

	private String adicionarDescricao(String descricaoTxt, String descricao) throws IOException {
		if (descricao == null) {
			return descricaoTxt;
		}
		if (descricaoTxt == null) {
			descricaoTxt = "";
		}
		List<String> descricaoList = IOUtils.readLines(new StringReader(descricaoTxt));
		if (descricaoList.contains(descricao)) {
			return descricaoTxt;
		}
		descricaoList.add(descricao);
		StringBuilder result = new StringBuilder();
		for (String linha : descricaoList) {
			result.append(linha).append("\n");
		}
		return result.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final void adicionarPendencia(_Contexto contexto, String descricao) throws IOException {
		Pendencia pendencia = null;

		// inserir no contexto
		List<String> pendenciaList = (List<String>) contexto.get("pendenciaList");
		if (pendenciaList == null) {
			pendenciaList = new ArrayList<String>();
		}
		pendenciaList.add(descricao);
		contexto.put("pendenciaList", pendenciaList);

		// inserir no objeto analisado
		if (getPendenciavel() instanceof Pessoa) {
			pendencia = new PessoaPendencia(getCodigo(), descricao);
			Pessoa pessoa = (Pessoa) getPendenciavel();
			if (pessoa != null && pessoa.getId() != null) {
				PessoaPendencia salvo = pessoaPendenciaDao.findOneByPessoaAndCodigo(pessoa, getCodigo().name());
				if (salvo != null) {
					pendencia.setId(salvo.getId());
					pendencia.setDescricao(adicionarDescricao(salvo.getDescricao(), descricao));
				}
			}
			((Pendenciavel<PessoaPendencia>) getPendenciavel()).getPendenciaList().add((PessoaPendencia) pendencia);
		} else if (getPendenciavel() instanceof PropriedadeRural) {
			pendencia = new PropriedadeRuralPendencia(getCodigo(), descricao);
			PropriedadeRural propriedadeRural = (PropriedadeRural) getPendenciavel();
			if (propriedadeRural != null && propriedadeRural.getId() != null) {
				PropriedadeRuralPendencia salvo = propriedadeRuralPendenciaDao.findOneByPropriedadeRuralAndCodigo(propriedadeRural, getCodigo().name());
				if (salvo != null) {
					pendencia.setId(salvo.getId());
					pendencia.setDescricao(adicionarDescricao(salvo.getDescricao(), descricao));
				}
			}
			((Pendenciavel<PropriedadeRuralPendencia>) getPendenciavel()).getPendenciaList().add((PropriedadeRuralPendencia) pendencia);
		}
	}

	public abstract String constatarPendencia(_Contexto contexto);

	@Override
	public final boolean executar(_Contexto contexto) throws Exception {
		if (contexto.getRequisicao() instanceof Pendenciavel) {
			setPendenciavel((Pendenciavel<?>) contexto.getRequisicao());
		}

		String pendencia = constatarPendencia(contexto);
		if (!StringUtils.isBlank(pendencia)) {
			adicionarPendencia(contexto, pendencia);
		}

		return false;
	}

	protected String extraiResultado(List<String> lista) {
		return UtilitarioString.collectionToString(lista, "\n");
	}

	public abstract PendenciaCodigo getCodigo();

	public final Pendenciavel<?> getPendenciavel() {
		return pendenciavel;
	}

	public final void setPendenciavel(Pendenciavel<?> pendenciavel) {
		this.pendenciavel = pendenciavel;
	}

}
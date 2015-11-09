package br.gov.df.emater.aterwebsrv.bo.pessoa;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service("PessoaVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private PessoaDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Pessoa result = dao.findOne(id);

		if (result instanceof PessoaFisica) {
			PessoaFisica pessoaFisica = (PessoaFisica) result;
			if (pessoaFisica.getNascimentoMunicipio() != null) {
				pessoaFisica.setNascimentoMunicipio(pessoaFisica.getNascimentoMunicipio().infoBasica());
			}
			if (pessoaFisica.getNascimentoEstado() != null) {
				pessoaFisica.setNascimentoEstado(pessoaFisica.getNascimentoEstado().infoBasica());
			}
			if (pessoaFisica.getNascimentoPais() != null) {
				pessoaFisica.setNascimentoPais(pessoaFisica.getNascimentoPais().infoBasica());
			}
		} else if (result instanceof PessoaJuridica) {

		}

		// fetch nas tabelas de apoio
		result.getArquivoList().size();
		result.getEmailList().size();
		if (result.getEnderecoList() != null) {
			for (PessoaEndereco pessoaEndereco : result.getEnderecoList()) {
				pessoaEndereco.setEndereco(pessoaEndereco.getEndereco().infoBasica());
			}
		}
		result.getGrupoSocialList().size();
		result.getRelacionamentoList().size();
		result.getTelefoneList().size();

		if (result.getUsuarioInclusao() != null) {
			result.setUsuarioInclusao(result.getUsuarioInclusao().infoBasica());
		}
		if (result.getUsuarioAlteracao() != null) {
			result.setUsuarioAlteracao(result.getUsuarioAlteracao().infoBasica());
		}

		em.detach(result);
		contexto.setResposta(result);

		return true;
	}
}
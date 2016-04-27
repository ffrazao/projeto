package br.gov.df.emater.aterwebsrv.bo.pessoa;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaArquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;

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

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		if (result instanceof PessoaFisica) {
			PessoaFisica pessoaFisica = (PessoaFisica) result;

			if (pessoaFisica.getNascimentoMunicipio() != null) {
				pessoaFisica.setNascimentoEstado(pessoaFisica.getNascimentoMunicipio().getEstado().infoBasica());
				pessoaFisica.setNascimentoMunicipio(pessoaFisica.getNascimentoMunicipio().infoBasica());
			}
			if (pessoaFisica.getNascimentoPais() != null) {
				pessoaFisica.setNascimentoPais(pessoaFisica.getNascimentoPais().infoBasica());
			}
		} else if (result instanceof PessoaJuridica) {

		}

		result.setPerfilArquivo(result.getPerfilArquivo() == null ? null : result.getPerfilArquivo().infoBasica());

		if (Confirmacao.S.equals(result.getPublicoAlvoConfirmacao())) {
			PublicoAlvo publicoAlvo = result.getPublicoAlvo();
			if (publicoAlvo.getPublicoAlvoPropriedadeRuralList() != null) {
				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvo.getPublicoAlvoPropriedadeRuralList()) {
					publicoAlvoPropriedadeRural.setPublicoAlvo(publicoAlvoPropriedadeRural.getPublicoAlvo().infoBasica());
					publicoAlvoPropriedadeRural.setComunidade(publicoAlvoPropriedadeRural.getComunidade().infoBasica());
					publicoAlvoPropriedadeRural.setPropriedadeRural(publicoAlvoPropriedadeRural.getPropriedadeRural() != null ? publicoAlvoPropriedadeRural.getPropriedadeRural().infoBasica() : null);
				}
			}
			if (publicoAlvo.getPublicoAlvoSetorList() != null) {
				for (PublicoAlvoSetor publicoAlvoSetor : publicoAlvo.getPublicoAlvoSetorList()) {
					publicoAlvoSetor.setPublicoAlvo(null);
				}
			}
		}

		// fetch nas tabelas de apoio
		if (result.getArquivoList() != null) {
			for (PessoaArquivo pessoaArquivo : result.getArquivoList()) {
				pessoaArquivo.setPessoa(null);
				pessoaArquivo.setArquivo(pessoaArquivo.getArquivo() == null? null : pessoaArquivo.getArquivo().infoBasica());
			}
		}
		if (result.getEmailList() != null) {
			for (PessoaEmail pessoaEmail : result.getEmailList()) {
				pessoaEmail.setPessoa(null);
			}
		}
		if (result.getEnderecoList() != null) {
			for (PessoaEndereco pessoaEndereco : result.getEnderecoList()) {
				pessoaEndereco.setEndereco(pessoaEndereco.getEndereco().infoBasica());
				pessoaEndereco.setPessoa(null);
			}
		}
		if (result.getGrupoSocialList() != null) {
			for (PessoaGrupoSocial pessoaGrupoSocial : result.getGrupoSocialList()) {
				pessoaGrupoSocial.setPessoa(null);
			}
		}
		if (result.getRelacionamentoList() != null) {
			for (PessoaRelacionamento relacionador : result.getRelacionamentoList()) {
				Relacionamento relacionamento = relacionador.getRelacionamento();
				Pessoa pessoaRelacionado = null;
				Integer idRelacionamento = null;
				RelacionamentoFuncao relacionadoFuncao = null;
				for (PessoaRelacionamento relacionado : relacionamento.getPessoaRelacionamentoList()) {
					if (!relacionado.getPessoa().getId().equals(result.getId())) {
						pessoaRelacionado = relacionado.getPessoa().infoBasica();
						idRelacionamento = relacionado.getId();
						relacionadoFuncao = relacionado.getRelacionamentoFuncao().infoBasica();
						break;
					}
				}
				relacionador.setId(idRelacionamento);
				relacionador.setRelacionamento(relacionador.getRelacionamento().infoBasica());
				relacionador.setRelacionamentoFuncao(relacionadoFuncao);
				relacionador.setPessoa(pessoaRelacionado);
			}
		}
		if (result.getTelefoneList() != null) {
			for (PessoaTelefone pessoaTelefone : result.getTelefoneList()) {
				pessoaTelefone.setPessoa(null);
			}
		}

		if (result.getUsuarioInclusao() != null) {
			result.setUsuarioInclusao(result.getUsuarioInclusao().infoBasica());
		}
		if (result.getUsuarioAlteracao() != null) {
			result.setUsuarioAlteracao(result.getUsuarioAlteracao().infoBasica());
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}
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
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaPendencia;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;

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
				pessoaArquivo.setArquivo(pessoaArquivo.getArquivo() == null ? null : pessoaArquivo.getArquivo().infoBasica());
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
				for (PessoaRelacionamento relacionado : relacionador.getRelacionamento().getPessoaRelacionamentoList()) {
					// aqui o registro original vem selecionado como o do
					// relacionador, ou seja, da pessoa que foi consultada
					// por isso é necessário inverter os dados com o do
					// relacionado, para envio a tela, para futuras modificações
					if (relacionado.getPessoa() != null && !relacionado.getPessoa().getId().equals(result.getId())) {
						// informações principais
						relacionador.setId(relacionado.getId());
						relacionador.setRelacionamentoFuncao(relacionador.getRelacionamentoFuncao() == null ? null : relacionador.getRelacionamentoFuncao().infoBasica());
						relacionador.setChaveSisater(relacionado.getChaveSisater());

						// captar os dados do relacionado
						relacionador.setPessoa(relacionado.getPessoa().infoBasica());
						break;
					} else {
						// informações principais
						relacionador.setId(relacionado.getId());
						relacionador.setRelacionamentoFuncao(relacionado.getRelacionamentoFuncao() == null ? null : relacionado.getRelacionamentoFuncao().infoBasica());
						relacionador.setChaveSisater(relacionado.getChaveSisater());

						// captar os dados do relacionado
						relacionador.setPessoa(null);
						relacionador.setApelidoSigla(relacionado.getApelidoSigla());
						relacionador.setCertidaoCasamentoRegime(relacionado.getCertidaoCasamentoRegime());
						relacionador.setCpf(relacionado.getCpf());
						relacionador.setEscolaridade(relacionado.getEscolaridade());
						relacionador.setGenero(relacionado.getGenero());
						relacionador.setNacionalidade(relacionado.getNacionalidade());
						relacionador.setNascimento(relacionado.getNascimento());
						if (relacionado.getNascimentoMunicipio() != null) {							
							relacionador.setNascimentoEstado(relacionado.getNascimentoMunicipio().getEstado().infoBasica());
							relacionador.setNascimentoMunicipio(relacionado.getNascimentoMunicipio().infoBasica());
						}
						relacionador.setNascimentoPais(relacionado.getNascimentoPais() == null ? null : relacionado.getNascimentoPais().infoBasica());
						relacionador.setNome(relacionado.getNome());
						relacionador.setNomeMae(relacionado.getNomeMae());
						relacionador.setProfissao(relacionado.getProfissao() == null ? null : relacionado.getProfissao().infoBasica());
						relacionador.setRgDataEmissao(relacionado.getRgDataEmissao());
						relacionador.setRgNumero(relacionado.getRgNumero());
						relacionador.setRgOrgaoEmissor(relacionado.getRgOrgaoEmissor());
						relacionador.setRgUf(relacionado.getRgUf());
						break;
					}
				}
				relacionador.setRelacionamento(relacionador.getRelacionamento().infoBasica());
			}
		}
		if (result.getTelefoneList() != null) {
			for (PessoaTelefone pessoaTelefone : result.getTelefoneList()) {
				pessoaTelefone.setPessoa(null);
			}
		}

		if (result.getPendenciaList() != null) {
			for (PessoaPendencia pessoaPendencia : result.getPendenciaList()) {
				pessoaPendencia.setPessoa(null);
			}
		}

		if (result.getInclusaoUsuario() != null) {
			result.setInclusaoUsuario(result.getInclusaoUsuario().infoBasica());
		}
		if (result.getAlteracaoUsuario() != null) {
			result.setAlteracaoUsuario(result.getAlteracaoUsuario().infoBasica());
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}
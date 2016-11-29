package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
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
	private FacadeBo facadeBo;

	@Autowired
	private EntityManager em;

	@SuppressWarnings("unchecked")
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
			if (!CollectionUtils.isEmpty(publicoAlvo.getPublicoAlvoPropriedadeRuralList())) {
				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvo.getPublicoAlvoPropriedadeRuralList()) {
					publicoAlvoPropriedadeRural.setPublicoAlvo(publicoAlvoPropriedadeRural.getPublicoAlvo().infoBasica());
					publicoAlvoPropriedadeRural.setComunidade(publicoAlvoPropriedadeRural.getComunidade().infoBasica());
					publicoAlvoPropriedadeRural.setPropriedadeRural(publicoAlvoPropriedadeRural.getPropriedadeRural() != null ? publicoAlvoPropriedadeRural.getPropriedadeRural().infoBasica() : null);
				}
			}
			if (!CollectionUtils.isEmpty(publicoAlvo.getPublicoAlvoSetorList())) {
				for (PublicoAlvoSetor publicoAlvoSetor : publicoAlvo.getPublicoAlvoSetorList()) {
					publicoAlvoSetor.setPublicoAlvo(null);
				}
			}
			// captar o índice de produção do produtor
			IndiceProducaoCadFiltroDto filtro = new IndiceProducaoCadFiltroDto();
			Calendar hoje = Calendar.getInstance();
			filtro.setAno(hoje.get(Calendar.YEAR));
			filtro.setPublicoAlvo(publicoAlvo);
			publicoAlvo.setIndiceProducaoList((List<Object>) facadeBo.indiceProducaoFiltroProducaoPublicoAlvo(contexto.getUsuario(), filtro).getResposta());
		}

		// fetch nas tabelas de apoio
		if (!CollectionUtils.isEmpty(result.getArquivoList())) {
			for (PessoaArquivo pessoaArquivo : result.getArquivoList()) {
				pessoaArquivo.setPessoa(null);
				pessoaArquivo.setArquivo(pessoaArquivo.getArquivo() == null ? null : pessoaArquivo.getArquivo().infoBasica());
			}
		}
		if (!CollectionUtils.isEmpty(result.getEmailList())) {
			for (PessoaEmail pessoaEmail : result.getEmailList()) {
				pessoaEmail.setPessoa(null);
			}
		}
		if (!CollectionUtils.isEmpty(result.getEnderecoList())) {
			for (PessoaEndereco pessoaEndereco : result.getEnderecoList()) {
				pessoaEndereco.setEndereco(pessoaEndereco.getEndereco().infoBasica());
				pessoaEndereco.setPessoa(null);
			}
		}
		if (!CollectionUtils.isEmpty(result.getGrupoSocialList())) {
			for (PessoaGrupoSocial pessoaGrupoSocial : result.getGrupoSocialList()) {
				pessoaGrupoSocial.setPessoa(null);
			}
		}
		if (!CollectionUtils.isEmpty(result.getRelacionamentoList())) {
			for (PessoaRelacionamento relacionador : result.getRelacionamentoList()) {
				for (PessoaRelacionamento relacionado : relacionador.getRelacionamento().getPessoaRelacionamentoList()) {
					// aqui o registro original vem selecionado como o do
					// relacionador, ou seja, da pessoa que foi consultada
					// por isso é necessário inverter os dados com o do
					// relacionado, para envio a tela, para futuras modificações
					if (relacionado.getPessoa() != null && result.getId().equals(relacionado.getPessoa().getId())) {
						continue;
					}
					if (relacionado.getPessoa() == null) {
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
					} else {
						// informações principais
						relacionador.setId(relacionado.getId());
						relacionador.setRelacionamentoFuncao(relacionado.getRelacionamentoFuncao() == null ? null : relacionado.getRelacionamentoFuncao().infoBasica());
						relacionador.setChaveSisater(relacionado.getChaveSisater());

						// captar os dados do relacionado
						relacionador.setPessoa(relacionado.getPessoa().infoBasica());
						break;
					}
				}
				relacionador.setRelacionamento(relacionador.getRelacionamento().infoBasica());
			}
		}
		if (!CollectionUtils.isEmpty(result.getTelefoneList())) {
			for (PessoaTelefone pessoaTelefone : result.getTelefoneList()) {
				pessoaTelefone.setPessoa(null);
			}
		}

		if (!CollectionUtils.isEmpty(result.getPendenciaList())) {
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
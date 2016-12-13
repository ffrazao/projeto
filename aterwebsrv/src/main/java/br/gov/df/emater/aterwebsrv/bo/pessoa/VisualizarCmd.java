package br.gov.df.emater.aterwebsrv.bo.pessoa;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

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
				pessoaFisica.setNascimentoEstado(infoBasicaReg(pessoaFisica.getNascimentoMunicipio().getEstado()));
				pessoaFisica.setNascimentoMunicipio(infoBasicaReg(pessoaFisica.getNascimentoMunicipio()));
			}
			if (pessoaFisica.getNascimentoPais() != null) {
				pessoaFisica.setNascimentoPais(infoBasicaReg(pessoaFisica.getNascimentoPais()));
			}
		} else if (result instanceof PessoaJuridica) {

		}

		result.setPerfilArquivo(infoBasicaReg(result.getPerfilArquivo()));

		if (Confirmacao.S.equals(result.getPublicoAlvoConfirmacao())) {
			PublicoAlvo pa = result.getPublicoAlvo();
			if (!CollectionUtils.isEmpty(pa.getPublicoAlvoPropriedadeRuralList())) {
				for (PublicoAlvoPropriedadeRural papr : pa.getPublicoAlvoPropriedadeRuralList()) {
					papr.setPublicoAlvo(infoBasicaReg(papr.getPublicoAlvo()));
					papr.setComunidade(infoBasicaReg(papr.getComunidade()));
					papr.setPropriedadeRural(infoBasicaReg(papr.getPropriedadeRural()));
				}
			}
			if (!CollectionUtils.isEmpty(pa.getPublicoAlvoSetorList())) {
				for (PublicoAlvoSetor publicoAlvoSetor : pa.getPublicoAlvoSetorList()) {
					publicoAlvoSetor.setPublicoAlvo(null);
				}
			}
			// captar o índice de produção do produtor
			IndiceProducaoCadFiltroDto filtro = new IndiceProducaoCadFiltroDto();
			Calendar hoje = Calendar.getInstance();
			filtro.setAno(hoje.get(Calendar.YEAR));
			filtro.setPublicoAlvo(pa);
			pa.setIndiceProducaoList((List<Object>) facadeBo.indiceProducaoFiltroProducaoPublicoAlvo(contexto.getUsuario(), filtro).getResposta());
			pa.setPessoa(null);
		}

		// fetch nas tabelas de apoio
		if (!CollectionUtils.isEmpty(result.getArquivoList())) {
			for (PessoaArquivo pessoaArquivo : result.getArquivoList()) {
				pessoaArquivo.setPessoa(null);
				pessoaArquivo.setArquivo(infoBasicaReg(pessoaArquivo.getArquivo()));
			}
		}
		if (!CollectionUtils.isEmpty(result.getEmailList())) {
			for (PessoaEmail pessoaEmail : result.getEmailList()) {
				pessoaEmail.setPessoa(null);
			}
		}
		if (!CollectionUtils.isEmpty(result.getEnderecoList())) {
			for (PessoaEndereco pessoaEndereco : result.getEnderecoList()) {
				pessoaEndereco.setEndereco(infoBasicaReg(pessoaEndereco.getEndereco()));
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
						relacionador.setRelacionamentoFuncao(infoBasicaReg(relacionado.getRelacionamentoFuncao()));
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
							relacionador.setNascimentoEstado(infoBasicaReg(relacionado.getNascimentoMunicipio().getEstado()));
							relacionador.setNascimentoMunicipio(infoBasicaReg(relacionado.getNascimentoMunicipio()));
						}
						relacionador.setNascimentoPais(infoBasicaReg(relacionado.getNascimentoPais()));
						relacionador.setNome(relacionado.getNome());
						relacionador.setNomeMae(relacionado.getNomeMae());
						relacionador.setProfissao(infoBasicaReg(relacionado.getProfissao()));
						relacionador.setRgDataEmissao(relacionado.getRgDataEmissao());
						relacionador.setRgNumero(relacionado.getRgNumero());
						relacionador.setRgOrgaoEmissor(relacionado.getRgOrgaoEmissor());
						relacionador.setRgUf(relacionado.getRgUf());
						break;
					} else {
						// informações principais
						relacionador.setId(relacionado.getId());
						relacionador.setRelacionamentoFuncao(infoBasicaReg(relacionado.getRelacionamentoFuncao()));
						relacionador.setChaveSisater(relacionado.getChaveSisater());

						// captar os dados do relacionado
						relacionador.setPessoa(infoBasicaReg(relacionado.getPessoa()));
						break;
					}
				}
				relacionador.setRelacionamento(infoBasicaReg(relacionador.getRelacionamento()));
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
			result.setInclusaoUsuario(infoBasicaReg(result.getInclusaoUsuario()));
		}
		if (result.getAlteracaoUsuario() != null) {
			result.setAlteracaoUsuario(infoBasicaReg(result.getAlteracaoUsuario()));
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}
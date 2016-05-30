package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralFormaUtilizacaoEspacoRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.AreaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EnderecoDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralArquivo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralFormaUtilizacaoEspacoRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Area;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;

@Service("PropriedadeRuralSalvarCmd")
public class SalvarCmd extends _Comando {
	
	@Autowired
	private PropriedadeRuralFormaUtilizacaoEspacoRuralDao propriedadeRuralFormaUtilizacaoEspacoRuralDao;

	@Autowired
	private AreaDao areaDao;

	@Autowired
	private ArquivoDao arquivoDao;

	@Autowired
	private PropriedadeRuralDao dao;

	@Autowired
	private EnderecoDao enderecoDao;

	@Autowired
	private PropriedadeRuralArquivoDao propriedadeRuralArquivoDao;

	@Autowired
	private PublicoAlvoPropriedadeRuralDao publicoAlvoPropriedadeRuralDao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PropriedadeRural result = (PropriedadeRural) contexto.getRequisicao();
		if (result.getId() == null) {
			result.setInclusaoUsuario(getUsuario(contexto.getUsuario().getName()));
			result.setInclusaoData(Calendar.getInstance());
		} else {
			result.setInclusaoUsuario(result.getInclusaoUsuario() == null ? null : getUsuario(result.getInclusaoUsuario().getUsername()));
		}
		result.setAlteracaoUsuario(contexto.getUsuario() == null ? null : getUsuario(contexto.getUsuario().getName()));
		result.setAlteracaoData(Calendar.getInstance());

		if (result.getEndereco() == null) {
			throw new BoException("O campo Endereço é obrigatório");
		}
		Endereco endereco = result.getEndereco();
		endereco.setPropriedadeRuralConfirmacao(Confirmacao.S);
		
		if (result.getEndereco().getId() != null) {			
			Endereco enderecoSalvo = enderecoDao.findOne(result.getEndereco().getId());
			if (enderecoSalvo != null && enderecoSalvo.getAreaList() != null) {
				for (Area areaSalvo: enderecoSalvo.getAreaList()) {
					boolean encontrou = false;
					if (endereco.getAreaList() != null) {
						for (Area area: endereco.getAreaList()) {
							if (area.getId().equals(areaSalvo.getId())) {
								encontrou = true;
								break;
							}
						}
					}
					if (!encontrou) {
						areaDao.delete(areaSalvo);
					}
				}
			}
		}

		if (endereco.getAreaList() != null) {
			for (Area area: endereco.getAreaList()) {
				area.setEndereco(endereco);
			}
		}
		enderecoDao.save(endereco);
		
		
		// ajustar o setor
		if (result.getFormaUtilizacaoEspacoRuralList() != null) {
			for (PropriedadeRuralFormaUtilizacaoEspacoRural formaUtilizacaoEspacoRural : result.getFormaUtilizacaoEspacoRuralList()) {
				formaUtilizacaoEspacoRural.setPropriedadeRural(result);
				if (result.getId() != null) {
					PropriedadeRuralFormaUtilizacaoEspacoRural salvo = propriedadeRuralFormaUtilizacaoEspacoRuralDao.findOneByPropriedadeRuralAndFormaUtilizacaoEspacoRural(result, formaUtilizacaoEspacoRural.getFormaUtilizacaoEspacoRural());
					if (salvo != null) {
						formaUtilizacaoEspacoRural.setId(salvo.getId());
					}					
				}
			}
		}
		
		dao.save(result);
		
		if (result.getPublicoAlvoPropriedadeRuralList() != null) {
			for (PublicoAlvoPropriedadeRural papr: result.getPublicoAlvoPropriedadeRuralList()) {
				papr.setPropriedadeRural(result);
				papr.setPublicoAlvo(publicoAlvoDao.findOneByPessoa(papr.getPublicoAlvo().getPessoa()));
				publicoAlvoPropriedadeRuralDao.save(papr);
			}
		}

		if (result.getArquivoList() != null) {
			// tratar a exclusao de registros
			for (PropriedadeRuralArquivo pessoaArquivo : result.getArquivoList()) {
				if (CadastroAcao.E.equals(pessoaArquivo.getCadastroAcao())) {
					propriedadeRuralArquivoDao.delete(pessoaArquivo);
				}
			}
			// tratar a insersao de registros
			for (PropriedadeRuralArquivo propriedadeRuralArquivo : result.getArquivoList()) {
				if (!CadastroAcao.E.equals(propriedadeRuralArquivo.getCadastroAcao())) {
					Arquivo arquivo = propriedadeRuralArquivo.getArquivo();
					arquivo = arquivoDao.findByMd5(arquivo.getMd5());
					if (arquivo != null) {
						arquivo.setMd5(propriedadeRuralArquivo.getArquivo().getMd5());
						arquivo.setNomeOriginal(propriedadeRuralArquivo.getArquivo().getNomeOriginal());
						arquivo.setDataUpload(propriedadeRuralArquivo.getArquivo().getDataUpload());
						arquivo.setExtensao(propriedadeRuralArquivo.getArquivo().getExtensao());
						arquivo.setTamanho(propriedadeRuralArquivo.getArquivo().getTamanho());
						arquivo.setTipo(propriedadeRuralArquivo.getArquivo().getTipo());
					}
					arquivo = arquivoDao.save(arquivo);
					propriedadeRuralArquivo.setArquivo(arquivo);
					propriedadeRuralArquivo.setPropriedadeRural(result);
					
					PropriedadeRuralArquivo propriedadeRuralArquivoSalvo = propriedadeRuralArquivoDao.findOneByPropriedadeRuralAndArquivo(result, arquivo);
					if (propriedadeRuralArquivoSalvo != null) {
						propriedadeRuralArquivo.setId(propriedadeRuralArquivoSalvo.getId());
					}
					propriedadeRuralArquivoDao.save(propriedadeRuralArquivo);
				}
			}
		}
		dao.flush();

		contexto.setResposta(result.getId());
		return true;
	}

}
package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import static br.gov.df.emater.aterwebsrv.bo.indice_producao.IndiceProducaoUtil.getComposicaoValorId;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ferramenta.UtilDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoComposicaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoCadDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;
          
@Service("IndiceProducaoSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private BemDao bemDao;

	@Autowired
	private ProducaoProprietarioDao dao;

	@Autowired
	private ProducaoComposicaoDao producaoComposicaoDao;

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	@Autowired
	private ProducaoDao producaoDao;

	@Autowired
	private UtilDao utilDao;

	public SalvarCmd() {
	}
	
	
	
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {


		final ProducaoGravaDto result = (ProducaoGravaDto) contexto.getRequisicao();
		
		
		//ProducaoProprietario producaoProprietario =  result.getProducaoProprietario();
		List<Object> producaoAgricolaList = result.getProducaoAgricolaList();


		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(result);
        System.out.println("JSON = " + json);
			 
		
		// atualizar o log do registro
		//logAtualizar(producao, contexto);

	
		//contexto.setResposta(producao.getId());
		return false;
	}


	
	public boolean executarOld(_Contexto contexto) throws Exception {

		final ProducaoProprietario result = (ProducaoProprietario) contexto.getRequisicao();
		
		contexto.put("producaoProprietarioList", result.getProducaoProprietarioList());

		
		// criticar o registro
		if (result.getUnidadeOrganizacional() == null && result.getPublicoAlvo() == null && result.getPropriedadeRural() == null) {
			throw new BoException("Não foi informado o responsável pela produção.");
		}
		if (result.getAno() == null) {
			throw new BoException("Não foi informado o ano da produção.");
		}
		if (result.getBemClassificado() == null || result.getBemClassificado().getId() == null) {
			throw new BoException("Não foi informado o bem da produção.");
		}
		if (CollectionUtils.isEmpty(result.getProducaoList())) {
			throw new BoException("Não foi informado a forma de produção do bem.");
		}
		

		// atualizar o log do registro
		logAtualizar(result, contexto);

		limparChavePrimaria(result.getProducaoList());
		limparChavePrimaria(result.getProducaoProprietarioList());

		// avaliar e excluir duplicatas de formas de produção
		final Set<String> composicaoList = new HashSet<>();
		for (int i = result.getProducaoList().size() - 1; i >= 0; i--) {
			final Producao producao = result.getProducaoList().get(i);
			final String composicao = getComposicaoValorId(producao);
			if (composicaoList.contains(composicao)) {
				// remover as formas de produção duplicadas
				if (producao.getId() != null && producaoDao.exists(producao.getId())) {
					producaoDao.delete(producao.getId());
				}
				result.getProducaoList().remove(i);
			}
			composicaoList.add(composicao);
		}

		ProducaoProprietario salvo = null;
		if (result.getUnidadeOrganizacional() != null) {
			result.setUnidadeOrganizacional(unidadeOrganizacionalDao.findOne(result.getUnidadeOrganizacional().getId()));
			if (!UnidadeOrganizacionalClassificacao.OP.equals(result.getUnidadeOrganizacional().getClassificacao())) {
				// TODO - remover este comentário após apresentação ao presid
				// throw new BoException("A classificação da Unidade
				// Organizacional informada não permite lançamento de IPA");
			}
			result.setPublicoAlvo(null);
			result.setPropriedadeRural(null);
			salvo = dao.findOneByAnoAndBemClassificadoAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(result.getAno(), result.getBemClassificado(), result.getUnidadeOrganizacional());
		} else {
			result.setUnidadeOrganizacional(null);
			if (result.getPublicoAlvo() != null) {
				result.setPublicoAlvo(publicoAlvoDao.findOne(result.getPublicoAlvo().getId()));
			}
			if (result.getPropriedadeRural() != null) {
				result.setPropriedadeRural(propriedadeRuralDao.findOne(result.getPropriedadeRural().getId()));
			}
			salvo = dao.findOneByAnoAndBemClassificadoAndPublicoAlvoAndPropriedadeRuralAndUnidadeOrganizacionalIsNull(result.getAno(), result.getBemClassificado(), result.getPublicoAlvo(), result.getPropriedadeRural());
		}

		// verificar se o registro já foi salvo
		if (salvo != null && !salvo.getId().equals(result.getId())) {
			throw new BoException("Registro já cadastrado");
		}

		// restaurar os dados do bem de produção
		result.setBemClassificado(bemDao.findOne(result.getBemClassificado().getId()));
		List<Producao> producaoList = new ArrayList<>();
		for (Producao pf: result.getProducaoList()) {
			if (pf.getId() == null) {
				producaoList.add(new Producao(pf));
			} else {
				producaoList.add(pf);
			}
		};

		dao.save(result);

		// recuperar as chaves primárias do registro salvo
		producaoRecuperarId(result, salvo);

		final Map<String, Object> bemClassificacaoDetalhe = utilDao.ipaBemClassificacaoDetalhes(result.getBemClassificado().getBemClassificacao());

		// proceder a exclusao dos registros
		excluirRegistros(result, "producaoList", producaoDao);

		// salvar a forma de produção
		producaoList.forEach((producao) -> {
			producao.setProducaoProprietario(result);
			logAtualizar(producao, contexto);
			try {
				producao.setVolume(((FormulaProduto) bemClassificacaoDetalhe.get("formulaProduto")).volume(producao));
			} catch (Exception e) {
				producao.setVolume(null);
				e.printStackTrace();
			}
			if (producao.getVolume() != null && producao.getValorUnitario() != null) {
				producao.setValorTotal(producao.getVolume().multiply(producao.getValorUnitario(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			} else {
				producao.setValorTotal(null);
			}
			
			List<ProducaoComposicao> producaoComposicaoList = producao.getProducaoComposicaoList();
			
			producaoDao.save(producao);

			Integer ordem = 0;
			for (ProducaoComposicao producaoComposicao : producaoComposicaoList) {
				// se não foi excluido
				producaoComposicao.setProducao(producao);
				producaoComposicao.setOrdem(++ordem);
				producaoComposicaoDao.save(producaoComposicao);
			}
		});

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}

	private void producaoRecuperarId(final ProducaoProprietario result, final ProducaoProprietario salvo) {
		if (salvo != null && !CollectionUtils.isEmpty(salvo.getProducaoList()) && !CollectionUtils.isEmpty(result.getProducaoList())) {
			// recuperar as chaves primárias das formas de produção
			result.getProducaoList().forEach((producao) -> {
				String composicao = getComposicaoValorId(producao);
				salvo.getProducaoList().stream().filter((producaoSalvo) -> producaoSalvo.getId() != null && !producaoSalvo.getId().equals(producao.getId()) && getComposicaoValorId(producaoSalvo).equals(composicao)).collect(Collectors.toList())
						.forEach((producaoSalvo) -> {
							producao.setId(producaoSalvo.getId());
							producao.setProducaoComposicaoList(producaoSalvo.getProducaoComposicaoList());
						});
			});
		}
	}

}
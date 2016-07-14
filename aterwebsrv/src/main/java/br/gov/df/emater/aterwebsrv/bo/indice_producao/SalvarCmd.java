package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import static br.gov.df.emater.aterwebsrv.bo.indice_producao.IndiceProducaoUtil.getComposicaoValorId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaComposicaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("IndiceProducaoSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private BemDao bemDao;

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private ProducaoFormaComposicaoDao producaoFormaComposicaoDao;

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	@Autowired
	private ProducaoFormaDao producaoFormaDao;

	@Autowired
	private UtilDao utilDao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		final Producao result = (Producao) contexto.getRequisicao();

		// criticar o registro
		if (result.getUnidadeOrganizacional() == null && result.getPublicoAlvo() == null && result.getPropriedadeRural() == null) {
			throw new BoException("Não foi informado o responsável pela produção.");
		}
		if (result.getAno() == null) {
			throw new BoException("Não foi informado o ano da produção.");
		}
		if (result.getBem() == null) {
			throw new BoException("Não foi informado o bem da produção.");
		}
		if (CollectionUtils.isEmpty(result.getProducaoFormaList())) {
			throw new BoException("Não foi informado a forma de produção do bem.");
		}

		// atualizar o log do registro
		logAtualizar(result, contexto);

		limparChavePrimaria(result.getProducaoFormaList());

		// avaliar e excluir duplicatas de formas de produção
		final List<String> composicaoList = new ArrayList<>();
		for (int i = result.getProducaoFormaList().size() - 1; i >= 0; i--) {
			final ProducaoForma producaoForma = result.getProducaoFormaList().get(i);
			final String composicao = getComposicaoValorId(producaoForma);
			if (composicaoList.contains(composicao)) {
				// remover as formas de produção duplicadas
				if (producaoForma.getId() != null && producaoFormaDao.exists(producaoForma.getId())) {
					producaoFormaDao.delete(producaoForma.getId());
				}
				result.getProducaoFormaList().remove(i);
			}
			composicaoList.add(composicao);
		}

		Producao salvo = null;
		if (result.getPublicoAlvo() != null) {
			result.setUnidadeOrganizacional(null);
			result.setPublicoAlvo(publicoAlvoDao.findOne(result.getPublicoAlvo().getId()));
			if (result.getPropriedadeRural() != null) {
				result.setPropriedadeRural(propriedadeRuralDao.findOne(result.getPropriedadeRural().getId()));
			}
			salvo = dao.findOneByAnoAndBemAndPublicoAlvoAndPropriedadeRuralAndUnidadeOrganizacionalIsNull(result.getAno(), result.getBem(), result.getPublicoAlvo(), result.getPropriedadeRural());
		} else {
			if (result.getUnidadeOrganizacional() == null) {
				throw new BoException("Não foi informada a Unidade Organizacional responsável pela produção.");
			}
			result.setUnidadeOrganizacional(unidadeOrganizacionalDao.findOne(result.getUnidadeOrganizacional().getId()));
			if (!UnidadeOrganizacionalClassificacao.OP.equals(result.getUnidadeOrganizacional().getClassificacao())) {
				// TODO - remover este comentário após apresentação ao presid
				// throw new BoException("A classificação da Unidade
				// Organizacional informada não permite lançamento de IPA");
			}
			result.setPublicoAlvo(null);
			result.setPropriedadeRural(null);
			salvo = dao.findOneByAnoAndBemAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(result.getAno(), result.getBem(), result.getUnidadeOrganizacional());
		}

		// verificar se o registro já foi salvo
		if (salvo != null && !salvo.getId().equals(result.getId())) {
			throw new BoException("Registro já cadastrado");
		}

		// restaurar os dados do bem de produção
		result.setBem(bemDao.findOne(result.getBem().getId()));

		dao.save(result);

		// recuperar as chaves primárias do registro salvo
		producaoFormaRecuperarId(result, salvo);

		final Map<String, Object> bemClassificacaoDetalhe = utilDao.ipaBemClassificacaoDetalhes(result.getBem().getBemClassificacao());

		// proceder a exclusao dos registros
		excluirRegistros(result, "producaoFormaList", producaoFormaDao);

		// salvar a forma de produção
		result.getProducaoFormaList().forEach((producaoForma) -> {
			producaoForma.setProducao(result);
			logAtualizar(producaoForma, contexto);
			try {
				producaoForma.setVolume(((FormulaProduto) bemClassificacaoDetalhe.get("formulaProduto")).volume(producaoForma));
			} catch (Exception e) {
				producaoForma.setVolume(null);
				e.printStackTrace();
			}
			if (producaoForma.getVolume() != null && producaoForma.getValorUnitario() != null) {
				producaoForma.setValorTotal(producaoForma.getVolume().multiply(producaoForma.getValorUnitario(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			} else {
				producaoForma.setValorTotal(null);
			}
			producaoFormaDao.save(producaoForma);

			Integer ordem = 0;
			for (ProducaoFormaComposicao producaoFormaComposicao : producaoForma.getProducaoFormaComposicaoList()) {
				// se não foi excluido
				producaoFormaComposicao.setProducaoForma(producaoForma);
				producaoFormaComposicao.setOrdem(++ordem);
				producaoFormaComposicaoDao.save(producaoFormaComposicao);
			}
		});

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}

	private void producaoFormaRecuperarId(final Producao result, final Producao salvo) {
		if (salvo != null && !CollectionUtils.isEmpty(salvo.getProducaoFormaList()) && !CollectionUtils.isEmpty(result.getProducaoFormaList())) {
			// recuperar as chaves primárias das formas de produção
			result.getProducaoFormaList().forEach((producaoForma) -> {
				String composicao = getComposicaoValorId(producaoForma);
				salvo.getProducaoFormaList().stream().filter((producaoFormaSalvo) -> producaoFormaSalvo.getId() != null && !producaoFormaSalvo.getId().equals(producaoForma.getId()) && getComposicaoValorId(producaoFormaSalvo).equals(composicao)).collect(Collectors.toList())
						.forEach((producaoFormaSalvo) -> {
							producaoForma.setId(producaoFormaSalvo.getId());
							producaoForma.setProducaoFormaComposicaoList(producaoFormaSalvo.getProducaoFormaComposicaoList());
						});
			});
		}
	}

}
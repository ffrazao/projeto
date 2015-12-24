package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemFormaProducaoMediaDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dto.PropriedadeRuralPorComunidadePublicoAlvoDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemFormaProducaoMedia;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemFormaProducaoMediaComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("PropriedadeRuralFiltrarPorPublicoAlvoEComunidadeCmd")
public class FiltrarPorPublicoAlvoEComunidadeCmd extends _Comando {

	@Autowired
	private BemDao bemDao;

	@Autowired
	private BemFormaProducaoMediaDao bemFormaProducaoMediaDao;

	@Autowired
	private ComunidadeDao comunidadeDao;

	@Autowired
	private PublicoAlvoPropriedadeRuralDao dao;

	@Autowired
	private ProducaoDao producaoDao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<Producao> result = null;

		PropriedadeRuralPorComunidadePublicoAlvoDto requisicao = (PropriedadeRuralPorComunidadePublicoAlvoDto) contexto.getRequisicao();

		Comunidade comunidade = comunidadeDao.findOne(requisicao.getComunidade().getId());

		Bem bem = bemDao.findOne(requisicao.getBem().getId());

		Producao producaoEsperada = producaoDao.findOneByAnoAndBemAndComunidadeAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(requisicao.getAno(), bem, comunidade);

		Calendar inicio = Calendar.getInstance();

		Calendar termino = Calendar.getInstance();

		inicio.set(requisicao.getAno(), 0, 01);
		termino.set(requisicao.getAno(), 11, 31);

		// percorrer o publico alvo informado
		for (PublicoAlvo publicoAlvo : requisicao.getPublicoAlvoList()) {
			// Verificar se o publico alvo esta ativo
			PublicoAlvo pa = publicoAlvoDao.findOneByPessoa(publicoAlvo.getPessoa());
			if (pa == null || !PessoaSituacao.A.equals(pa.getPessoa().getSituacao()) || Confirmacao.N.equals(pa.getPessoa().getPublicoAlvoConfirmacao())) {
				continue;
			}
			List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList = dao.findByComunidadeAndPublicoAlvoAndPropriedadeRuralIdIsNotNull(comunidade, pa);

			if (publicoAlvoPropriedadeRuralList != null && publicoAlvoPropriedadeRuralList.size() > 0) {
				Producao producao = null;

				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvoPropriedadeRuralList) {
					// Verificar se o vinculo com a propriedade esta ativo
					if (publicoAlvoPropriedadeRural.getInicio().after(termino) || ((publicoAlvoPropriedadeRural.getTermino() != null && publicoAlvoPropriedadeRural.getTermino().before(inicio)))) {
						continue;
					}

					List<ProducaoForma> producaoFormaList = null;

					// captar dados da producao
					if (producaoEsperada != null) {
						producao = new Producao();
						producao.setAno(producaoEsperada.getAno());
						producao.setBem(producaoEsperada.getBem().infoBasica());
						producao.setPropriedadeRural(publicoAlvoPropriedadeRural.getPropriedadeRural().infoBasica());
						producao.setPublicoAlvo(pa.infoBasica());

						// captar as médias de producao da propriedade e do
						// produtor
						List<BemFormaProducaoMedia> bemFormaProducaoMediaList = bemFormaProducaoMediaDao.findByBemAndPropriedadeRuralAndPublicoAlvo(bem, producao.getPropriedadeRural(), producao.getPublicoAlvo());

						if (producaoEsperada.getProducaoFormaList() != null) {
							for (ProducaoForma producaoForma : producaoEsperada.getProducaoFormaList()) {
								// encontrar média de producao pela composição
								// da forma de produção
								BemFormaProducaoMedia bemFormaProducaoMedia = pegaBemFormaProducaoMedia(producaoForma.getProducaoFormaComposicaoList(), bemFormaProducaoMediaList);
								ProducaoForma pf = new ProducaoForma();
								if (bemFormaProducaoMedia != null) {
									pf.setItemAValor(bemFormaProducaoMedia.getItemAMedia());
									pf.setItemBValor(bemFormaProducaoMedia.getItemBMedia());
									pf.setItemCValor(bemFormaProducaoMedia.getItemCMedia());
									pf.setVolume(bemFormaProducaoMedia.getVolume());
									pf.setValorTotal(bemFormaProducaoMedia.getValorTotal());
									pf.setValorUnitario(bemFormaProducaoMedia.getValorUnitario());
								} else {
									// captar valores da comunidade
									pf.setItemAValor(producaoForma.getItemAValor());
									pf.setItemBValor(producaoForma.getItemBValor());
									pf.setItemCValor(producaoForma.getItemCValor());
									pf.setVolume(producaoForma.getVolume());
									pf.setValorTotal(producaoForma.getValorTotal());
									pf.setValorUnitario(producaoForma.getValorUnitario());
								}
								List<ProducaoFormaComposicao> producaoFormaComposicaoList = new ArrayList<ProducaoFormaComposicao>();
								for (ProducaoFormaComposicao producaoFormaComposicao : producaoForma.getProducaoFormaComposicaoList()) {
									ProducaoFormaComposicao pfc = new ProducaoFormaComposicao();
									pfc.setOrdem(producaoFormaComposicao.getOrdem());
									pfc.setFormaProducaoValor(producaoFormaComposicao.getFormaProducaoValor().infoBasica());
									producaoFormaComposicaoList.add(pfc);
								}
								pf.setProducaoFormaComposicaoList(producaoFormaComposicaoList);

								if (producaoFormaList == null) {
									producaoFormaList = new ArrayList<ProducaoForma>();
								}
								producaoFormaList.add(pf);
							}
						}
					}
					if (producao != null && producaoFormaList != null) {
						producao.setProducaoFormaList(producaoFormaList);

						if (result == null) {
							result = new ArrayList<Producao>();
						}
						result.add(producao);
					}
				}
			}
		}

		contexto.setResposta(result);

		return false;
	}

	private BemFormaProducaoMedia pegaBemFormaProducaoMedia(List<ProducaoFormaComposicao> producaoFormaComposicaoList, List<BemFormaProducaoMedia> bemFormaProducaoMediaList) {
		if (producaoFormaComposicaoList == null || bemFormaProducaoMediaList == null) {
			return null;
		}

		Integer producao[] = new Integer[producaoFormaComposicaoList.size()];
		int pos = 0;
		for (ProducaoFormaComposicao producaoFormaComposicao : producaoFormaComposicaoList) {
			producao[pos++] = producaoFormaComposicao.getFormaProducaoValor().getId();
		}
		Arrays.sort(producao);

		media: for (BemFormaProducaoMedia bemFormaProducaoMedia : bemFormaProducaoMediaList) {
			Integer media[] = new Integer[bemFormaProducaoMedia.getBemFormaProducaoMediaComposicaoList().size()];
			pos = 0;
			for (BemFormaProducaoMediaComposicao bemFormaProducaoMediaComposicao : bemFormaProducaoMedia.getBemFormaProducaoMediaComposicaoList()) {
				media[pos++] = bemFormaProducaoMediaComposicao.getFormaProducaoValor().getId();
			}
			Arrays.sort(media);

			if (media.length != producao.length) {
				continue;
			}

			for (pos = 0; pos < media.length; pos++) {
				if (media[pos] != producao[pos]) {
					continue media;
				}
			}
			// encontrou a media
			return bemFormaProducaoMedia;
		}

		// não encontrou nada
		return null;
	}

}

package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormularioDestino;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralCronogramaPagamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFinanciamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFluxoCaixa;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRural;

@Service("ProjetoCreditoRuralCalcularFluxoCaixaCmd")
public class CalcularFluxoCaixaCmd extends _SalvarCmd {

	private class Item {

		private boolean cronograma;

		private String nomeLote;

		public Item(String nomeLote) {
			this.setNomeLote(nomeLote);
		}

		public String getNomeLote() {
			return nomeLote;
		}

		public boolean isCronograma() {
			return cronograma;
		}

		public void setCronograma(boolean cronograma) {
			this.cronograma = cronograma;
		}

		public void setNomeLote(String nomeLote) {
			this.nomeLote = nomeLote;
		}
	}

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private EntityManager em;

	@Autowired
	private PublicoAlvoPropriedadeRuralDao publicoAlvoPropriedadeRuralDao;

	@Autowired
	private FacadeBo facadeBo;

	public CalcularFluxoCaixaCmd() {
	};

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRural result = (ProjetoCreditoRural) contexto.getRequisicao();

		// verificar se há cronogramas e se estão corretamente selecionados
		List<Item> investimentoNomeLote, custeioNomeLote;
		investimentoNomeLote = pegarNomeLote(result.getInvestimentoList());
		custeioNomeLote = pegarNomeLote(result.getCusteioList());

		if (investimentoNomeLote.size() == 0 && custeioNomeLote.size() == 0) {
			throw new BoException("Nenhum lote informado", "Erro ao calcular fluxo de caixa");
		}
		Collections.sort(investimentoNomeLote, (n1, n2) -> n1.getNomeLote().compareTo(n2.getNomeLote()));
		Collections.sort(custeioNomeLote, (n1, n2) -> n1.getNomeLote().compareTo(n2.getNomeLote()));

		nomeLoteTemCronograma(investimentoNomeLote, result.getCronogramaPagamentoInvestimentoList());
		nomeLoteTemCronograma(custeioNomeLote, result.getCronogramaPagamentoCusteioList());

		List<String> investimentoNomeLoteSemCronogrma = new ArrayList<>();
		investimentoNomeLote.forEach((v) -> {
			if (!v.isCronograma()) {
				investimentoNomeLoteSemCronogrma.add(v.getNomeLote());
			}
		});
		List<String> custeioNomeLoteSemCronogrma = new ArrayList<>();
		custeioNomeLote.forEach((v) -> {
			if (!v.isCronograma()) {
				custeioNomeLoteSemCronogrma.add(v.getNomeLote());
			}
		});
		if (investimentoNomeLoteSemCronogrma.size() > 0 || custeioNomeLoteSemCronogrma.size() > 0) {
			throw new BoException("Há lotes sem cronograma de pagamento | De investimento (" + investimentoNomeLoteSemCronogrma + ") | De custeio(" + custeioNomeLoteSemCronogrma + ")", "Erro ao calcular fluxo de caixa");
		}
		if (result.getPublicoAlvo() == null) {
			throw new BoException("Não informado o beneficiário do Projeto de Crédito Rural");
		}
		if (result.getPublicoAlvoPropriedadeRuralList() == null) {
			throw new BoException("Não informado a(s) Propriedade(s) Rural(ais) do beneficiário do Projeto de Crédito Rural");
		}

		// recuperar dados do publico alvo
		result.setPublicoAlvo(publicoAlvoDao.findOne(result.getPublicoAlvo().getId()));
		
		// captar diagnosticos
		FormularioColetaCadFiltroDto filtro = new FormularioColetaCadFiltroDto();
		Set<FormularioDestino> destinoList = new HashSet<>();
		destinoList.add(FormularioDestino.PE);
		filtro.setDestino(destinoList);
		Set<Confirmacao> subFormularioList = new HashSet<>();
		subFormularioList.add(Confirmacao.N);
		filtro.setSubformulario(subFormularioList);
		Set<Situacao> situacaoList = new HashSet<>();
		situacaoList.add(Situacao.A);
		filtro.setSituacao(situacaoList);
		filtro.setPessoa(result.getPublicoAlvo().getPessoa());
		_Contexto diagnosticoCtx = facadeBo.formularioColetaFiltroExecutar(contexto.getUsuario(), filtro);
		result.getPublicoAlvo().getPessoa().setDiagnosticoList(diagnosticoCtx.getResposta());
		em.detach(result.getPublicoAlvo());

		// recuperar dados das propriedades rurais vinculadas
		for (ProjetoCreditoRuralPublicoAlvoPropriedadeRural prop : result.getPublicoAlvoPropriedadeRuralList()) {
			prop.setPublicoAlvoPropriedadeRural(publicoAlvoPropriedadeRuralDao.findOne(prop.getPublicoAlvoPropriedadeRural().getId()));
			if (!prop.getPublicoAlvoPropriedadeRural().getPublicoAlvo().getId().equals(result.getPublicoAlvo().getId())) {
				throw new BoException("Informado Propriedade Rural que não está vinculada ao Beneficiário, verifique!");
			}
			// captar diagnosticos
			filtro = new FormularioColetaCadFiltroDto();
			destinoList = new HashSet<>();
			destinoList.add(FormularioDestino.PR);
			filtro.setDestino(destinoList);
			filtro.setSubformulario(subFormularioList);
			filtro.setSituacao(situacaoList);			
			filtro.setPropriedadeRural(prop.getPublicoAlvoPropriedadeRural().getPropriedadeRural());
			diagnosticoCtx = facadeBo.formularioColetaFiltroExecutar(contexto.getUsuario(), filtro);
			prop.getPublicoAlvoPropriedadeRural().getPropriedadeRural().setDiagnosticoList(diagnosticoCtx.getResposta());
			em.detach(prop.getPublicoAlvoPropriedadeRural());
		}

		// calcular os itens do fluxo de caixa
		List<ProjetoCreditoRuralFluxoCaixa> fluxoCaixaList = new ArrayList<>();
		int id = 0;
		for (FluxoCaixaCodigo codigo : FluxoCaixaCodigo.values()) {
			ProjetoCreditoRuralFluxoCaixa pcrfc = codigo.calcular(result);
			// caso não exista, criar um novo ID
			if (pcrfc.getId() == null) {
				pcrfc.setId(--id);
			}
			fluxoCaixaList.add(pcrfc);
		}
		fluxoCaixaList.sort((fc1, fc2) -> fc1.getOrdem().compareTo(fc2.getOrdem()));

		result.setFluxoCaixaList(fluxoCaixaList);

		contexto.setResposta(result);

		return false;
	}

	private void nomeLoteTemCronograma(List<Item> nomeLoteList, List<ProjetoCreditoRuralCronogramaPagamento> cronogramaList) {
		if (nomeLoteList != null) {
			nomeLoteList.forEach((v) -> {
				v.setCronograma(false);
				if (cronogramaList != null) {
					for (int i = 0; i < cronogramaList.size(); i++) {
						if (v.getNomeLote().equals(cronogramaList.get(i).getNomeLote()) && Confirmacao.S.equals(cronogramaList.get(i).getSelecionado())) {
							v.setCronograma(true);
							break;
						}
					}
				}
			});
		}
	};

	private List<Item> pegarNomeLote(List<ProjetoCreditoRuralFinanciamento> lista) throws BoException {
		List<Item> result = new ArrayList<>();
		boolean encontrou, loteNaoIdentificado = false;
		if (lista != null) {
			for (ProjetoCreditoRuralFinanciamento v : lista) {
				if (StringUtils.isBlank(v.getNomeLote())) {
					loteNaoIdentificado = true;
				}
				encontrou = false;
				for (int i = 0; i < result.size(); i++) {
					if (v.getNomeLote().equals(result.get(i).getNomeLote())) {
						encontrou = true;
						break;
					}
				}
				if (!encontrou) {
					result.add(new Item(v.getNomeLote()));
				}
			}
		}
		if (loteNaoIdentificado) {
			throw new BoException("Há lotes não identificados");
		}
		return result;
	}

}
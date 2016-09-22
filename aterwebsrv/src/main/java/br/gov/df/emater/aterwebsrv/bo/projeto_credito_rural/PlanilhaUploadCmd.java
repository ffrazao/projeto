package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesaApoioDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioExcel;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

@Service("ProjetoCreditoRuralPlanilhaUploadCmd")
public class PlanilhaUploadCmd extends _Comando {

	@Autowired
	private ArquivoDao arquivoDao;

	public PlanilhaUploadCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRuralReceitaDespesaApoioDto result = (ProjetoCreditoRuralReceitaDespesaApoioDto) contexto.getRequisicao();

		Arquivo arquivo = arquivoDao.findByMd5(result.getArquivo().getMd5());

		List<List<List<Object>>> arqExcel = UtilitarioExcel.lerPlanilha(new ByteArrayInputStream(arquivo.getConteudo()));

		System.out.println(arqExcel);
		
		List<Double> custoList = new ArrayList<>();

		List<Double> receitaList = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			custoList.add((Double) ((List<Object>) ((List<List<Object>>) arqExcel.get(1)).get(44)).get(5 + (i * 2)));
					
			receitaList.add((Double) ((List<Object>) ((List<List<Object>>) arqExcel.get(1)).get(63)).get(5 + (i * 2)));
		}

		System.out.println();

		contexto.setResposta(result);

		return false;
	}

}
package br.gov.df.emater.aterwebsrv.importador.empregado;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioArquivo;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioExcel;

@Service
public class EmpregadoRelacaoExcelCarregarCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		File relacaoEmpregados = new File((File) contexto.get("tempDir"), "quadro_de_especialidades_da_emater-df_2016_11_22.xlsx");
		if (!relacaoEmpregados.exists()) {
			UtilitarioArquivo.downloadOrigem("http://extranet.emater.df.gov.br/index.php/component/phocadownload/category/1-institucional?download=1034:relacao-de-funcionarios-da-emater-df", relacaoEmpregados);
		}
		List<Map<String, Object>> mapa = UtilitarioExcel.criarMapaDoArquivoExcel(relacaoEmpregados, 0, 1);

		// ordernar pelo nome do empregado
		mapa.sort((m1, m2) -> m1.get("NOME").toString().compareTo(m2.get("NOME").toString()));

		contexto.put("RelacaoEmpregadosExcel", mapa);

		if (logger.isInfoEnabled()) {
			logger.info("Mapa de empregados carregado na memória");
		}

		return false;
	}

}
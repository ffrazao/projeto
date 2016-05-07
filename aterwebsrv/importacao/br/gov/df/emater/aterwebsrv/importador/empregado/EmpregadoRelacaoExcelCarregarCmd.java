package br.gov.df.emater.aterwebsrv.importador.empregado;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
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
		
		File tempDir = (File) contexto.get("tempDir");
		
		File relacaoEmpregados = new File(tempDir, "quadro_de_especialidades_da_emater-df_31_03_2016.xlsx");
		if (!relacaoEmpregados.exists()) {
			UtilitarioArquivo.downloadOrigem("http://extranet.emater.df.gov.br/index.php/component/phocadownload/category/1-institucional?download=1021:relacao-de-empregados", relacaoEmpregados);
		}
		List<Map<String, Object>> mapa = UtilitarioExcel.criarMapaDoArquivoExcel(relacaoEmpregados, 0, 1);
		
		Collections.sort(mapa, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				return m1.get("NOME").toString().compareTo(m2.get("NOME").toString());
			}
		});
		contexto.put("RelacaoEmpregadosExcel", mapa);

		return false;
	}

}
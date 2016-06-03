package br.gov.df.emater.aterwebsrv.importador.empregado;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioExcel;

@Service
public class EmpregadoDemitidosExcelCarregarCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		File tempDir = (File) contexto.get("tempDir");

		File demitidoEmpregadoExcel = new File(tempDir, "demitidos.xlsx");
		if (!demitidoEmpregadoExcel.exists()) {
			throw new BoException("Não foi possivel encontrar o arquivo dos demitidos");
		}
		List<Map<String, Object>> mapa = UtilitarioExcel.criarMapaDoArquivoExcel(demitidoEmpregadoExcel, 0, 0);

		Collections.sort(mapa, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> m1, Map<String, Object> m2) {
				return m1.get("MATRICULA").toString().compareTo(m2.get("MATRICULA").toString());
			}
		});
		contexto.put("DemitidoEmpregadoExcel", mapa);

		if (logger.isInfoEnabled()) {
			logger.info("Mapa dos demitidos da Emater-DF carregados");
		}

		return false;
	}

}
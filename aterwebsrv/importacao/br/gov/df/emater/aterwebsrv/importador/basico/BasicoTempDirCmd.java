package br.gov.df.emater.aterwebsrv.importador.basico;

import java.io.File;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service
public class BasicoTempDirCmd extends _Comando {

	public static final File TEMP_DIR = new File(BasicoTempDirCmd.class.getResource("/").getPath() + "/../temp");

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		File result = TEMP_DIR;
		if (!result.exists()) {
			result.mkdir();
		} else if (!result.isDirectory()) {
			throw new RuntimeException("Diretório temporario invalido!");
		}

		contexto.put("tempDir", result);
		
		if (logger.isInfoEnabled()) {
			logger.info("Diretório temporário verificado");
		}

		return false;
	}

}
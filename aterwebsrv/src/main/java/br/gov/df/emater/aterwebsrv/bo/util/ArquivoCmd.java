package br.gov.df.emater.aterwebsrv.bo.util;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;

@Service("UtilArquivoCmd")
public class ArquivoCmd extends _Comando {

	private static final String DIRETORIO_PERFIL = File.separator + "resources" + File.separator + "perfil";

	private static final String DIRETORIO_UPLOAD = File.separator + "resources" + File.separator + "upload";

	@Override
	@SuppressWarnings({ "unchecked" })
	public boolean executar(_Contexto context) throws Exception {
		
		Map<String, Object> requisicao = (Map<String, Object>) context.getRequisicao();
		
		MultipartFile file = (MultipartFile) requisicao.get("arquivo");
		HttpServletRequest request = (HttpServletRequest) requisicao.get("request");
		String tipo = (String) requisicao.get("tipo");

		String result = null;
		String extensao = null;
		String diretorio = null;

		if ("perfil".equals(tipo)) {
			diretorio = DIRETORIO_PERFIL;
		} else if ("arquivos".equals(tipo)) {
			diretorio = DIRETORIO_UPLOAD;
		}
		File uploadDiretorio = new File(request.getServletContext().getRealPath("/") + diretorio);

		if (!uploadDiretorio.exists()) {
			uploadDiretorio.mkdirs();
		} else if (!uploadDiretorio.isDirectory()) {
			throw new BoException("Configuração do diretorio de destino inválida!");
		}

		if (file.isEmpty()) {
			throw new BoException("Arquivo vazio!");
		}

		if (file.getOriginalFilename() != null && (!"undefined".equals(file.getOriginalFilename()))) {
			extensao = FilenameUtils.getExtension(file.getOriginalFilename());
			if (StringUtils.isEmpty(extensao)) {
				extensao = ".".concat(extensao);
			}
		}
		if ((extensao == null || extensao.trim().length() == 0) && file.getContentType().equals("image/png")) {
			extensao = ".png";
		}
		result = Criptografia.MD5_FILE(file.getBytes()) + extensao;
		File arquivo = new File(uploadDiretorio.getPath() + File.separator + result);
		file.transferTo(arquivo);

		context.setResposta(diretorio + File.separator + result);

		return false;
	}

}
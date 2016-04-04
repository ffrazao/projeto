package br.gov.df.emater.aterwebsrv.bo.util;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.modelo.dto.ArquivoDto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

@Service("UtilArquivoCmd")
public class ArquivoCmd extends _Comando {

	private static final String DIRETORIO_PERFIL = File.separator.concat("resources").concat(File.separator).concat("perfil");

	private static final String DIRETORIO_UPLOAD = File.separator.concat("resources").concat(File.separator).concat("upload");

	@Autowired
	private ArquivoDao arquivoDao;

	@Override
	@SuppressWarnings({ "unchecked" })
	public boolean executar(_Contexto context) throws Exception {
		// recuperar parametros
		Map<String, Object> requisicao = (Map<String, Object>) context.getRequisicao();
		MultipartFile file = (MultipartFile) requisicao.get("arquivo");
		HttpServletRequest request = (HttpServletRequest) requisicao.get("request");
		String tipo = (String) requisicao.get("tipo");

		String md5 = null;
		String extensao = null;
		String diretorio = null;
		String nome = null;

		if ("perfil".equals(tipo)) {
			diretorio = DIRETORIO_PERFIL;
		} else if ("arquivos".equals(tipo)) {
			diretorio = DIRETORIO_UPLOAD;
		}
		File uploadDiretorio = new File(request.getServletContext().getRealPath("/").concat(diretorio));

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
			if (!StringUtils.isEmpty(extensao)) {
				extensao = ".".concat(extensao);
			}
		}
		if ((extensao == null || extensao.trim().length() == 0) && file.getContentType().equals("image/png")) {
			extensao = ".png";
		}
		md5 = Criptografia.MD5_FILE(file.getBytes());
		nome = md5.concat(extensao);
		File fileName = new File(uploadDiretorio.getPath().concat(File.separator).concat(nome));

		// gravar no banco de dados
		Arquivo arquivo = arquivoDao.findByMd5(md5);
		if (arquivo == null) {
			arquivo = new Arquivo();
		}
		arquivo.setDataUpload(Calendar.getInstance());
		arquivo.setExtensao(extensao);
		arquivo.setMd5(md5);
		arquivo.setNomeOriginal(file.getOriginalFilename());
		arquivo.setTamanho(file.getBytes().length);
		arquivo.setTipo(tipo);
		arquivo.setLocalDiretorioWeb(diretorio.concat(File.separator).concat(nome));
		arquivo.setConteudo(file.getBytes());
		arquivoDao.save(arquivo);

		// gravar no diretorio de upload
		file.transferTo(fileName);

		context.setResposta(new ArquivoDto(arquivo.getLocalDiretorioWeb(), arquivo.getMd5(), arquivo.getExtensao()));

		return false;
	}

}
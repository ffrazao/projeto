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
import br.gov.df.emater.aterwebsrv.dto.pessoa.ArquivoDto;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

@Service("UtilArquivoSubirCmd")
public class ArquivoSubirCmd extends _Comando implements ArquivoConstantes {

	@Autowired
	private ArquivoDao arquivoDao;

	@Override
	@SuppressWarnings({ "unchecked" })
	public boolean executar(_Contexto context) throws Exception {
		// recuperar parametros
		Map<String, Object> requisicao = (Map<String, Object>) context.getRequisicao();
		MultipartFile arquivoEnviado = (MultipartFile) requisicao.get("arquivo");
		HttpServletRequest request = (HttpServletRequest) requisicao.get("request");
		ArquivoTipo tipo = (ArquivoTipo) requisicao.get("tipo");

		String md5 = null;
		String extensao = null;
		String diretorio = null;
		String nomeMd5Extensao = null;
		try {

			if (ArquivoTipo.P.equals(tipo)) {
				diretorio = DIRETORIO_PERFIL;
			} else if (ArquivoTipo.A.equals(tipo)) {
				diretorio = DIRETORIO_UPLOAD;
			}
			File diretorioFisicoServidor = new File(request.getServletContext().getRealPath("/").concat(diretorio));

			if (arquivoEnviado.isEmpty()) {
				throw new BoException(String.format("O conteúdo do arquivo %s está vazio!", arquivoEnviado.getOriginalFilename()));
			}

			if (arquivoEnviado.getSize() > 10240000) {
				throw new BoException(String.format("O conteúdo do arquivo %s excede o tamanho máximo de 10 mega bytes permitido!", arquivoEnviado.getOriginalFilename()));
			}

			if (!diretorioFisicoServidor.exists()) {
				diretorioFisicoServidor.mkdirs();
			} else if (!diretorioFisicoServidor.isDirectory()) {
				throw new BoException("Configuração do diretorio de destino é inválida!");
			}

			if (arquivoEnviado.getOriginalFilename() != null && (!"undefined".equals(arquivoEnviado.getOriginalFilename()))) {
				extensao = FilenameUtils.getExtension(arquivoEnviado.getOriginalFilename());
				if (!StringUtils.isEmpty(extensao)) {
					extensao = ".".concat(extensao);
				}
			} else {
				throw new BoException("Não foi possível identificar o nome do arquivo enviado!");
			}
			if ((extensao == null || extensao.trim().length() == 0) && arquivoEnviado.getContentType().equals("image/png")) {
				extensao = ".png";
			}
			md5 = Criptografia.MD5_FILE(arquivoEnviado.getBytes());
			nomeMd5Extensao = md5.concat(extensao);
			File arquivoGravado = new File(diretorioFisicoServidor.getPath().concat(File.separator).concat(nomeMd5Extensao));

			// gravar no banco de dados
			Arquivo arquivo = arquivoDao.findByMd5(md5);
			if (arquivo == null) {
				arquivo = new Arquivo();
			}
			arquivo.setDataUpload(Calendar.getInstance());
			arquivo.setExtensao(extensao);
			arquivo.setMd5(md5);
			arquivo.setNomeOriginal(arquivoEnviado.getOriginalFilename());
			arquivo.setTamanho(arquivoEnviado.getBytes().length);
			arquivo.setMimeTipo(arquivoEnviado.getContentType());
			arquivo.setTipo(tipo);
			arquivo.setLocalDiretorioWeb(diretorio.concat(File.separator).concat(nomeMd5Extensao));
			arquivo.setConteudo(arquivoEnviado.getBytes());
			arquivoDao.save(arquivo);

			// gravar no diretorio de upload
			arquivoEnviado.transferTo(arquivoGravado);

			context.setResposta(new ArquivoDto(arquivo.getLocalDiretorioWeb(), arquivo.getMd5(), arquivo.getExtensao()));

			return false;
		} catch (Exception e) {
			throw new BoException(e);
		}
	}

}
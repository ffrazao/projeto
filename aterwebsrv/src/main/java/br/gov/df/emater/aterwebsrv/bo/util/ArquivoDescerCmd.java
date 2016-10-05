package br.gov.df.emater.aterwebsrv.bo.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import eu.medsea.mimeutil.MimeUtil;

@Service("UtilArquivoDescerCmd")
public class ArquivoDescerCmd extends _Comando implements ArquivoConstantes {

	class ArquivoConteudo {
		File arquivo;
		byte[] conteudo;
	}

	@Autowired
	private ArquivoDao arquivoDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto context) throws Exception {
		// recuperar parametros
		Map<String, Object> requisicao = (Map<String, Object>) context.getRequisicao();
		String nomeArquivo = (String) requisicao.get("arquivo");
		HttpServletRequest request = (HttpServletRequest) requisicao.get("request");
		HttpServletResponse response = (HttpServletResponse) requisicao.get("response");
		try {
			ArquivoConteudo conteudo = null;
			conteudo = getDiretorio(request.getServletContext().getRealPath("/").concat(DIRETORIO_PERFIL), nomeArquivo);
			if (conteudo == null) {
				conteudo = getDiretorio(request.getServletContext().getRealPath("/").concat(DIRETORIO_UPLOAD), nomeArquivo);
			}
			if (conteudo == null) {
				Arquivo arquivoBanco = arquivoDao.findByMd5(nomeArquivo);
				if (arquivoBanco == null) {
					throw new FileNotFoundException(String.format("O arquivo [%s] não foi encontrado", nomeArquivo));
				} else {
					String diretorio = null;
					if (ArquivoTipo.P.equals(arquivoBanco.getTipo())) {
						diretorio = DIRETORIO_PERFIL;
					} else if (ArquivoTipo.A.equals(arquivoBanco.getTipo())) {
						diretorio = DIRETORIO_UPLOAD;
					}
					File diretorioServidor = new File(request.getServletContext().getRealPath("/").concat(diretorio));
					if (!diretorioServidor.exists()) {
						logger.debug(String.format("Diretório de arquivos não encontrado, criando um novo [%s]", diretorioServidor.toString()));
						diretorioServidor.mkdirs();
					}
					File arquivoDescer = new File(diretorioServidor, String.format("%s%s", arquivoBanco.getMd5(), arquivoBanco.getExtensao()));

					// recuperar o arquivo no system file
					if (!arquivoDescer.exists()) {
						logger.debug(String.format("O arquivo [%s] não encontrado no servidor de arquivos, inicio do download do banco de dados", nomeArquivo));
						if (arquivoBanco.getConteudo() == null || arquivoBanco.getConteudo().length == 0) {
							throw new FileNotFoundException(String.format("Nao foi possivel recuperar o arquivo [%s]", arquivoBanco.getMd5()));
						}
						// recuperar o conteúdo do arquivo caso tenha sido
						// apagado
						try (OutputStream out = new FileOutputStream(arquivoDescer)) {
							out.write(arquivoBanco.getConteudo());
							out.flush();
							logger.debug(String.format("O arquivo [%s] salvo em [%s]", nomeArquivo, arquivoDescer.getAbsolutePath()));
						}
					}
					conteudo = new ArquivoConteudo();
					conteudo.arquivo = arquivoDescer;
					conteudo.conteudo = arquivoBanco.getConteudo();
				}
			}
			// identificar mimetype
			response.setHeader("Content-Disposition", String.format("inline; filename=%s", conteudo.arquivo.getName()));
			response.setContentType(MimeUtil.getMimeTypes(conteudo.conteudo).toString());
			response.setContentLength(conteudo.conteudo.length);
			response.getOutputStream().write(conteudo.conteudo);
			response.flushBuffer();

		} catch (Exception e) {
			new BoException(e);
		}
		return false;
	}

	private ArquivoConteudo getDiretorio(String diretorioNome, String arquivoBusca) throws Exception {
		ArquivoConteudo result = null;
		File diretorio = new File(diretorioNome);
		if (diretorio.exists()) {
			FileFilter fileFilter = new RegexFileFilter(arquivoBusca.concat(".*"));
			for (File arquivo : diretorio.listFiles(fileFilter)) {
				if (arquivo.exists()) {
					result = new ArquivoConteudo();
					result.arquivo = arquivo;
					result.conteudo = new byte[(int) arquivo.length()];
					try (InputStream in = new FileInputStream(arquivo)) {
						in.read(result.conteudo);
					}
					return result;
				}
			}
		}
		return result;
	}

}
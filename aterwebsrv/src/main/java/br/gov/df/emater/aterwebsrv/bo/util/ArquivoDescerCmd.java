package br.gov.df.emater.aterwebsrv.bo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

@Service("UtilArquivoDescerCmd")
public class ArquivoDescerCmd extends _Comando implements ArquivoConstantes {

	@Autowired
	private ArquivoDao arquivoDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto context) throws Exception {

		// recuperar parametros
		Map<String, Object> requisicao = (Map<String, Object>) context.getRequisicao();
		String file = (String) requisicao.get("arquivo");
		HttpServletRequest request = (HttpServletRequest) requisicao.get("request");
		HttpServletResponse response = (HttpServletResponse) requisicao.get("response");

		String diretorio = null;

		try {
			Arquivo arquivoBanco = arquivoDao.findByMd5(file);
			if (arquivoBanco == null) {
				throw new FileNotFoundException(String.format("O arquivo [%s] não foi encontrado", file));
			} else {

				if (ArquivoTipo.P.equals(arquivoBanco.getTipo())) {
					diretorio = DIRETORIO_PERFIL;
				} else if (ArquivoTipo.A.equals(arquivoBanco.getTipo())) {
					diretorio = DIRETORIO_UPLOAD;
				}
				File diretorioServidor = new File(request.getServletContext().getRealPath("/").concat(diretorio));

				File arquivoDescer = new File(diretorioServidor, String.format("%s%s", arquivoBanco.getMd5(), arquivoBanco.getExtensao()));

				// recuperar o arquivo no system file
				if (!arquivoDescer.exists()) {
					if (arquivoBanco.getConteudo() == null || arquivoBanco.getConteudo().length == 0) {
						throw new FileNotFoundException(String.format("Nao foi possivel recuperar o arquivo [%s]", arquivoBanco.getMd5()));
					}

					// recuperar o conteúdo do arquivo caso tenha sido apagado
					try (OutputStream out = new FileOutputStream(arquivoDescer)) {
						out.write(arquivoBanco.getConteudo());
						out.flush();
					}
				}

				// ler o conteudo do arquivo
				byte[] bytes = new byte[(int) arquivoDescer.length()];
				try (InputStream in = new FileInputStream(arquivoDescer)) {
					in.read(bytes);
				}

				// Preparar a resposta
				// response.setHeader("Content-Disposition",
				// String.format("attachment; filename=%s",
				// arquivo.getNomeOriginal()));
				response.setHeader("Content-Disposition", String.format("inline; filename=%s", arquivoBanco.getNomeOriginal()));
				response.setContentType(arquivoBanco.getMimeTipo());
				response.setContentLength(arquivoBanco.getTamanho());
				response.getOutputStream().write(bytes);
				response.flushBuffer();

				arquivoBanco = null;
			}

			return false;
		} catch (Exception e) {
			new BoException(e);
		}
		return false;
	}
}
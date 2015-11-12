package br.gov.df.emater.aterwebsrv.rest;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;

@RestController
public class UtilRest {

	private static final String DIRETORIO_PERFIL = File.separator + "resources" + File.separator + "perfil";
	
	private static final String DIRETORIO_UPLOAD = File.separator + "resources" + File.separator + "upload";
	
	@Autowired
	private FacadeBo facadeBo;

	@RequestMapping(value = "dominio", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta getDominio(@RequestParam(required = true) String[] ent, @RequestParam(required = false) String npk, @RequestParam(required = false) String vpk, @RequestParam(required = false) String order, @RequestParam(required = false) String[] fetchs, Principal usuario)
			throws Exception {
		try {
			return new Resposta(facadeBo.dominio(usuario, ent, npk, vpk, order, fetchs).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}
	
	@ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "perfil")
    public Resposta perfil(@RequestParam("file") MultipartFile file, HttpServletRequest request, Principal usuario) throws IOException {
        byte[] bytes;
        String result = null;
        String extensao = null;
        
        File uploadDiretorio = new File(request.getServletContext().getRealPath("/") + DIRETORIO_PERFIL);
        
        if (!uploadDiretorio.exists()) {
        	uploadDiretorio.mkdirs();
        } else if (!uploadDiretorio.isDirectory()) {
        	throw new BoException("Configuração do diretorio de destino inválida!");
        }

        if (!file.isEmpty()) {
            bytes = file.getBytes();
            if (file.getOriginalFilename() != null) {
            	extensao = "." + FilenameUtils.getExtension(file.getOriginalFilename());
            }
            if ((extensao == null || extensao.trim().length() == 0) && file.getContentType().equals("image/png")) {
            	extensao = ".png";
            }
            result = Criptografia.MD5_FILE(bytes) + extensao;
            File arquivo = new File(uploadDiretorio.getPath() + File.separator  + result);
            file.transferTo(arquivo);
        }

        return new Resposta(DIRETORIO_PERFIL + File.separator + result);
    }
	
	
	@ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "arquivos")
    public Resposta arquivos(@RequestParam("file") MultipartFile file, HttpServletRequest request, Principal usuario) throws IOException {
        byte[] bytes;
        String result = null;
        String extensao = null;
        
        File uploadDiretorio = new File(request.getServletContext().getRealPath("/") + DIRETORIO_UPLOAD);
        
        if (!uploadDiretorio.exists()) {
        	uploadDiretorio.mkdirs();
        } else if (!uploadDiretorio.isDirectory()) {
        	throw new BoException("Configuração do diretorio de destino inválida!");
        }

        if (!file.isEmpty()) {
            bytes = file.getBytes();
            if (file.getOriginalFilename() != null) {
            	extensao = "." + FilenameUtils.getExtension(file.getOriginalFilename());
            }
            if ((extensao == null || extensao.trim().length() == 0) && file.getContentType().equals("image/png")) {
            	extensao = ".png";
            }
            result = Criptografia.MD5_FILE(bytes) + extensao;
            File arquivo = new File(uploadDiretorio.getPath() + File.separator  + result);
            file.transferTo(arquivo);
        }

        return new Resposta(DIRETORIO_UPLOAD + File.separator + result);
    }

}

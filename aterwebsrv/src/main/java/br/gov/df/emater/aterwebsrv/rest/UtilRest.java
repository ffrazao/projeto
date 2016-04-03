package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

@RestController
public class UtilRest {

	@Autowired
	private FacadeBo facadeBo;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "arquivos")
	public Resposta arquivos(@RequestParam("file") MultipartFile file, HttpServletRequest request, Principal usuario) throws Exception {
		return new Resposta(facadeBo.utilArquivo(usuario, file, request, "arquivos").getResposta());
	}

	@RequestMapping(value = "dominio", method = RequestMethod.GET)
	public Resposta getDominio(@RequestParam(required = true) String[] ent, @RequestParam(required = false) String npk, @RequestParam(required = false) String vpk, @RequestParam(required = false) String order, @RequestParam(required = false) String[] fetchs, Principal usuario)
			throws Exception {
		return new Resposta(facadeBo.dominio(usuario, ent, npk, vpk, order, fetchs).getResposta());
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "perfil")
	public Resposta perfil(@RequestParam("file") MultipartFile file, HttpServletRequest request, Principal usuario) throws Exception {
		return new Resposta(facadeBo.utilArquivo(usuario, file, request, "perfil").getResposta());
	}

}
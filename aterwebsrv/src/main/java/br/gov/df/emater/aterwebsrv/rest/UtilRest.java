package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;

@RestController
public class UtilRest {

	@Autowired
	private FacadeBo facadeBo;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "arquivo-subir", method = RequestMethod.POST)
	public Resposta arquivoSubir(@RequestParam("file") MultipartFile arquivo, HttpServletRequest request, Principal usuario) throws Exception {
		return new Resposta(facadeBo.utilArquivoSubir(usuario, arquivo, request, ArquivoTipo.A).getResposta());
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "arquivo-descer", method = RequestMethod.GET)
	public void descer(@RequestParam String arquivo, HttpServletRequest request, HttpServletResponse response, Principal usuario) throws Exception {
		facadeBo.utilArquivoDescer(usuario, arquivo, request, response);
	}

	@RequestMapping(value = "dominio", method = RequestMethod.GET)
	public Resposta getDominio(@RequestParam(required = true) String[] ent, @RequestParam(required = false) String npk, @RequestParam(required = false) String vpk, @RequestParam(required = false) String order, @RequestParam(required = false) String[] fetchs, @RequestParam(required = false) String nomeEnum, Principal usuario)
			throws Exception {
		return new Resposta(facadeBo.dominio(usuario, ent, npk, vpk, order, fetchs, nomeEnum).getResposta());
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "perfil")
	public Resposta perfil(@RequestParam("file") MultipartFile file, HttpServletRequest request, Principal usuario) throws Exception {
		return new Resposta(facadeBo.utilArquivoSubir(usuario, file, request, ArquivoTipo.P).getResposta());
	}

}
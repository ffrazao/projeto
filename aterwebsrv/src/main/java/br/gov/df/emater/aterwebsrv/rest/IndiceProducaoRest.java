package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoCadDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@RestController
@RequestMapping("/indice-producao")
public class IndiceProducaoRest {

	@Autowired
	private FacadeBo facadeBo;

	public IndiceProducaoRest() {
	}

	
//	@RequestMapping(value = "/producao", method = RequestMethod.POST)
//	public Resposta producao(@RequestBody ProducaoProprietario producaoProprietario, Principal usuario) throws Exception {
//		return new Resposta(facadeBo.indiceProducaoProducao(usuario, producaoProprietario).getResposta());
//	}
	
	//Teste
	@RequestMapping(value = "/producao", method = RequestMethod.POST)
	public Resposta producao(@RequestBody Ipa ipa, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoProducao(usuario, ipa).getResposta());
	}

	@RequestMapping(value = "/bem-classificacao-matriz", method = RequestMethod.GET)
	public Resposta bemClassificacaoMatriz(Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoBemClassificacaoMatriz(usuario).getResposta());
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody IndiceProducaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/filtro-producao-propriedade-rural", method = RequestMethod.POST)
	public Resposta filtroProducaoPropriedadeRural(@RequestBody IndiceProducaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroProducaoPropriedadeRural(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/filtro-producao-publico-alvo", method = RequestMethod.POST)
	public Resposta filtroProducaoPublicoAlvo(@RequestBody IndiceProducaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroProducaoPublicoAlvo(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return editarIpa(producaoProprietario, usuario);
	}
	
	@RequestMapping(value = "/editar-flor", method = RequestMethod.POST)
	public Resposta editarFlor(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return editarIpaFlor(producaoProprietario, usuario);
	}
	
	@RequestMapping(value = "/editar-animal", method = RequestMethod.POST)
	public Resposta editarAnimal(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		System.out.println("Chegou no java.");
		return editarIpaAnimal(producaoProprietario, usuario);
	}
	
	@RequestMapping(value = "/editar-arte", method = RequestMethod.POST)
	public Resposta editarArte(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return editarIpaArte(producaoProprietario, usuario);
	}
	
	@RequestMapping(value = "/editar-agro", method = RequestMethod.POST)
	public Resposta editarAgro(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return editarIpaAgro(producaoProprietario, usuario);
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		return salvar(producaoProprietario, usuario);
	}
	//Floricultura
	@RequestMapping(value = "/incluir-flor", method = RequestMethod.POST)
	public Resposta incluirFlor(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		return salvarFlor(producaoProprietario, usuario);
	}
	
	//Arte
		@RequestMapping(value = "/incluir-arte", method = RequestMethod.POST)
		public Resposta incluirArte(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
			return salvarArte(producaoProprietario, usuario);
		}
	//Agro
		@RequestMapping(value = "/incluir-agro", method = RequestMethod.POST)
		public Resposta incluirAgro(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
			return salvarAgro(producaoProprietario, usuario);
		}		

	//Animal
		@RequestMapping(value = "/incluir-animal", method = RequestMethod.POST)
		public Resposta incluirAnimal(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
			return salvarAnimal(producaoProprietario, usuario);
		}

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Resposta novo(@RequestBody ProducaoProprietario producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoNovo(usuario, producaoProprietario).getResposta());
	}

	public Resposta salvar(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoSalvar(usuario, producaoProprietario).getResposta());
	}
	
	//Floricultura
	public Resposta salvarFlor(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoSalvarFlor(usuario, producaoProprietario).getResposta());
	}
	
//Arte
	public Resposta salvarArte(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		System.out.println("salvar arte");
		return new Resposta(facadeBo.indiceProducaoSalvarArte(usuario, producaoProprietario).getResposta());
	}
//Agro
	public Resposta salvarAgro(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		System.out.println("salvar agro");
		return new Resposta(facadeBo.indiceProducaoSalvarAgro(usuario, producaoProprietario).getResposta());
	}
//Animal
	public Resposta salvarAnimal(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		System.out.println("salvar animal");
		return new Resposta(facadeBo.indiceProducaoSalvarAnimal(usuario, producaoProprietario).getResposta());
	}
	
	//Editar
	public Resposta editarIpa(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoEditar(usuario, producaoProprietario).getResposta());
	}
	
	public Resposta editarIpaFlor(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoEditarFlor(usuario, producaoProprietario).getResposta());
	}
	
	public Resposta editarIpaArte(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoEditarArte(usuario, producaoProprietario).getResposta());
	}
	
	public Resposta editarIpaAnimal(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoEditarAnimal(usuario, producaoProprietario).getResposta());
	}
	
	public Resposta editarIpaAgro(@RequestBody ProducaoCadDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoEditarAgro(usuario, producaoProprietario).getResposta());
	}
	
	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoVisualizar(usuario, id).getResposta());
	}

}
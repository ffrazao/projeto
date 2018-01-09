package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dto.pessoa.DeclaracaoCeasaRelFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.pessoa.DeclaracaoProdutorRelFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;

@Service("PessoaDeclaracaoCeasaRelCmd")
public class DeclaracaoCeasaRelCmd extends _Comando {

	@Autowired
	private PublicoAlvoPropriedadeRuralDao dao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private _Relatorio relatorio;
	
	
	int cont;
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		DeclaracaoCeasaRelFiltroDto filtro = (DeclaracaoCeasaRelFiltroDto) contexto.getRequisicao();
		 cont = 1;
		filtro.getProducaoList().forEach( (x) -> {
			x.setId(cont);
			cont++;
		});
		
		List<PublicoAlvoPropriedadeRural> lista = null;
		lista = (List<PublicoAlvoPropriedadeRural>) dao.findAll(filtro.getPublicoAlvoPropriedadeRuralIdList());
		String principalAtividadeProdutiva = "";
		String tipoPessoa = "";
		

		Calendar emissao = Calendar.getInstance();
		Calendar expiracao = Calendar.getInstance();
		expiracao.add(Calendar.YEAR, 2);

		Calendar fimCarencia = Calendar.getInstance();
		fimCarencia.add(Calendar.MONTH, 2);

		for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : lista) {
			
			if (publicoAlvoPropriedadeRural.getPublicoAlvo() != null && !CollectionUtils.isEmpty(publicoAlvoPropriedadeRural.getPublicoAlvo().getPublicoAlvoSetorList())) {
				for (PublicoAlvoSetor pas : publicoAlvoPropriedadeRural.getPublicoAlvo().getPublicoAlvoSetorList()) {
					principalAtividadeProdutiva += pas.getSetor().getNome()+", ";
					publicoAlvoPropriedadeRural.getPropriedadeRural().setPrincipaisAtividadesProdutivas(principalAtividadeProdutiva);
				}
				tipoPessoa = publicoAlvoPropriedadeRural.getPublicoAlvo().getPessoa().getPessoaTipo().toString();
			}	
		}
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("Usuario", getUsuario(contexto.getUsuario().getName()));
		parametros.put("RelatorioNome", "DECLARAÇÃO DE ATIVIDADE RURAL");
		parametros.put("Producao", filtro.getProducaoList());
		parametros.put("TipoPessoa", tipoPessoa);

		byte[] result = relatorio.imprimir("pessoa/DeclaracaoCeasaRel", parametros, lista);

		contexto.setResposta(result);

		return false;
	}

}

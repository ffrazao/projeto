package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dto.CarteiraProdutorRelFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;

@Service("PessoaCarteiraProdutorRelCmd")
public class CarteiraProdutorRelCmd extends _Comando {

	@Autowired
	private PublicoAlvoPropriedadeRuralDao dao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private _Relatorio relatorio;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		CarteiraProdutorRelFiltroDto filtro = (CarteiraProdutorRelFiltroDto) contexto.getRequisicao();
		List<PublicoAlvoPropriedadeRural> lista = null;
		lista = dao.findAll(filtro.getPublicoAlvoPropriedadeRuralIdList());

		Calendar emissao = Calendar.getInstance();
		Calendar expiracao = Calendar.getInstance();
		expiracao.add(Calendar.YEAR, 2);

		Calendar fimCarencia = Calendar.getInstance();
		fimCarencia.add(Calendar.MONTH, 2);

		for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : lista) {
			Calendar carteiraExpiracao = publicoAlvoPropriedadeRural.getPublicoAlvo().getCarteiraProdutorExpiracao();
			if (carteiraExpiracao == null || carteiraExpiracao.before(fimCarencia)) {
				publicoAlvoPropriedadeRural.getPublicoAlvo().setCarteiraProdutorEmissao(emissao);
				publicoAlvoPropriedadeRural.getPublicoAlvo().setCarteiraProdutorExpiracao(expiracao);
				publicoAlvoDao.save(publicoAlvoPropriedadeRural.getPublicoAlvo());
			}
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("Usuario", getUsuario(contexto.getUsuario().getName()));
		parametros.put("RelatorioNome", "CARTÃO DO PRODUTOR RURAL");

		byte[] result = relatorio.imprimir("pessoa/CarteiraProdutorRel", parametros, lista);

		contexto.setResposta(result);

		return false;
	}

}

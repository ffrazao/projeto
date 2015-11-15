package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;

@Service("PropriedadeRuralSalvarCmd")
public class SalvarCmd extends _Comando {

	public SalvarCmd() {
	}

	@Autowired
	private PropriedadeRuralDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PropriedadeRural propriedadeRural = (PropriedadeRural) contexto.getRequisicao();
		if (propriedadeRural.getId() == null) {
			propriedadeRural.setUsuarioInclusao(getUsuario(contexto.getUsuario().getName()));
			propriedadeRural.setInclusaoData(Calendar.getInstance());
		} else {
			propriedadeRural.setUsuarioInclusao(getUsuario(propriedadeRural.getUsuarioInclusao().getUsername()));
		}
		propriedadeRural.setUsuarioAlteracao(getUsuario(contexto.getUsuario().getName()));
		propriedadeRural.setAlteracaoData(Calendar.getInstance());

		dao.save(propriedadeRural);

		dao.flush();

		contexto.setResposta(propriedadeRural.getId());
		return true;
	}

}
package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EnderecoDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.BaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;

@Service("PropriedadeRuralSalvarCmd")
public class SalvarCmd extends _Comando {

	public SalvarCmd() {
	}

	@Autowired
	private PropriedadeRuralDao dao;
	
	@Autowired
	private PublicoAlvoPropriedadeRuralDao publicoAlvoPropriedadeRuralDao;
	
	@Autowired
	private PublicoAlvoDao publicoAlvoDao;
	
	@Autowired
	private EnderecoDao enderecoDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PropriedadeRural result = (PropriedadeRural) contexto.getRequisicao();
		if (result.getId() == null) {
			result.setUsuarioInclusao(getUsuario(contexto.getUsuario().getName()));
			result.setInclusaoData(Calendar.getInstance());
		} else {
			result.setUsuarioInclusao(getUsuario(result.getUsuarioInclusao().getUsername()));
		}
		result.setUsuarioAlteracao(getUsuario(contexto.getUsuario().getName()));
		result.setAlteracaoData(Calendar.getInstance());

		if (result.getEndereco() == null) {
			throw new BoException("O campo Endereço é obrigatório");
		}
		Endereco endereco = result.getEndereco();
		endereco.setPropriedadeRuralConfirmacao(Confirmacao.S);
		enderecoDao.save(endereco);
		
		result.setBaciaHidrografica(new BaciaHidrografica(1));

		dao.save(result);
		
		for (PublicoAlvoPropriedadeRural papr: result.getPublicoAlvoPropriedadeRuralList()) {
			papr.setPropriedadeRural(result);
			papr.setPublicoAlvo(publicoAlvoDao.findOneByPessoa(papr.getPublicoAlvo().getPessoa()));
			publicoAlvoPropriedadeRuralDao.save(papr);
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return true;
	}

}
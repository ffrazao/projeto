package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaJuridicaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service("PessoaVerificaCNPJCmd")
public class VerificaCNPJCmd extends _Comando {

	@Autowired
	private PessoaJuridicaDao pessoaJuridicaDao;
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		String msg = "";
		String numero = (String) contexto.getRequisicao();


		if (Util.isCnpjValido(numero)){
			numero = UtilitarioString.formataCnpj(numero);
			
			List<PessoaJuridica> pessoaJuridicaList = pessoaJuridicaDao.findByCnpj(numero);
			if (pessoaJuridicaList != null) {
				for (PessoaJuridica pessoaJuridica : pessoaJuridicaList) {
					msg = "CNPJ já cadastrado para: " + pessoaJuridica.getNome();
				}
			} else {
				return true;
			}

		}
		
		
		contexto.setResposta(msg);
		
		return false;
	}

	


}

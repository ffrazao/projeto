package br.gov.df.emater.aterwebsrv.bo.seguranca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("SegurancaVisualizarPerfilCmd")
public class VisualizarPerfilCmd extends _Comando {

	@Autowired
	private UsuarioDao usuarioDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		String username = (String) contexto.getRequisicao();

		Usuario result = usuarioDao.findByUsername(username);

		if (result == null) {
			throw new BoException("Usuário não cadastrado");
		}
	
		if (result.getPessoaEmail() != null) {
			PessoaEmail pessoaEmail = new PessoaEmail();
			pessoaEmail.setId(result.getPessoaEmail().getId());
			
			Email email = new Email();
			email.setId(result.getPessoaEmail().getEmail().getId());
			email.setEndereco(result.getPessoaEmail().getEmail().getEndereco());
			
			pessoaEmail.setEmail(email);

			result.setPessoaEmail(pessoaEmail);
		}
		
		List<PessoaEmail> emailList = result.getPessoa().getEmailList();
		if (emailList != null) {
			for (PessoaEmail email:emailList) {
				email.setPessoa(null);
			}
		}
		result.setPessoa(result.getPessoa().infoBasica());
		result.getPessoa().setEmailList(emailList);
		
		result.setPassword(null);
		
		contexto.setResposta(result);

		return false;
	}

}
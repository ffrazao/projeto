package br.gov.df.emater.aterwebsrv.bo.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("SegurancaRenovarSenhaCmd")
public class RenovarSenhaCmd extends _Comando {
	
	@Autowired
	private UsuarioDao usuarioDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario usuario = (Usuario) contexto.getRequisicao();
		
		Usuario usuarioSalvo = usuarioDao.findByUsername(usuario.getUsername());
		
		if (usuarioSalvo == null) {
			throw new BoException("Usuário não cadastrado");
		}
		String senhaAtual = Criptografia.MD5(usuarioSalvo.getId() + usuario.getPassword());
		if (!senhaAtual.equals(usuarioSalvo.getPassword())) {
			throw new BoException("A senha atual é inválida!");
		}
		String novaSenha = Criptografia.MD5(usuarioSalvo.getId() + usuario.getNewPassword());
		if (novaSenha.equals(usuarioSalvo.getPassword())) {
			throw new BoException("A nova senha não pode ser a mesma que a atual");
		}
		
		usuarioSalvo.setUsuarioStatusConta(UsuarioStatusConta.A);
		usuarioSalvo.setExpires(null);
		usuarioSalvo.setPassword(novaSenha);
		usuarioDao.save(usuarioSalvo);
		
		return false;
	}

}
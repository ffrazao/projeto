package br.gov.df.emater.aterwebsrv.bo.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("SegurancaSalvarPerfilCmd")
public class SalvarPerfilCmd extends _Comando {
	
	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private ArquivoDao arquivoDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario usuario = (Usuario) contexto.getRequisicao();
		
		Usuario usuarioSalvo = usuarioDao.findOne(usuario.getId());
		
		if (usuarioSalvo == null) {
			throw new BoException("Usuário não cadastrado");
		}
		
		Usuario username = usuarioDao.findByUsername(usuario.getUsername());
		if (username != null && username.getId() != usuarioSalvo.getId()) {
			throw new BoException("Este nome de usuário já está em uso!");
		}
		
		// preparar a foto do perfil
		Arquivo arquivo = usuario.getPessoa().getPerfilArquivo();
		if (arquivo != null && arquivo.getMd5() != null) {
			arquivo = arquivoDao.findByMd5(arquivo.getMd5());
		} else {
			arquivo = null;
		}
		
		usuarioSalvo.getPessoa().setPerfilArquivo(arquivo);

		// salvar a foto do perfil
		pessoaDao.save(usuarioSalvo.getPessoa());
		
		// salvar demais dados do usuário
		usuarioSalvo.setUsername(usuario.getUsername());
		usuarioSalvo.setPessoaEmail(usuario.getPessoaEmail());
		usuarioSalvo.setInfoSobreUsuario(usuario.getInfoSobreUsuario());
		usuarioDao.save(usuarioSalvo);
		
		return false;
	}

}
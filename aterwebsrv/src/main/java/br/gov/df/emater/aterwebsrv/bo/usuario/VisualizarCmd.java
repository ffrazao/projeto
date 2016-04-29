package br.gov.df.emater.aterwebsrv.bo.usuario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.modelo.sistema.UsuarioPerfil;

@Service("UsuarioVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private UsuarioDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Usuario usuario = dao.findOne(id);

		if (usuario == null) {
			throw new BoException("Registro não localizado");
		}
		// fetch nas dependencias
		if (usuario.getPessoa() != null) {
			Pessoa pessoa = usuario.getPessoa().infoBasica();
			pessoa.setObservacoes(usuario.getPessoa().getObservacoes());
			List<PessoaEmail> pessoaEmailList = new ArrayList<PessoaEmail>();
			for (PessoaEmail pessoaEmail : usuario.getPessoa().getEmailList()) {
				pessoaEmailList.add(new PessoaEmail(pessoaEmail.getId(), pessoaEmail.getEmail().infoBasica()));
			}
			pessoa.setEmailList(pessoaEmailList);
			
			usuario.setPessoa(pessoa);
		} else {
			usuario.setUnidadeOrganizacional(usuario.getUnidadeOrganizacional().infoBasica());
		}
		if (usuario.getUsuarioInclusao() != null) {
			usuario.setUsuarioInclusao(usuario.getUsuarioInclusao().infoBasica());
		}
		if (usuario.getUsuarioAlteracao() != null) {
			usuario.setUsuarioAlteracao(usuario.getUsuarioAlteracao().infoBasica());
		}
		
		if (usuario.getPessoaEmail()!=null && usuario.getPessoaEmail().getEmail() != null) {
			usuario.getPessoaEmail().setEmail(usuario.getPessoaEmail().getEmail().infoBasica());
			usuario.getPessoaEmail().setPessoa(null);
		}
		
		if (usuario.getAuthorities() != null) {
			for (UsuarioPerfil usuarioPerfil: usuario.getAuthorities()) {
				usuarioPerfil.setUsuario(null);
			}
		}
		
		em.detach(usuario);

		contexto.setResposta(usuario);

		return false;
	}
}
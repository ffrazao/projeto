package br.gov.df.emater.aterwebsrv.seguranca;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoViDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.LotacaoViDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.EmpregoVi;
import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;
import br.gov.df.emater.aterwebsrv.modelo.sistema.PerfilFuncionalidadeComando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.modelo.sistema.UsuarioPerfil;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private PessoaDao pessoaDao;
	
	@Autowired
	private EmpregoViDao empregoViDao;
	
	@Autowired
	private LotacaoViDao lotacaoViDao;

	private void avaliarPerfil(Set<UsuarioPerfil> authorities, Set<UsuarioPerfil> authoritiesRetorno, Map<String, Set<String>> perfilFuncionalidadeComandoListRetorno, Map<String, Set<String>> perfilFuncionalidadeComandoListNegadoRetorno) {
		for (UsuarioPerfil usuarioPerfil : authorities) {
			// ignorar os perfis do usuario inativos
			if (Confirmacao.N.equals(usuarioPerfil.getAtivo()) || Confirmacao.N.equals(usuarioPerfil.getPerfil().getAtivo())) {
				continue;
			}

			// inicio avaliação das funcionalidades/comandos do perfil do
			// usuario
			for (PerfilFuncionalidadeComando fc : usuarioPerfil.getPerfil().getPerfilFuncionalidadeComandoList()) {
				// ignorar as funcionalidades inativas
				if (Confirmacao.N.equals(fc.getFuncionalidadeComando().getFuncionalidade().getAtivo())) {
					continue;
				}
				String funcionalidade = fc.getFuncionalidadeComando().getFuncionalidade().getCodigo();
				String comando = fc.getFuncionalidadeComando().getComando().getCodigo();

				if (Confirmacao.N.equals(fc.getConceder())) {
					// marcar item para futura remoção caso algum outro perfil
					// conceda esta funcionalidade/comando
					coletarElemento(funcionalidade, comando, perfilFuncionalidadeComandoListNegadoRetorno);
				} else {
					coletarElemento(funcionalidade, comando, perfilFuncionalidadeComandoListRetorno);
				}
			}

			Perfil perfilRetorno = new Perfil();
			perfilRetorno.setCodigo(usuarioPerfil.getPerfil().getCodigo());

			UsuarioPerfil usuarioPerfilRetorno = new UsuarioPerfil();
			usuarioPerfilRetorno.setPerfil(perfilRetorno);

			authoritiesRetorno.add(usuarioPerfilRetorno);
		}
	}

	private void coletarElemento(String funcionalidade, String comando, Map<String, Set<String>> destinoList) {
		Set<String> elementoList = destinoList.get(funcionalidade);
		if (elementoList == null) {
			elementoList = new HashSet<String>();
			destinoList.put(funcionalidade, elementoList);
		}
		elementoList.add(comando);
	}

	@Override
	@Transactional(readOnly = true)
	public final Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
		final Usuario usuario = usuarioDao.findByUsername(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		// inicio avaliacao dos perfis do usuário
		Set<UsuarioPerfil> authoritiesRetorno = (new HashSet<UsuarioPerfil>());
		Map<String, Set<String>> perfilFuncionalidadeComandoListRetorno = new HashMap<String, Set<String>>();
		Map<String, Set<String>> perfilFuncionalidadeComandoListNegadoRetorno = new HashMap<String, Set<String>>();

		// avaliar perfis do usuario
		avaliarPerfil(usuario.getAuthorities(), authoritiesRetorno, perfilFuncionalidadeComandoListRetorno, perfilFuncionalidadeComandoListNegadoRetorno);
		
		// inicio avaliar perfis da empresa do usuario
		List<EmpregoVi> empregoViList = empregoViDao.findByEmpregadoId(usuario.getPessoa().getId());
		for (EmpregoVi empregoVi: empregoViList) {
			Pessoa empregador = pessoaDao.findOne(empregoVi.getEmpregadorId());
			for (Usuario usuarioEmpregador: usuarioDao.findByPessoa(empregador)) {
				avaliarPerfil(usuarioEmpregador.getAuthorities(), authoritiesRetorno, perfilFuncionalidadeComandoListRetorno, perfilFuncionalidadeComandoListNegadoRetorno);
			}
		}
		// fim avaliar perfis da empresa do usuario
		
		// inicio avaliar perfis da lotacoes do usuario
		List<LotacaoVi> lotacaoViList = lotacaoViDao.findByEmpregadoId(usuario.getPessoa().getId());
		for (LotacaoVi lotacaoVi: lotacaoViList) {
			Pessoa unidadeOrganizacional = pessoaDao.findOne(lotacaoVi.getUnidadeOrganizacionalId());
			for (Usuario usuarioUnidadeOrganizacional: usuarioDao.findByPessoa(unidadeOrganizacional)) {
				avaliarPerfil(usuarioUnidadeOrganizacional.getAuthorities(), authoritiesRetorno, perfilFuncionalidadeComandoListRetorno, perfilFuncionalidadeComandoListNegadoRetorno);
			}
		}
		// fim avaliar perfis da lotacoes do usuario

		// inicio remover as funcionalidades/comandos que não foram concedidos
		for (Map.Entry<String, Set<String>> perfilFuncionalidadeComandoNegado : perfilFuncionalidadeComandoListNegadoRetorno.entrySet()) {
			Map.Entry<String, Set<String>> perfilFuncionalidadeComando = null;			
			for (Map.Entry<String, Set<String>> perfilFuncionalidadeComandoRetorno : perfilFuncionalidadeComandoListRetorno.entrySet()) {
				if (perfilFuncionalidadeComandoRetorno.getKey().equals(perfilFuncionalidadeComandoNegado.getKey())) {
					perfilFuncionalidadeComando = perfilFuncionalidadeComandoRetorno;
					break;
				}
			}
			// proceder somente se existir a funcionalidade/comando
			if (perfilFuncionalidadeComando != null) {
				// excluir o comando
				for (String comandoNegado : perfilFuncionalidadeComandoNegado.getValue()) {
					perfilFuncionalidadeComando.getValue().remove(comandoNegado);
				}
				// excluir a funcionalidade se necessario
				if (perfilFuncionalidadeComando.getValue().size() == 0) {
					perfilFuncionalidadeComandoListRetorno.remove(perfilFuncionalidadeComando.getKey());
				}
			}
		}
		// fim remover as funcionalidades/comandos não concedidos

		// fim avaliacao dos perfis do usuário

		Pessoa pessoaRetorno = null;
		try {
			pessoaRetorno = usuario.getPessoa().getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Contacte o administrador do sistema, erro login ao instanciar Pessoa", e);
		}
		pessoaRetorno.setApelidoSigla(usuario.getPessoa().getApelidoSigla());
		pessoaRetorno.setNome(usuario.getPessoa().getNome());
		pessoaRetorno.setPessoaTipo(usuario.getPessoa().getPessoaTipo());
		pessoaRetorno.setSituacao(usuario.getPessoa().getSituacao());
		pessoaRetorno.setSituacaoData(usuario.getPessoa().getSituacaoData());

		Usuario usuarioRetorno = new Usuario();
		usuarioRetorno.setId(usuario.getId());
		usuarioRetorno.setPassword(usuario.getPassword());
		usuarioRetorno.setAuthorities(authoritiesRetorno);
		usuarioRetorno.setExpires(usuario.getExpires());
		usuarioRetorno.setFuncionalidadeComandoList(perfilFuncionalidadeComandoListRetorno);
		usuarioRetorno.setPessoa(pessoaRetorno);
		usuarioRetorno.setUsername(usuario.getUsername());
		usuarioRetorno.setUsuarioStatusConta(usuario.getUsuarioStatusConta());

		// captar os perfis ativos
		detailsChecker.check(usuarioRetorno);
		return usuarioRetorno;
	}
}
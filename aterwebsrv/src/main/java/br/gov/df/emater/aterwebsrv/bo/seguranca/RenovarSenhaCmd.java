package br.gov.df.emater.aterwebsrv.bo.seguranca;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.SenhaPassadaDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.SenhaPassada;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("SegurancaRenovarSenhaCmd")
public class RenovarSenhaCmd extends _Comando {

	@Autowired
	private SenhaPassadaDao senhaPassadaDao;

	@Autowired
	private UsuarioDao usuarioDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario usuario = (Usuario) contexto.getRequisicao();

		String novaSenha = usuario.getNewPassword();

		// protegendo a senha
		for (String nome : usuario.getUsername().split("\\.")) {
			if (novaSenha.toLowerCase().trim().indexOf(nome) >= 0) {
				throw new BoException("Não utilize parte de seu nome de usuário como senha de acesso");
			}
		}
		for (String nome : usuario.getPessoa().getNome().split(" ")) {
			if (nome.length() > 3 && novaSenha.toLowerCase().trim().indexOf(nome.toLowerCase()) >= 0) {
				throw new BoException("Não utilize parte de seu nome como senha de acesso");
			}
		}
		for (String nome : usuario.getPessoa().getApelidoSigla().split(" ")) {
			if (novaSenha.toLowerCase().trim().indexOf(nome.toLowerCase()) >= 0) {
				throw new BoException("Não utilize parte de seu nome como senha de acesso");
			}
		}
		if (UtilitarioString.temCaractereRepetido(novaSenha.toLowerCase(), 3)) {
			throw new BoException("Não utilize caracteres muitas vezes repetido na senha de acesso");
		}

		Usuario usuarioSalvo = usuarioDao.findByUsername(usuario.getUsername());
		if (usuarioSalvo == null) {
			throw new BoException("Usuário não cadastrado");
		}
/*		if (usuarioSalvo.getPessoa() instanceof PessoaFisica) {
			Calendar aniversario = ((PessoaFisica) usuarioSalvo.getPessoa()).getNascimento();
			if (aniversario != null) {
				String ano = UtilitarioString.zeroEsquerda(Integer.toString(aniversario.get(Calendar.YEAR)), 4);
				String ano2Dig = UtilitarioString.zeroEsquerda(Integer.toString(aniversario.get(Calendar.YEAR)).substring(2, 4), 2);
				String mes = UtilitarioString.zeroEsquerda(Integer.toString(aniversario.get(Calendar.MONTH) + 1), 2);
				String dia = UtilitarioString.zeroEsquerda(Integer.toString(aniversario.get(Calendar.DATE)), 2);
				if ((novaSenha.toLowerCase().trim().indexOf(ano) >= 0) || (novaSenha.toLowerCase().trim().indexOf(ano2Dig) >= 0) || (novaSenha.toLowerCase().trim().indexOf(mes) >= 0) || (novaSenha.toLowerCase().trim().indexOf(dia) >= 0)) {
					throw new BoException("Não utilize parte de seu aniversário como senha de acesso");
				}
			}
		}
		if (senhaPassadaDao.findOneByUsuarioIdAndSenha(usuarioSalvo.getId(), novaSenha) != null) {
			throw new BoException("Não utilize senhas passadas.");
		}
*/
		String senhaAtual = Criptografia.MD5(usuarioSalvo.getId() + usuario.getPassword());
		if (!senhaAtual.equals(usuarioSalvo.getPassword())) {
			throw new BoException("A senha atual é inválida!");
		}
		novaSenha = Criptografia.MD5(usuarioSalvo.getId() + usuario.getNewPassword());
		if (novaSenha.equals(usuarioSalvo.getPassword())) {
			throw new BoException("A nova senha não pode ser a mesma que a atual");
		}

		usuarioSalvo.setUsuarioStatusConta(UsuarioStatusConta.A);
		Calendar acessoExpiraEm = Calendar.getInstance();
		acessoExpiraEm.add(Calendar.MONTH, 6);
		usuarioSalvo.setAcessoExpiraEm(acessoExpiraEm);
		usuarioSalvo.setPassword(novaSenha);
		usuarioDao.save(usuarioSalvo);

		senhaPassadaDao.save(new SenhaPassada(usuarioSalvo, usuarioSalvo.getPassword()));

		return false;
	}

}
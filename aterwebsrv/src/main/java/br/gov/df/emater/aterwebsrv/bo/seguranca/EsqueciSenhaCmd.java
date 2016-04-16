package br.gov.df.emater.aterwebsrv.bo.seguranca;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("SegurancaEsqueciSenhaCmd")
public class EsqueciSenhaCmd extends _Comando {
	
	@Autowired
	private UsuarioDao usuarioDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		String email = (String) contexto.getRequisicao();
		
		Usuario usuario = usuarioDao.findByPessoaEmailEmailEndereco(email);
		
		if (usuario == null) {
			throw new BoException("Este e-mail para renovação de senha não está cadastrado. Nenhum e-mail foi enviado!");
		}
		if (!UsuarioStatusConta.R.equals(usuario.getUsuarioStatusConta()) && !usuario.isAccountNonExpired()) {
			throw new BoException("A conta vinculada a este e-mail está inativa. Para maiores informações, entre em contato com o administrador do sistema. Nenhum e-mail foi enviado!");
		}
		
		StringBuilder novaSenha = new StringBuilder();
		novaSenha.append("Nova");
		novaSenha.append(UtilitarioString.zeroEsquerda((int) (9999 * Math.random()), 4));

		Calendar expiracao = Calendar.getInstance();
		expiracao.roll(Calendar.DATE, 1);
		
		StringBuilder texto = new StringBuilder();
		texto.append("Você solicitou a renovação de sua senha no sistema ATER web. Para tal:").append("\n");
		texto.append("1. acesse o sistema e efetue o login");
		texto.append("\n");
		texto.append("2. informe o usuario => ");
		texto.append(usuario.getUsername());
		texto.append("\n");
		texto.append("3. informe a senha => ");
		texto.append(novaSenha.toString());
		texto.append("\n");
		texto.append("Caso não tenha solicitado esta renovação de senha, por favor, ignore este e-mail.");
		texto.append("\n");
		texto.append("O prazo para renovação da senha termina em ");
		texto.append(UtilitarioData.getInstance().formataDataHora(expiracao));
		texto.append("\n");

		usuario.setPassword(Criptografia.MD5(usuario.getId() + novaSenha.toString()));
		usuario.setUsuarioStatusConta(UsuarioStatusConta.R);
		usuario.setExpires(expiracao);		
		usuarioDao.saveAndFlush(usuario);
		
		Map<String, Object> requisicao = new HashMap<String, Object>();
		
		requisicao.put("para", email);
		requisicao.put("copiaPara", email);
		requisicao.put("remetente", "aterweb@emater.df.gov.br");
		requisicao.put("assunto", "ATER web, renove sua senha");
		requisicao.put("texto", texto.toString());
		
		contexto.setRequisicao(requisicao);
		
		return false;
	}

}
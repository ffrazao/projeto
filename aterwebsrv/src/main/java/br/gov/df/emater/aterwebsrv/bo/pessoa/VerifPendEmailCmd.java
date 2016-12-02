package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EmailDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("PessoaVerifPendEmailCmd")
public class VerifPendEmailCmd extends VerificarPendenciasCmd {
	
	@Autowired
	private EmailDao emailDao;

	@Override
	public String constatarPendencia(_Contexto contexto) {
		Pessoa pessoa = (Pessoa) getPendenciavel();

		List<String> mensagemList = new ArrayList<String>();
		if (pessoa.getEmailList() != null) {
			Set<String> cadastrado = new HashSet<>();
			for (int i = pessoa.getEmailList().size() - 1; i >= 0; i--) {
				if (pessoa.getEmailList().get(i).getEmail() == null || StringUtils.isBlank(pessoa.getEmailList().get(i).getEmail().getEndereco())) {
					pessoa.getEmailList().remove(i);
					continue;
				}
				String informado = UtilitarioString.formataEmail(pessoa.getEmailList().get(i).getEmail().getEndereco());
				if (!Util.isEmailValido(informado) || !UtilitarioString.isValidEmail(informado)) {
					mensagemList.add(String.format("Email inválido [%s]", pessoa.getEmailList().get(i).getEmail().getEndereco()));
					pessoa.getEmailList().remove(i);
					continue;
				} else {
					if (cadastrado.contains(informado)) {
						pessoa.getEmailList().remove(i);
						continue;
					}
					cadastrado.add(informado);
					Email temp = emailDao.findByEndereco(informado);
					if (temp == null) {
						temp = new Email(informado);
					}
					pessoa.getEmailList().get(i).setEmail(temp);
				}
			}
		}
		return extraiResultado(mensagemList);
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.EMAIL;
	}

}
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
import br.gov.df.emater.aterwebsrv.dao.pessoa.TelefoneDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;

@Service("PessoaVerifPendTelefoneCmd")
public class VerifPendTelefoneCmd extends VerificarPendenciasCmd {
	
	@Autowired
	private TelefoneDao telefoneDao;

	@Override
	public String constatarPendencia(_Contexto contexto) {
		Pessoa pessoa = (Pessoa) getPendenciavel();

		List<String> mensagemList = new ArrayList<String>();
		if (pessoa.getTelefoneList() != null) {
			Set<String> cadastrado = new HashSet<>();
			for (int i = pessoa.getTelefoneList().size() - 1; i >= 0; i--) {
				if (pessoa.getTelefoneList().get(i).getTelefone() == null || StringUtils.isBlank(pessoa.getTelefoneList().get(i).getTelefone().getNumero())) {
					pessoa.getTelefoneList().remove(i);
					continue;
				}
				String informado = UtilitarioString.formataTelefone(pessoa.getTelefoneList().get(i).getTelefone().getNumero());
				if (!UtilitarioString.soNumero(informado).endsWith(UtilitarioString.soNumero(pessoa.getTelefoneList().get(i).getTelefone().getNumero()))) {
					mensagemList.add(String.format("Telefone, número ou formato inválido [%s]", pessoa.getTelefoneList().get(i).getTelefone().getNumero()));
					pessoa.getTelefoneList().remove(i);
					continue;
				} else {
					if (cadastrado.contains(informado)) {
						pessoa.getTelefoneList().remove(i);
						continue;
					}
					cadastrado.add(informado);
					Telefone temp = telefoneDao.findByNumero(informado);
					if (temp == null) {
						temp = new Telefone(informado);
					}
					pessoa.getTelefoneList().get(i).setTelefone(temp);
				}
			}
		}
		return extraiResultado(mensagemList);
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.TELEFONE;
	}

}
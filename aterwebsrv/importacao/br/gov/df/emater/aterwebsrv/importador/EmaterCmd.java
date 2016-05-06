package br.gov.df.emater.aterwebsrv.importador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service
public class EmaterCmd extends _Comando {

	@Autowired
	private PessoaDao pessoaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		FacadeBo facadeBo = (FacadeBo) contexto.get("facadeBo");
		
		Pessoa emater = pessoaDao.findByNome("Empresa de Assistência Técnica e Extensão Rural do Distrito Federal");
		if (emater == null) {
			emater = new PessoaJuridica();
			emater.setNome("Empresa de Assistência Técnica e Extensão Rural do Distrito Federal");
			emater.setApelidoSigla("EMATER-DF");
			emater.setPublicoAlvoConfirmacao(Confirmacao.N);			
			List<PessoaEmail> pessoaEmailList = new ArrayList<PessoaEmail>();
			pessoaEmailList.add(new PessoaEmail(null, new Email("aterweb@emater.df.gov.br"), "C"));
			emater.setEmailList(pessoaEmailList);
			emater.setId((Integer) facadeBo.pessoaSalvar(null, emater).getResposta());
		}

		contexto.put("emater", pessoaDao.findOne(emater.getId()));

		return false;
	}

}
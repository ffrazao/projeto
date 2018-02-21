package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Util;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;

@Service("PessoaVerificaCPFCmd")
public class VerificaCPFCmd extends _Comando {

	@Autowired
	private PessoaFisicaDao pessoaFisicaDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		String msg = "";
		String numero = (String) contexto.getRequisicao();


		// formatar o número
		if (Util.isCpfValido(numero)){
			numero = UtilitarioString.formataCpf(numero);
			
			List<PessoaFisica> pessoaFisicaList = pessoaFisicaDao.findByCpf(numero);
			if (pessoaFisicaList != null) {
				for (PessoaFisica pessoaFisica : pessoaFisicaList) {
					msg = "CPF já cadastrado para: " + pessoaFisica.getNome();
				}
			} 
			if( msg == "") {
				
				List<PessoaRelacionamento> pessoaRelacionamentoList = pessoaRelacionamentoDao.findByCpf(numero);
				if (pessoaRelacionamentoList != null) {
					for (PessoaRelacionamento pessoaRelacionamento : pessoaRelacionamentoList) {
						
						List<PessoaRelacionamento> relacionamentoList = pessoaRelacionamento.getRelacionamento().getPessoaRelacionamentoList();
						if (relacionamentoList == null) {
							relacionamentoList = pessoaRelacionamentoDao.findByRelacionamento(pessoaRelacionamento.getRelacionamento());
						}
						for (PessoaRelacionamento pr : relacionamentoList) {
							if (pr.getPessoa() != null) {
								if (!pessoaRelacionamento.getId().equals(pr.getId())){
									msg = "CPF já no cadastrado simplificado de : " + pessoaRelacionamento.getNome() + ", que está no cadastro completo de: "+pr.getPessoa().getNome() ;
								}
							}
						}

						
					}
				}
			}
		
		}
		
		contexto.setResposta(msg);
		
		return false;
	}

	


}

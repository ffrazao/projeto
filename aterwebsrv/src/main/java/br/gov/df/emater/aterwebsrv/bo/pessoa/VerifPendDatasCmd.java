//30112002
package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service("PessoaVerifPendDatasCmd")
public class VerifPendDatasCmd extends VerificarPendenciasCmd {

	@Autowired
	private PessoaFisicaDao pessoaFisicaDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	@Override
	public String constatarPendencia(_Contexto contexto) {

		Calendar dataNascimento = null;
		Calendar dataRg = null;
		List<String> pendenciaList = new ArrayList<>();
		

		if (contexto.getRequisicao() instanceof PessoaFisica) {
			PessoaFisica pessoa = (PessoaFisica) contexto.getRequisicao(); 
			dataNascimento = pessoa.getNascimento();
			dataRg = pessoa.getRgDataEmissao();
			
			// TODO fazer verificacao no proprio registro se pf ver em seus relac.
			// se relac ver principa e outros relacs
			if (dataNascimento != null) {
				if (dataNascimento.after(Calendar.getInstance())) {
					 pendenciaList.add(String.format("A data de nascimento informada é inválida\n"));
				} 
			}
			if (dataRg != null) {
				if (dataRg.after(Calendar.getInstance())) {
					pendenciaList.add(String.format("A data de emissão do RG informada é inválida\n"));
				} 
			}
			
			if (dataNascimento != null && dataRg != null) {
				if (dataNascimento.after(dataRg)) {
					pendenciaList.add(String.format("A data de emissão do RG não pode ser inferior a data de nascimento.\n"));
				} 
			}
			
			return pendenciaList.toString();
			
		} else if (contexto.getRequisicao() instanceof PessoaJuridica) {
			PessoaJuridica pessoa = (PessoaJuridica) contexto.getRequisicao(); 
		}
				
		return null;
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.DATA;
	}

}
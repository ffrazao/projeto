package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("PessoaVerifPendCh")
public class VerifPendCh extends _Cadeia {

	@Autowired
	public VerifPendCh(VerifPendCnpjCmd c1, VerifPendCpfCmd c2, VerifPendInscricaoEstadualCmd c3, VerifPendNisCmd c4, VerifPendRgCmd c5, VerifPendTelefoneCmd c6, VerifPendEmailCmd c7, VerifPendRelacionamentoCmd c8, VerifPendDatasCmd c9) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
		super.addCommand(c4);
		super.addCommand(c5);
		super.addCommand(c6);
		super.addCommand(c7);
		super.addCommand(c8);
		super.addCommand(c9);
	}

}
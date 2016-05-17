package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UF;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;

@Service("PessoaVerifPendRgCmd")
public class VerifPendRgCmd extends VerificarPendenciasCmd {
	
	private final Calendar ANO_1900 = new GregorianCalendar(1900,0,1);

	@Autowired
	private PessoaFisicaDao dao;

	@Override
	public String constatarPendencia() {
		if (!(getPendenciavel() instanceof PessoaFisica)) {
			return null;
		}
		PessoaFisica pessoa = (PessoaFisica) getPendenciavel();

		String numero = pessoa.getRgNumero();
		String orgaoEmissor = pessoa.getRgOrgaoEmissor();
		String uf = pessoa.getRgUf();
		Calendar dataEmissao = pessoa.getRgDataEmissao();
		if (numero == null || numero.trim().length() == 0) {
			pessoa.setRgNumero(null);
			pessoa.setRgOrgaoEmissor(null);
			pessoa.setRgUf(null);
			pessoa.setRgDataEmissao(null);
			return null;
		}
		List<String> mensagemList = new ArrayList<String>();
		if (orgaoEmissor == null || orgaoEmissor.trim().length() == 0) {
			mensagemList.add(String.format("Órgão Emissor não informado"));
		}
		if (uf == null || uf.trim().length() == 0) {
			mensagemList.add(String.format("UF não informado"));
		}
		if (dataEmissao == null) {
			mensagemList.add(String.format("Data de Emissão não informada!"));
		}
		if (uf != null && EnumUtils.getEnum(UF.class, uf) == null) {
			mensagemList.add(String.format("UF inválida, [%s]", uf));
		}
		if (dataEmissao != null && dataEmissao.after(Calendar.getInstance())) {
			mensagemList.add(String.format("Data Emissão inválida, data futura [%s]", UtilitarioData.getInstance().formataData(dataEmissao)));
		}
		if (dataEmissao != null && pessoa.getNascimento() != null && dataEmissao.before(pessoa.getNascimento())) {
			mensagemList.add(String.format("Data Emissão inválida, antes do nascimento da pessoa [emissao=%s, nascimento=%s]", UtilitarioData.getInstance().formataData(dataEmissao), UtilitarioData.getInstance().formataData(pessoa.getNascimento())));
		}
		if (dataEmissao != null && dataEmissao.before(ANO_1900)) {
			mensagemList.add(String.format("Data Emissão inválida, data muito antiga [emissao=%s]", UtilitarioData.getInstance().formataData(dataEmissao)));
		}
		List<PessoaFisica> salvoList = null;
		if (uf == null) {
			salvoList = dao.findByRgNumero(numero);
		} else {
			salvoList = dao.findByRgNumeroAndRgUf(numero, uf);
		}
		if (salvoList != null) {
			for (PessoaFisica salvo : salvoList) {
				if (!salvo.getId().equals(pessoa.getId())) {
					mensagemList.add(String.format("O número de RG informado [%s] já está vinculado à pessoa <a ng-click=\"modalVerPessoa(%d)\">%s</a><br>", numero, salvo.getId(), salvo.getNome()));
				}
			}
		}
		if (mensagemList.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String mensagem : mensagemList) {
			sb.append(mensagem).append("\n");
		}
		return sb.toString();
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.RG;
	}

}
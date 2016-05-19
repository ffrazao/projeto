package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.pendencia.VerificarPendenciasCmd;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisicaDadosBasicos;

@Service("PessoaVerifPendRelacionamentoCmd")
public class VerifPendRelacionamentoCmd extends VerificarPendenciasCmd {

	private static final String MENSAGEM_ERRO = "Erro nos dados da pessoa relacionada [%s] erro => [%s]";

	@Autowired
	private FacadeBo facadeBo;

	@SuppressWarnings("unchecked")
	@Override
	public String constatarPendencia(_Contexto contexto) {
		Pessoa pessoa = (Pessoa) getPendenciavel();

		List<String> mensagemList = new ArrayList<String>();
		if (pessoa.getRelacionamentoList() != null) {
			for (int i = pessoa.getRelacionamentoList().size() - 1; i >= 0; i--) {
				if (pessoa.getRelacionamentoList().get(i).getRelacionamento() == null) {
					pessoa.getRelacionamentoList().remove(i);
					continue;
				}

				if (pessoa.getRelacionamentoList().get(i).getPessoa() == null) {
					PessoaFisicaDadosBasicos pfdb = pessoa.getRelacionamentoList().get(i);
					if (!(pessoa instanceof PessoaFisica)) {
						mensagemList.add(String.format(MENSAGEM_ERRO, pfdb.getNome(), "dados de pessoa vinculada a um tipo de pessoa inválida"));
					}
					// se nome nulo apagar o resto
					if (pfdb.getNome() == null) {
						mensagemList.add(String.format(MENSAGEM_ERRO, pfdb.getNome(), "nome da pessoa não informado"));
						pessoa.getRelacionamentoList().remove(i);
						continue;
					}

					if (pfdb.getCpf() != null) {
						_Contexto resposta = null;
						try {
							resposta = facadeBo._executar(contexto.getUsuario(), "PessoaVerifPendCpfCmd", pessoa.getRelacionamentoList().get(i));
						} catch (Exception e) {
							e.printStackTrace();
						}

						// captar lista de pendencias
						if (resposta != null) {
							List<String> pendenciaList = (List<String>) resposta.get("pendenciaList");
							if (pendenciaList != null) {
								mensagemList.add(String.format(MENSAGEM_ERRO, pfdb.getNome(), UtilitarioString.collectionToString(pendenciaList, ",")));
							} else {
								pfdb.setCpf(UtilitarioString.formataCpf(pfdb.getCpf()));
							}
						}
					}
				}
			}
		}
		return extraiResultado(mensagemList);
	}

	@Override
	public PendenciaCodigo getCodigo() {
		return PendenciaCodigo.RELACIONAMENTO;
	}

}
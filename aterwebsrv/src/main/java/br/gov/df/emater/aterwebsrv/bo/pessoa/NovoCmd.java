package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("PessoaNovoCmd")
public class NovoCmd extends _Comando {

	@Autowired
	private FacadeBo facadeBo;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		Pessoa result = (Pessoa) contexto.getRequisicao();

		result.setSituacao(PessoaSituacao.A);

		// captar os formularios ativos
		_Contexto formularioResposta = facadeBo.formularioFiltroExecutar(contexto.getUsuario(), new FormularioCadFiltroDto(Confirmacao.S));
		if (formularioResposta.getResposta() != null) {
			result.setDiagnosticoList(new ArrayList<Formulario>());
			for (Object[] diagnostico : (List<Object[]>) formularioResposta.getResposta()) {
				((ArrayList<Formulario>) result.getDiagnosticoList()).add(new Formulario((Integer) diagnostico[0], (String) diagnostico[1], (String) diagnostico[2], (Situacao) diagnostico[3], (Calendar) diagnostico[4], (Calendar) diagnostico[5]));
			}
		}

		contexto.setResposta(result);

		return true;
	}

}
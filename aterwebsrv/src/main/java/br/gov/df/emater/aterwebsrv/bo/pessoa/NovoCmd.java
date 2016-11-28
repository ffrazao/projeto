package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service("PessoaNovoCmd")
public class NovoCmd extends _Comando {

	@Autowired
	private FacadeBo facadeBo;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		Pessoa result = (Pessoa) contexto.getRequisicao();

		result.setSituacao(PessoaSituacao.A);

		result.setPublicoAlvoConfirmacao(Confirmacao.N);

		UserAuthentication usuario = (UserAuthentication) contexto.getUsuario();

		if (!(result instanceof GrupoSocial)) {
			
			if (usuario != null && usuario.getDetails() != null && ((Usuario) usuario.getDetails()).getLotacaoAtual() != null) {
				if (UnidadeOrganizacionalClassificacao.OP.equals(((Usuario) usuario.getDetails()).getLotacaoAtual().getClassificacao())) {
					result.setPublicoAlvoConfirmacao(Confirmacao.S);
				}
			}
			
			// captar os formularios ativos
			_Contexto formularioResposta = facadeBo.formularioFiltroExecutar(contexto.getUsuario(),
					new FormularioCadFiltroDto(Confirmacao.S));
			if (formularioResposta.getResposta() != null) {
				result.setDiagnosticoList(new ArrayList<Formulario>());
				for (Object[] diagnostico : (List<Object[]>) formularioResposta.getResposta()) {
					((ArrayList<Formulario>) result.getDiagnosticoList())
					.add(new Formulario((Integer) diagnostico[0], (String) diagnostico[1], (String) diagnostico[2],
							(Situacao) diagnostico[3], (Calendar) diagnostico[4], (Calendar) diagnostico[5]));
				}
			}
		}

		contexto.setResposta(result);

		return true;
	}

}
package br.gov.df.emater.aterwebsrv.bo.pessoa;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("PessoaVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private PessoaDao dao;
	
	@Autowired
	private EntityManager em; 

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Pessoa result = dao.findOne(id);
		switch (result.getPessoaTipo()) {
		case PF:
			PessoaFisica pessoaFisica = (PessoaFisica) result;
			Municipio municipio = null;
			Estado estado = null;
			Pais pais = null;
			if (pessoaFisica.getNascimentoMunicipio() != null) {
				municipio = new Municipio(pessoaFisica.getNascimentoMunicipio().getId(),pessoaFisica.getNascimentoMunicipio().getNome());  
				if (pessoaFisica.getNascimentoMunicipio().getEstado() != null) {
					estado = new Estado(pessoaFisica.getNascimentoMunicipio().getEstado().getId(), pessoaFisica.getNascimentoMunicipio().getEstado().getNome());
					if (pessoaFisica.getNascimentoMunicipio().getEstado().getPais() != null) {
						pais = new Pais(pessoaFisica.getNascimentoMunicipio().getEstado().getPais().getId(), pessoaFisica.getNascimentoMunicipio().getEstado().getPais().getNome());
					}
				}
			}
			pessoaFisica.setNascimentoMunicipio(municipio);
			pessoaFisica.setNascimentoEstado(estado);
			pessoaFisica.setNascimentoPais(pais);
			break;
		case PJ:
			PessoaJuridica pessoaJuridica = (PessoaJuridica) result;
			break;
		}
		result.setUsuarioInclusao(new Usuario(result.getUsuarioInclusao().getUsername()));
		result.setUsuarioAlteracao(new Usuario(result.getUsuarioAlteracao().getUsername()));
		em.detach(result);
		contexto.setResposta(result);
		dao.flush();
		
		return true;
	}
}
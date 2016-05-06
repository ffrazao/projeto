package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Repository("UnidadeOrganizacionalDao")
public interface UnidadeOrganizacionalDao extends JpaRepository<UnidadeOrganizacional, Integer> {

	List<UnidadeOrganizacional> findByNomeLikeAndClassificacaoIn(String nome, Set<UnidadeOrganizacionalClassificacao> classificacao);

	UnidadeOrganizacional findOneByPessoaJuridicaAndSigla(PessoaJuridica pessoa, String lotacaoSigla);
	
}
	
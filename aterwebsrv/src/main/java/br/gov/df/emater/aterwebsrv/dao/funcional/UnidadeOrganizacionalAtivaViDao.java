package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalAtivaVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Repository("UnidadeOrganizacionalAtivaViDao")
public interface UnidadeOrganizacionalAtivaViDao extends JpaRepository<UnidadeOrganizacionalAtivaVi, Integer> {

	List<UnidadeOrganizacional> findByNomeLikeAndClassificacaoIn(String nome, Set<UnidadeOrganizacionalClassificacao> classificacao);

	UnidadeOrganizacional findOneByPessoaJuridicaAndNome(PessoaJuridica emater, String lotacaoNome);

	UnidadeOrganizacional findOneByPessoaJuridicaAndApelidoSigla(PessoaJuridica pessoa, String lotacaoApelidoSigla);

	List<UnidadeOrganizacional> findAllByPessoaJuridica(PessoaJuridica pessoaJuridica);

}

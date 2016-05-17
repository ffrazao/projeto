package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;

@Repository("BemDao")
public interface BemDao extends JpaRepository<Bem, Integer>, BemDaoCustom {

	List<Bem> findByBemClassificacaoAndNomeLikeIgnoreCase(BemClassificacao bemClassificacao, String nome);

}

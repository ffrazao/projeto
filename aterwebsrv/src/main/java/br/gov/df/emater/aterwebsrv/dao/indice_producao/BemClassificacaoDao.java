package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;

@Repository("BemClassificacaoDao")
public interface BemClassificacaoDao extends JpaRepository<BemClassificacao, Integer>, BemClassificacaoDaoCustom {

	List<BemClassificacao> findByNomeLikeIgnoreCase(String nome);

}
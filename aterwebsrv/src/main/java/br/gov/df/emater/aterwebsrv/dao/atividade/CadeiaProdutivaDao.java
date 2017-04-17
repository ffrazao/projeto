package br.gov.df.emater.aterwebsrv.dao.atividade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.Assunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.CadeiaProdutiva;

@Repository("CadeiaProdutivaDao")
public interface CadeiaProdutivaDao extends JpaRepository<Assunto, Integer> {

	CadeiaProdutiva findOneByNome(String nome);

}
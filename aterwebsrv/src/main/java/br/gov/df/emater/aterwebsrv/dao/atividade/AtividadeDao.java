package br.gov.df.emater.aterwebsrv.dao.atividade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;

@Repository("AtividadeDao")
public interface AtividadeDao extends JpaRepository<Atividade, Integer>, AtividadeDaoCustom {

	Atividade findOneByCodigo(String codigo);

}
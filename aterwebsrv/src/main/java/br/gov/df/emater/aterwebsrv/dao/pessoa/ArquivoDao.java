package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

@Repository("ArquivoDao")
public interface ArquivoDao extends JpaRepository<Arquivo, Integer> {

	Arquivo findByMd5(String md5);

}

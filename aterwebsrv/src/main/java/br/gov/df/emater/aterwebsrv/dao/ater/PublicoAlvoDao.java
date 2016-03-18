package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Repository("PublicoAlvoDao")
public interface PublicoAlvoDao extends JpaRepository<PublicoAlvo, Integer> {

	PublicoAlvo findOneByPessoa(Pessoa pessoa);

}
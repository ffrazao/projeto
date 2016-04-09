package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.ater.Setor;

@Repository("PublicoAlvoSetorDao")
public interface PublicoAlvoSetorDao extends JpaRepository<PublicoAlvoSetor, Integer> {

	PublicoAlvoSetor findOneByPublicoAlvoAndSetor(PublicoAlvo publicoAlvo, Setor setor);

}
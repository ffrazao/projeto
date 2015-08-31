package br.gov.df.emater.aterwebsrv.dao.teste;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.teste.Teste;

@Repository("testeDao")
public interface TesteDao extends JpaRepository<Teste, Long> {

//	public Teste findByNome(String nome);

//	@Query(value = "SELECT count(*) FROM teste.teste WHERE nome like :nome", nativeQuery = true)
//	public Integer contarPorNome(@Param("nome") String nome);

//	public Integer countByNomeContainingIgnoreCase(String nome);

}
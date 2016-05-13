package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralArquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

public interface PropriedadeRuralArquivoDao extends JpaRepository<PropriedadeRuralArquivo, Integer> {

	PropriedadeRuralArquivo findOneByPropriedadeRuralAndArquivo(PropriedadeRural result, Arquivo arquivo);

	PropriedadeRuralArquivo findOneByChaveSisater(String chaveSisater);

}
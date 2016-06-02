package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.List;

public interface AtividadeChaveSisaterDaoCustom {

	List<Integer> findAtividadePorChaveSisater(String base);
}
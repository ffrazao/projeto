package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePessoaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Repository("AtividadePessoaDao")
public interface AtividadePessoaDao extends JpaRepository<AtividadePessoa, Integer> {

	List<AtividadePessoa> findByAtividadeAndParticipacao(Atividade result, AtividadePessoaParticipacao participacao);

	List<AtividadePessoa> findTop10ByAtividadeIdAndParticipacaoOrderByPessoaNomeDesc(Integer atividadeId, AtividadePessoaParticipacao participacao);

	List<AtividadePessoa> findByPessoa(Pessoa pessoa);

}
package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadePessoaDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEmailDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaEnderecoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaGrupoSocialDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaTelefoneDao;
import br.gov.df.emater.aterwebsrv.dto.pessoa.MesclarPessoaDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaArquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
          
@Service("MesclarPessoasCmd")
public class MesclarPessoasCmd extends _SalvarCmd { 

	@Autowired
	private PessoaDao PessoaDao;

	@Autowired
	private PessoaArquivoDao pessoaArquivoDao;

	@Autowired
	private PessoaEmailDao pessoaEmailDao;

	@Autowired
	private PessoaEnderecoDao pessoaEnderecoDao;

	@Autowired
	private PessoaGrupoSocialDao pessoaGrupoSocialDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	@Autowired
	private PessoaTelefoneDao pessoaTelefoneDao;

	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private PublicoAlvoPropriedadeRuralDao publicoAlvoPropriedadeRuralDao;

	@Autowired
	private AtividadePessoaDao atividadePessoaDao;
	
	@Autowired
	private IpaDao ipaDao;
	
	@Autowired
	private PessoaFisicaDao pessoaFisicaDao;
	
	@Autowired
	private ColetaDao coletaDao;
	

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		MesclarPessoaDto filtro = (MesclarPessoaDto) contexto.getRequisicao();
				
		PessoaFisica pessoaPrincipal = pessoaFisicaDao.findOneById(filtro.getPrincipalId());
				
        for (Integer pessoaId :  filtro.getPessoaIdList() ) {
        	
        	PessoaFisica pessoa = pessoaFisicaDao.findOneById(pessoaId);
        	
        	int a = pessoa.getId();
        	int b = pessoaPrincipal.getId();

        	if( a != b ){
        		
        		if(
        				(pessoa.getCpf() != null) && 
        				(pessoaPrincipal.getCpf() == null)
        		){
        			pessoaPrincipal.setCpf(pessoa.getCpf());
        		}
    		
            	pessoa.setSituacao( PessoaSituacao.M );
            	pessoa.setSituacaoData(Calendar.getInstance());
            	PessoaDao.save(pessoa);
            	
            	for ( PessoaEndereco pessoaEndereco : pessoa.getEnderecoList() ) {
            		pessoaEndereco.setPessoa(pessoaPrincipal);
            		pessoaEndereco.setPrincipal(Confirmacao.N);
            		pessoaEnderecoDao.save(pessoaEndereco);
            	}
            	//pessoaEnderecoDao.flush();
            	
            	for ( PessoaTelefone pessoaTelefone : pessoa.getTelefoneList() ) {
            		pessoaTelefone.setPessoa(pessoaPrincipal);
            		pessoaTelefone.setPrincipal(Confirmacao.N);
            		pessoaTelefoneDao.save(pessoaTelefone);
            	}
            	//pessoaTelefoneDao.flush();
            	            	
            	for ( PessoaEmail pessoaEmail : pessoa.getEmailList() ) {
            		pessoaEmail.setPessoa(pessoaPrincipal);
            		pessoaEmail.setPrincipal(Confirmacao.N);
            		pessoaEmailDao.save(pessoaEmail);
            	}
            	//pessoaEmailDao.flush();
            	
            	for ( PessoaGrupoSocial pessoaGrupoSocial : pessoa.getGrupoSocialList() ) {
            		pessoaGrupoSocial.setPessoa(pessoaPrincipal);
            		pessoaGrupoSocialDao.save(pessoaGrupoSocial);
            	}
            	//pessoaGrupoSocialDao.flush();

            	for ( PessoaArquivo pessoaArquivo : pessoa.getArquivoList() ) {
            		pessoaArquivo.setPessoa(pessoaPrincipal);
            		pessoaArquivoDao.save(pessoaArquivo);
            	}
            	//pessoaArquivoDao.flush();

            	for ( PessoaRelacionamento pessoaRelacionamento : pessoa.getRelacionamentoList() ) {
            		pessoaRelacionamento.setPessoa(pessoaPrincipal);
            		pessoaRelacionamentoDao.save(pessoaRelacionamento);
            	}
        		//pessoaRelacionamentoDao.flush();
            	
        		if ( ( Confirmacao.N.equals(pessoaPrincipal.getPublicoAlvoConfirmacao()) && Confirmacao.S.equals(pessoa.getPublicoAlvoConfirmacao()) ) ||
        			 ( pessoaPrincipal.getPublicoAlvo() == null && pessoa.getPublicoAlvo() != null ) ){
        			pessoaPrincipal.setPublicoAlvoConfirmacao( Confirmacao.S );
        			pessoaPrincipal.setPublicoAlvo(pessoa.getPublicoAlvo());
        			publicoAlvoDao.save(pessoaPrincipal.getPublicoAlvo()); 
        			publicoAlvoDao.flush();
        			PessoaDao.save(pessoaPrincipal);
        	        PessoaDao.flush();

        		}
        		
            	if (Confirmacao.S.equals(pessoaPrincipal.getPublicoAlvoConfirmacao()) && Confirmacao.S.equals(pessoa.getPublicoAlvoConfirmacao()) ){
            		if( pessoa.getPublicoAlvo() != null ){
	                	for ( PublicoAlvoPropriedadeRural   publicoAlvoPropriedadeRural : pessoa.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList() ) {
	                		publicoAlvoPropriedadeRural.setPublicoAlvo(pessoaPrincipal.getPublicoAlvo());
	                		publicoAlvoPropriedadeRural.setPrincipal(Confirmacao.N);
	                		publicoAlvoPropriedadeRuralDao.save(publicoAlvoPropriedadeRural);
	                	}
	                	//publicoAlvoPropriedadeRuralDao.flush();
            		}
            	}
            	
            	List<AtividadePessoa>  atividadePessoaList =  atividadePessoaDao.findByPessoa(pessoa );
            	for ( AtividadePessoa  atividadePessoa : atividadePessoaList ) {
            		atividadePessoa.setPessoa(pessoaPrincipal);
            		atividadePessoaDao.save(atividadePessoa);
            	}
            	//atividadePessoaDao.flush();          	            	
            	
            	List<Integer> ipaIdPessoa = ipaDao.retornaIdIpa(pessoa.getId());
            	
            	for (Integer id : ipaIdPessoa) {
					ipaDao.UpdatePessoa(pessoaPrincipal.getId(), id);
					//System.out.println("Entrou no for ipa");
					//System.out.println("id: " + id);
				}
            	
            	List<Integer> coletaIdPessoa = coletaDao.findOneByPessoa(pessoa.getId());
            	
            	for(Integer id : coletaIdPessoa){
            		coletaDao.UpdatePessoa(pessoaPrincipal.getId(), id);
            	}
            	
        	}
        }
         
		return false;
	}	
}
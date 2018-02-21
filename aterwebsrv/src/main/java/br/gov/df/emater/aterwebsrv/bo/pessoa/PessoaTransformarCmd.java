package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.dto.pessoa.PessoaSimplesDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.OrganizacaoTipo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("PessoaTransformarCmd")
public class PessoaTransformarCmd extends _Comando {

	@Autowired
	private PessoaDao pessoaDao;
	
	@Autowired
	private PessoaRelacionamentoDao pessoaRelDao;
	
	@Autowired
	private PublicoAlvoDao paDao;
	
	@Autowired
	private UsuarioDao userDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PessoaSimplesDto ps = (PessoaSimplesDto) contexto.getRequisicao();
		String nome = contexto.getUsuario().getName();
		
		PessoaFisica pf = new PessoaFisica();
		
		if(ps.getCpf() != null){
			pf.setCpf(ps.getCpf());
		}
		
		if(ps.getRgNumero() != null){
			pf.setRgNumero(ps.getRgNumero());
		}
		
		if(ps.getApelidoSigla() != null){
			pf.setApelidoSigla(ps.getApelidoSigla());
		}
		
		if(ps.getChaveSisater() != null){
			pf.setChaveSisater(ps.getChaveSisater());
		}
		
		if(ps.getNome() != null){
			pf.setNome(ps.getNome());
		}
		
		if(nome != null){
			//Usuario user = userDao.findByUsername(nome);
			pf.setInclusaoUsuario(userDao.findByUsername(nome));
		}
		
		if("F".equals(ps.getGenero())){
			pf.setGenero(PessoaGenero.F);
		}else if("M".equals(ps.getGenero())){
			pf.setGenero(PessoaGenero.M);
		}
		
		pf.setPessoaTipo(PessoaTipo.PF);
		pf.setSituacaoData(Calendar.getInstance());
		pf.setSituacao(PessoaSituacao.A);
		pf.setPublicoAlvoConfirmacao(Confirmacao.S);
		pf.setObservacoes("Esse cadastro foi criado pela opção: Transformar cadastro Simples em cadastro Completo!");
		
		pessoaDao.save(pf);
		
		//Publico Alvo
		
		PublicoAlvo publicoAlvo = new PublicoAlvo();
		publicoAlvo.setCategoria(PublicoAlvoCategoria.T);
		publicoAlvo.setSegmento(PublicoAlvoSegmento.F);
		OrganizacaoTipo ot = new OrganizacaoTipo();
		ot.setId(9);
		publicoAlvo.setOrganizacaoTipo(ot);
		publicoAlvo.setPessoa(pf);
		
		paDao.save(publicoAlvo);
		pf.setPublicoAlvo(publicoAlvo);
		pessoaDao.save(pf);
		pessoaRelDao.updatePessoaId(pf, ps.getId());
			
		return false;
	}
}
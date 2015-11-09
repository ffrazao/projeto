package br.gov.df.emater.aterwebsrv.bo.endereco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.CidadeDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EstadoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.MunicipioDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PaisDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;

@Service("EnderecoNovoCmd")
public class NovoCmd extends _Comando {
	
	@Autowired
	private PaisDao paisDao;
	
	@Autowired
	private EstadoDao estadoDao;
	
	@Autowired
	private MunicipioDao municipioDao;
	
	@Autowired
	private CidadeDao cidadeDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Endereco result = new Endereco();
		Pais pais = paisDao.findByPadrao(Confirmacao.S).get(0);
		Estado estado = estadoDao.findByPadraoAndPais(Confirmacao.S, pais).get(0);
		Municipio municipio = municipioDao.findByPadraoAndEstado(Confirmacao.S, estado).get(0);
		Cidade cidade = cidadeDao.findByPadraoAndMunicipio(Confirmacao.S, municipio).get(0);
		
		result.setPropriedadeRuralConfirmacao(Confirmacao.N);
		result.setPais(pais.infoBasica());
		result.setEstado(estado.infoBasica());
		result.setMunicipio(municipio.infoBasica());
		result.setCidade(cidade.infoBasica());
		
		contexto.setResposta(result);
		
		return true;
	}

}
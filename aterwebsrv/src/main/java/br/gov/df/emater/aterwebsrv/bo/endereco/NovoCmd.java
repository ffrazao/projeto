package br.gov.df.emater.aterwebsrv.bo.endereco;

import java.util.List;

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
		
		Pais pais = null;
		Estado estado = null;
		Municipio municipio = null;
		Cidade cidade = null;

		List<Pais> paisList = paisDao.findByPadrao(Confirmacao.S);
		if (paisList != null && paisList.size() == 1) {
			pais = paisList.get(0).infoBasica();
			List<Estado> estadoList = estadoDao.findByPadraoAndPais(Confirmacao.S, pais);
			if (estadoList != null && estadoList.size() == 1) {
				estado = estadoList.get(0).infoBasica();
				List<Municipio> municipioList = municipioDao.findByPadraoAndEstado(Confirmacao.S, estado);
				if (municipioList != null && municipioList.size() == 1) {
					municipio = municipioList.get(0).infoBasica();
					List<Cidade> cidadeList = cidadeDao.findByPadraoAndMunicipio(Confirmacao.S, municipio);
					if (cidadeList != null && cidadeList.size() == 1) {
						cidade = cidadeList.get(0).infoBasica();
					}
				}
			}
		}

		result.setPais(pais);
		result.setEstado(estado);
		result.setMunicipio(municipio);
		result.setCidade(cidade);
		result.setBairro("");
		result.setLogradouro("");
		result.setComplemento("");
		result.setNumero("");

		contexto.setResposta(result);

		return true;
	}

}
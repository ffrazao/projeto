package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EstadoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.MunicipioDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PaisDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;

@Service("PessoaBuscarCepCmd")
public class BuscarCepCmd extends _Comando {

	@Autowired
	private PaisDao paisDao;

	@Autowired
	private EstadoDao estadoDao;

	@Autowired
	private MunicipioDao municipioDao;

	private ObjectMapper objectMapper;

	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			synchronized (BuscarCepCmd.class) {
				objectMapper = new ObjectMapper();
				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				// remover todos os itens nulos ou vazios das serializacoes json
				objectMapper.setSerializationInclusion(Include.NON_NULL);
				objectMapper.setSerializationInclusion(Include.NON_EMPTY);
			}
		}
		return objectMapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean executar(_Contexto contexto) throws Exception {
		String cep = (String) contexto.getRequisicao();
		Map<String, Object> mapaEndereco = null;
		contexto.setResposta(null);

		URLConnection con = new URL(String.format("http://viacep.com.br/ws/%s/json/", UtilitarioString.soNumero(cep.trim()))).openConnection();
		try (InputStream in = con.getInputStream()) {
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;

			String resposta = IOUtils.toString(in, encoding);

			if (resposta != null) {
				mapaEndereco = getObjectMapper().readValue(resposta, Map.class);
			}

			if (mapaEndereco.get("erro") == null || !(boolean) mapaEndereco.get("erro")) {
				Endereco result = new Endereco();
				result.setPais(paisDao.findByPadrao(Confirmacao.S).get(0).infoBasica());
				result.setEstado(estadoDao.findOneByPaisAndSigla(result.getPais(), (String) mapaEndereco.get("uf")).infoBasica());
				String municipio = String.format("%%%s%%", ((String) mapaEndereco.get("localidade")).replaceAll("\\s", "%"));
				List<Municipio> municipioList = municipioDao.findByEstadoAndNomeLike(result.getEstado(), municipio);
				if (municipioList.size() > 0) {
					result.setMunicipio(municipioList.get(0).infoBasica());
				}
				result.setCep((String) mapaEndereco.get("cep"));
				result.setLogradouro((String) mapaEndereco.get("logradouro"));
				result.setComplemento((String) mapaEndereco.get("complemento"));
				result.setBairro((String) mapaEndereco.get("bairro"));
				result.setCodigoIbge((String) mapaEndereco.get("ibge"));
				contexto.setResposta(result);
			}

			return false;
		}
	}
}
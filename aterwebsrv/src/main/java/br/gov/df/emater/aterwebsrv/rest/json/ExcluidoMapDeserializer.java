package br.gov.df.emater.aterwebsrv.rest.json;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ExcluidoMapDeserializer extends JsonDeserializer<Map<String, Set<Serializable>>> {

	@Override
	public Map<String, Set<Serializable>> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		Map<String, Set<Serializable>> result = new HashMap<>();

		JsonNode node = parser.getCodec().readTree(parser);
		node.forEach(registro -> registro.fieldNames().forEachRemaining(nomeCampo -> {
			// captar valores enviados
			Set<Serializable> valorList = (Set<Serializable>) result.get(nomeCampo);
			if (valorList == null) {
				valorList = new HashSet<>();
			}
			valorList.add(registro.get(nomeCampo).asInt());

			// acumular item
			result.put(nomeCampo, valorList);
		}));

		return result;
	}
}
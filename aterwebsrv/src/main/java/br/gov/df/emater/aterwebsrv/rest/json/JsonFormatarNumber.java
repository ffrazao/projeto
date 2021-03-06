package br.gov.df.emater.aterwebsrv.rest.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;

public class JsonFormatarNumber extends JsonDeserializer<Number> {

	public JsonFormatarNumber() {
	}

	@Override
	public Number deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Number result = UtilitarioNumero.getInstance().stringToNumber(jp.getText());
		return result;
	}
}
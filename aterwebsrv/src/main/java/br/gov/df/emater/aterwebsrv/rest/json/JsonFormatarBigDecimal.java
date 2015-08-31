package br.gov.df.emater.aterwebsrv.rest.json;

import java.io.IOException;
import java.math.BigDecimal;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonFormatarBigDecimal extends JsonDeserializer<BigDecimal> {

	public JsonFormatarBigDecimal() {
	}

	@Override
	public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		BigDecimal result = UtilitarioNumero.getInstance().stringToBigDecimal(jp.getText());
		return result;
	}
}
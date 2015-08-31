package br.gov.df.emater.aterwebsrv.rest.json;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDeserializerMilisegundos extends JsonDeserializer<Calendar> {

	public JsonDeserializerMilisegundos() {
	}

	@Override
	public Calendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Calendar result = null;
		try {
			result = UtilitarioData.getInstance().formataMilisegundos(jp.getText());
		} catch (ParseException e) {
			new RuntimeException(e);
		}
		return result;
	}
}

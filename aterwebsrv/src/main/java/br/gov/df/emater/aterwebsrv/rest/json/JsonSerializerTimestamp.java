package br.gov.df.emater.aterwebsrv.rest.json;

import java.io.IOException;
import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonSerializerTimestamp extends JsonSerializer<Calendar> {

	@Override
	public void serialize(Calendar date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
		if (date != null) {
			gen.writeString(UtilitarioData.getInstance().formataTimestamp(date));
		}
	}
}
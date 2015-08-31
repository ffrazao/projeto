package br.gov.df.emater.aterwebsrv.rest.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHibernateAwareObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	// private SimpleModule simpleModule = new SimpleModule();

	public JsonHibernateAwareObjectMapper() {

		// simpleModule.registerSubtypes(new NamedType(MeioContatoEmail.class,
		// "MeioContatoEmail"), new NamedType(MeioContatoEndereco.class,
		// "MeioContatoEndereco"), new NamedType(
		// MeioContatoTelefonico.class, "MeioContatoTelefonico"));

		// habilitar o modulo de manipula��o das entidades Hibernate
		// module.configure(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS,
		// true);
		// module.configure(Feature.FORCE_LAZY_LOADING, true);

		//Hibernate4Module hibernate4Module = new Hibernate4Module();
		//registerModule(hibernate4Module);

		// para configurar o formato padr�o das datas
		// serializadas/desserializadas
		setDateFormat(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));

		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		// remover todos os itens nulos ou vazios das serializacoes json
		setSerializationInclusion(Include.NON_NULL);
		setSerializationInclusion(Include.NON_EMPTY);

		// enable(SerializationFeature.INDENT_OUTPUT);

		// para habilitar a identifica��o de todas as classes enviadas e
		// recebidas pelo Json
		// enableDefaultTyping();
		// enableDefaultTyping(DefaultTyping.NON_FINAL);
		// enableDefaultTyping(DefaultTyping.NON_FINAL,
		// JsonTypeInfo.As.WRAPPER_OBJECT);

		// JavaType jt = SimpleType.construct(MeioContato.class);
		// StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		// builder.inclusion(As.PROPERTY);
		// builder.typeProperty("@type");
		// builder.init(Id.NAME,
		// new ClassNameIdResolver(jt, TypeFactory.defaultInstance()));
		//
		// List<NamedType> subtypes = new ArrayList<NamedType>();
		// subtypes.add(new NamedType(MeioContatoEmail.class,
		// "MeioContatoEmail"));
		// subtypes.add(new NamedType(MeioContatoEndereco.class,
		// "MeioContatoEndereco"));
		// subtypes.add(new NamedType(MeioContatoTelefonico.class,
		// "MeioContatoTelefonico"));
		//
		// subtypes.add(new NamedType(PessoaFisica.class, "PessoaFisica"));
		// subtypes.add(new NamedType(PessoaJuridica.class, "PessoaJuridica"));
		// subtypes.add(new NamedType(PessoaGrupo.class, "PessoaGrupo"));
		//
		// builder.buildTypeDeserializer(getDeserializationConfig(), jt,
		// subtypes);
		//
		// setDefaultTyping(builder);
	}
}

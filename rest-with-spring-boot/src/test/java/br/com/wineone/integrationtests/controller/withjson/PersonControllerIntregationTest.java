package br.com.wineone.integrationtests.controller.withjson;


import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonParseException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wineone.configs.TestConfigs;
import br.com.wineone.data.vo.v1.PersonVOResponse;
import br.com.wineone.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.wineone.integrationtests.vo.AccountCredentialsVO;
import br.com.wineone.integrationtests.vo.PersonVO;
import br.com.wineone.integrationtests.vo.TokenVO;
import br.com.wineone.integrationtests.vo.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerIntregationTest extends AbstractIntegrationTest{
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
	private static PersonVO person;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonParseException, JsonMappingException, IOException {
		AccountCredentialsVO credentials = new AccountCredentialsVO("leandro", "admin123");
		
		var token = given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(credentials)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class)
				.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	
	@Test
	@Order(1)
	public void testCreate() throws JsonParseException, JsonMappingException, IOException {
		
		mockPerson();

		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();
		
		PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
		
		person = createdPerson;
		
		Assertions.assertTrue(person.getId() > 0);
		Assertions.assertNotNull(person.getAddress());
		Assertions.assertNotNull(person.getFirstName());
		Assertions.assertNotNull(person.getLastName());
		Assertions.assertNotNull(person.getGender());
		
		Assertions.assertEquals("rua dos bobos",person.getAddress());
		Assertions.assertEquals("matheus lisba",person.getFirstName());
		Assertions.assertEquals("oliveira",person.getLastName());
		Assertions.assertEquals("M",person.getGender());
	}
	
	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonParseException, JsonMappingException, IOException {
		
		mockPerson();

		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();
		Assertions.assertNotNull(content);
		Assertions.assertEquals("Invalid CORS request",content);
	}
	
	@Test
	@Order(3)
	public void testFindById() throws JsonParseException, JsonMappingException, IOException {
		
		mockPerson();
		
		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
						.pathParam("id", person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		Assertions.assertTrue(person.getId() > 0);
		Assertions.assertNotNull(person.getAddress());
		Assertions.assertNotNull(person.getFirstName());
		Assertions.assertNotNull(person.getLastName());
		Assertions.assertNotNull(person.getGender());
		
		Assertions.assertEquals("rua dos bobos",person.getAddress());
		Assertions.assertEquals("matheus lisba",person.getFirstName());
		Assertions.assertEquals("oliveira",person.getLastName());
		Assertions.assertEquals("M",person.getGender());
	}
	
	@Test
	@Order(3)
	public void testFindByIdWithWrongOrigin() throws JsonParseException, JsonMappingException, IOException {
		
		mockPerson();
		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
						.pathParam("id", person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();
		Assertions.assertNotNull(content);
		Assertions.assertEquals("Invalid CORS request",content);
		
	}
	
	@Test
	@Order(4)
	public void testUpdatePerson() throws JsonParseException, JsonMappingException, IOException {
		
		mockPerson();
		
		person.setGender("F");
		person.setFirstName("joão duarte");
		person.setLastName("da silva santos");
		person.setAddress("rua dos outros numero 222");
		
		String content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
				.body(person)
				.when()
				.put()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		Assertions.assertTrue(person.getId() > 0);
		Assertions.assertNotNull(person.getAddress());
		Assertions.assertNotNull(person.getFirstName());
		Assertions.assertNotNull(person.getLastName());
		Assertions.assertNotNull(person.getGender());
		
		Assertions.assertEquals("rua dos outros numero 222",person.getAddress());
		Assertions.assertEquals("joão duarte",person.getFirstName());
		Assertions.assertEquals("da silva santos",person.getLastName());
		Assertions.assertEquals("F",person.getGender());
	}
	
	@Test
	@Order(5)
	public void testFindAllPersons() throws JsonParseException, JsonMappingException, IOException {
		given() // creating another person
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
			.body(person)
			.when()
			.post()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString();

		String allPersons = given() // creating another person
								.spec(specification)
								.contentType(TestConfigs.CONTENT_TYPE_JSON)
								.queryParams("page",0, "limit",100000, "direction", "asc")
								.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
								.basePath("api/person/v1/all")
								.when()
								.get()
								.then()
								.statusCode(200)
								.extract()
								.body()
								.asString();
		
		WrapperPersonVO wrapper = objectMapper.readValue(allPersons, WrapperPersonVO.class);
		List<br.com.wineone.integrationtests.vo.PersonVOResponse> allPersonsList = wrapper.getEmbedded().getPersons();
		System.out.println(allPersonsList.size());
		assertEquals(1017,allPersonsList.size());
		
	}
	
	@Test
	@Order(6)
	public void testDesablePerson() throws JsonParseException, JsonMappingException, IOException {
		
		String content = given()
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		PersonVOResponse persistedPerson = objectMapper.readValue(content, PersonVOResponse.class);
		Assertions.assertEquals(false, persistedPerson.isEnabled());
	}
	
	@Test
	@Order(7)
	public void testDeleteAPerson() throws JsonParseException, JsonMappingException, IOException {
		given()
		.spec(specification)
		.contentType(TestConfigs.CONTENT_TYPE_JSON)
		.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
		.pathParam("id", person.getId())
		.when()
		.delete("{id}")
		.then()
		.statusCode(204)
		.extract()
		.body()
		.asString();
		
		
		String allPersons = given() // creating another person
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page",0, "limit",100000, "direction", "asc")
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
				.basePath("api/person/v1/all")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		WrapperPersonVO wrapper = objectMapper.readValue(allPersons, WrapperPersonVO.class);
		List<br.com.wineone.integrationtests.vo.PersonVOResponse> allPersonsList = wrapper.getEmbedded().getPersons();
		assertEquals(1016,allPersonsList.size());
	}

	

	private void mockPerson() {
		person.setAddress("rua dos bobos");
		person.setFirstName("matheus lisba");
		person.setLastName("oliveira");
//		person.setId(1L);
		person.setGender("M");
	}
}





















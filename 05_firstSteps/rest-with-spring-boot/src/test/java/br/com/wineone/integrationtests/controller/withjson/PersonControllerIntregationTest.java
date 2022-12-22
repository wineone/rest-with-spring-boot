package br.com.wineone.integrationtests.controller.withjson;


import static io.restassured.RestAssured.given;

import java.io.IOException;

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
import br.com.wineone.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.wineone.integrationtests.vo.PersonVO;
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
	@Order(1)
	public void testCreate() throws JsonParseException, JsonMappingException, IOException {
		
		mockPerson();
		specification = new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		
		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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
		specification = new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		
		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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
		specification = new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MATHEUS)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		
		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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
		specification = new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
						.setBasePath("/api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		
		
		String content = given()
						.spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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


	private void mockPerson() {
		person.setAddress("rua dos bobos");
		person.setFirstName("matheus lisba");
		person.setLastName("oliveira");
//		person.setId(1L);
		person.setGender("M");
	}
}





















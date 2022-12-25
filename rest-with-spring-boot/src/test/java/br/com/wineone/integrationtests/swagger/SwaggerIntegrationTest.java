package br.com.wineone.integrationtests.swagger;


import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.wineone.configs.TestConfigs;
import br.com.wineone.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest{
	
	@Test
	public void shouldDisplaySwaggerUiPage() {
		String content = given()
						.basePath("swagger-ui/index.html")
						.port(TestConfigs.SERVER_PORT)
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();
		Assertions.assertTrue(content.contains("Swagger UI"));
		
	}
}

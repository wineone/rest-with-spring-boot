package br.com.wineone.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wineone.data.vo.v1.PersonVO;
import br.com.wineone.data.vo.v2.PersonVOV2;
import br.com.wineone.exceptions.ResourceNotFoundException;
import br.com.wineone.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/person/v1")
@Tag(name = "People", description = "Endpoints for managing people")
public class PersonController {

	@Autowired
	private PersonServices personServices;
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Finds a person", description = "Finds all people", tags = {"people"}, 
		responses = {
			@ApiResponse(
					description = "Success", responseCode = "200", 
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PersonVO.class)
						)	 
			),
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public PersonVO findById(
			@PathVariable(value = "id") Long id
	) {
		return personServices.findById(id);
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Finds all people", description = "Finds all people", tags = {"people"}, 
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
					content = { 
						@Content(
								mediaType = "application/json",
								array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
						) 
					}
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public List<PersonVO> findAll() throws ResourceNotFoundException {
		return personServices.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "creates a person in the db", description = "Finds all people", tags = {"people"}, 
		responses = {
			@ApiResponse(
					description = "Created", responseCode = "201", 
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PersonVO.class)
						)	 
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public PersonVO create(@RequestBody() PersonVO PersonVO) {
		return personServices.create(PersonVO);
	}
	
//	@RequestMapping(value="/v2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public PersonVOV2 createV2(@RequestBody() PersonVOV2 personVOV2) {
//		return personServices.createV2(personVOV2);
//	}
//	
	@RequestMapping(method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "updates a person in the db", description = "Finds all people", tags = {"people"}, 
		responses = {
			@ApiResponse(
					description = "Success", responseCode = "200",
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PersonVO.class)
						)	 
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public PersonVO update(@RequestBody() PersonVO PersonVO) {
		return personServices.update(PersonVO);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "delete a person in the db", description = "Finds all people", tags = {"people"}, 
		responses = {
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<?> delete(
			@PathVariable(value = "id") Long id
	) {
		personServices.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}

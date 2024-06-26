package br.com.wineone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wineone.data.vo.v1.PersonVORequest;
import br.com.wineone.data.vo.v1.PersonVOResponse;
import br.com.wineone.exceptions.ResourceNotFoundException;
import br.com.wineone.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;

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
							schema = @Schema(implementation = PersonVORequest.class)
						)	 
			),
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public PersonVOResponse findById(
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
								array = @ArraySchema(schema = @Schema(implementation = PersonVORequest.class))
						) 
					}
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<PagedModel<EntityModel<PersonVOResponse>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) throws ResourceNotFoundException {
		
		var sortDirection = "asc".equalsIgnoreCase(direction) ? Direction.ASC : Direction.DESC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"firstName"));
		
		return ResponseEntity.ok(personServices.findAll(pageable));
	}
	
	@RequestMapping(value="/findPersonByName/{firstName}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Finds people by name", description = "Finds people by name", tags = {"people"}, 
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
					content = { 
						@Content(
								mediaType = "application/json",
								array = @ArraySchema(schema = @Schema(implementation = PersonVORequest.class))
						) 
					}
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<PagedModel<EntityModel<PersonVOResponse>>> findPersonsByName(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@PathVariable(value = "firstName") String firstname
			) throws ResourceNotFoundException {
		
		var sortDirection = "asc".equalsIgnoreCase(direction) ? Direction.ASC : Direction.DESC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"firstName"));
		
		return ResponseEntity.ok(personServices.findByUsername(firstname,pageable));
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "creates a person in the db", description = "Finds all people", tags = {"people"}, 
		responses = {
			@ApiResponse(
					description = "Created", responseCode = "201", 
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PersonVORequest.class)
						)	 
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public PersonVOResponse create(@RequestBody() PersonVORequest PersonVO) {
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
							schema = @Schema(implementation = PersonVORequest.class)
						)	 
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public PersonVOResponse update(@RequestBody() PersonVOResponse PersonVO) {
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
	
	@PatchMapping(value="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Desable a person in the db", description = "Deable a person in the db", tags = {"people"}, 
		responses = {
			@ApiResponse(
					description = "Success", responseCode = "200", 
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PersonVORequest.class)
						)	 
			),
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public PersonVOResponse desablePerson(
			@PathVariable(value = "id") Long id
	) {
		return personServices.desablePerson(id);
	}
	
}

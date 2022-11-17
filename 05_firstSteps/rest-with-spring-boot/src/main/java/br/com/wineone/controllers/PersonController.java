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

@RestController
@RequestMapping("api/person/v1")
public class PersonController {

	@Autowired
	private PersonServices personServices;
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public PersonVO findById(
			@PathVariable(value = "id") Long id
	) {
		return personServices.findById(id);
	}
	
	@RequestMapping(value="/all", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<PersonVO> findAll() throws ResourceNotFoundException {
		return personServices.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO create(@RequestBody() PersonVO PersonVO) {
		return personServices.create(PersonVO);
	}
	
//	@RequestMapping(value="/v2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public PersonVOV2 createV2(@RequestBody() PersonVOV2 personVOV2) {
//		return personServices.createV2(personVOV2);
//	}
//	
	@RequestMapping(method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO update(@RequestBody() PersonVO PersonVO) {
		return personServices.update(PersonVO);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(
			@PathVariable(value = "id") Long id
	) {
		personServices.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}

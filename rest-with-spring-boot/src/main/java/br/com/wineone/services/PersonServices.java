package br.com.wineone.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.wineone.controllers.PersonController;
import br.com.wineone.data.vo.v1.PersonVORequest;
import br.com.wineone.data.vo.v1.PersonVOResponse;
import br.com.wineone.data.vo.v2.PersonVOV2;
import br.com.wineone.exceptions.RequiredObjectIsNullException;
import br.com.wineone.exceptions.ResourceNotFoundException;
import br.com.wineone.mapper.DozerMapper;
import br.com.wineone.mapper.custom.PersonMapper;
import br.com.wineone.models.Person;
import br.com.wineone.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service()
public class PersonServices {
	
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PersonMapper personMapper;
	
	
	public PersonVOResponse findById(Long id) {
		logger.info("finding one PersonVO!");
		Optional<Person> query =  personRepository.findById(id);
		PersonVOResponse vo = DozerMapper.parseObject(query.orElseThrow(() -> new ResourceNotFoundException("no records found for this id")), PersonVOResponse.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	@Transactional
	public PersonVOResponse desablePerson(Long id) {
		logger.info("finding one PersonVO!");
		personRepository.desablePerson(id);
		Optional<Person> query = personRepository.findById(id);
		PersonVOResponse vo = DozerMapper.parseObject(query.orElseThrow(() -> new ResourceNotFoundException("no records found for this id")), PersonVOResponse.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public Page<PersonVOResponse> findAll(Pageable pageable) {
		logger.info("finding all persons!");
		
		var personPage = personRepository.findAll(pageable);
		
		Page<PersonVOResponse> personVosPage = personPage.map(p -> DozerMapper.parseObject(p,PersonVOResponse.class));
		
		personVosPage.map(
				p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())
		);
		
		return personVosPage;
	}
	
	public PersonVOResponse create(PersonVORequest personVo) throws RequiredObjectIsNullException {
		logger.info("creating a person");
		
		if(personVo == null) throw new RequiredObjectIsNullException();
		
		var entity = DozerMapper.parseObject(personVo, Person.class);
		PersonVOResponse vo = DozerMapper.parseObject(personRepository.save(entity),PersonVOResponse.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVOResponse update(PersonVOResponse personVo) throws RequiredObjectIsNullException {
		logger.info("updating a PersonVO");
		
		if(personVo == null) throw new RequiredObjectIsNullException();
		
		
		Person per = personRepository.findById(personVo.getKey()).orElseThrow(() -> new ResourceNotFoundException("no records found for this id"));
		per.setFirstName(personVo.getFirstName());
		per.setLastName(personVo.getLastName());
		per.setAddress(personVo.getAddress());
		per.setGender(personVo.getGender());
		PersonVOResponse vo = DozerMapper.parseObject(personRepository.save(per),PersonVOResponse.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	
	public void delete(Long id) {
		Person per = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("no records found for this id"));
		personRepository.delete(per);
	}

	public PersonVOV2 createV2(PersonVOV2 personVOV2) {
		logger.info("creating a person");
		personVOV2.setId(0);
		var entity = personMapper.convertVoToEntity(personVOV2);
		return personMapper.convertEntityToVo(personRepository.save(entity));
	}
	
}
















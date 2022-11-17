package br.com.wineone.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.wineone.controllers.PersonController;
import br.com.wineone.data.vo.v1.PersonVO;
import br.com.wineone.data.vo.v2.PersonVOV2;
import br.com.wineone.exceptions.ResourceNotFoundException;
import br.com.wineone.mapper.DozerMapper;
import br.com.wineone.mapper.custom.PersonMapper;
import br.com.wineone.models.Person;
import br.com.wineone.repositories.PersonRepository;

@Service()
public class PersonServices {
	
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PersonMapper personMapper;
	
	
	public PersonVO findById(Long id) {
		logger.info("finding one PersonVO!");
		Optional<Person> query =  personRepository.findById(id);
		PersonVO vo = DozerMapper.parseObject(query.orElseThrow(() -> new ResourceNotFoundException("no records found for this id")), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public List<PersonVO> findAll() {
		logger.info("finding all persons!");
		var persons =  DozerMapper.parseListObjects(personRepository.findAll(),PersonVO.class);
		for(var person: persons) {
			person.add(linkTo(methodOn(PersonController.class).findById(person.getKey())).withSelfRel());
		}	
		return persons;
	}
	
	public PersonVO create(PersonVO personVo) {
		logger.info("creating a person");
		personVo.setKey((long) 0);
		var entity = DozerMapper.parseObject(personVo, Person.class);
		PersonVO vo = DozerMapper.parseObject(personRepository.save(entity),PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVO update(PersonVO personVo) {
		logger.info("updating a PersonVO");
		Person per = personRepository.findById(personVo.getKey()).orElseThrow(() -> new ResourceNotFoundException("no records found for this id"));
		per.setFirstName(personVo.getFirstName());
		per.setLastName(personVo.getLastName());
		per.setAddress(personVo.getAddress());
		per.setGender(personVo.getGender());
		PersonVO vo = DozerMapper.parseObject(personRepository.save(per),PersonVO.class);
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
















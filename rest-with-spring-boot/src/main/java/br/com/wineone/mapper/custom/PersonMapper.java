package br.com.wineone.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.wineone.data.vo.v2.PersonVOV2;
import br.com.wineone.models.Person;

@Service
public class PersonMapper {
	public PersonVOV2 convertEntityToVo(Person person) {
		PersonVOV2 pervo = new PersonVOV2();
		pervo.setId(person.getId());
		pervo.setAddress(person.getAddress());
		pervo.setBirthday(new Date());
		pervo.setFirstName(person.getFirstName());
		pervo.setGender(person.getGender());
		pervo.setLastName(person.getLastName());
		return pervo;
	}
	
	public Person convertVoToEntity(PersonVOV2 person) {
		Person pervo = new Person();
		pervo.setId(person.getId());
		pervo.setAddress(person.getAddress());
		pervo.setFirstName(person.getFirstName());
		pervo.setGender(person.getGender());
		pervo.setLastName(person.getLastName());
		return pervo;
	}
}

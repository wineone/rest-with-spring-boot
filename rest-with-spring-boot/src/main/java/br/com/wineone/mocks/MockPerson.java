package br.com.wineone.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.wineone.data.vo.v1.PersonVORequest;
import br.com.wineone.data.vo.v1.PersonVOResponse;
import br.com.wineone.models.Person;

public class MockPerson {
	public static Person getMockPerson(long id) {
		Person p =  new Person();
		p.setAddress("");
		p.setFirstName("");
		p.setGender("");
		p.setId(id);
		p.setLastName("");
		return p;
	}
	
	public static Person getMockPerson() {
		Person p =  new Person();
		p.setAddress("");
		p.setFirstName("");
		p.setGender("");
		p.setLastName("");
		return p;
	}
	
	public static PersonVOResponse getMockVOResponse(long id) {
		PersonVOResponse p =  new PersonVOResponse();
		p.setAddress("");
		p.setFirstName("");
		p.setGender("");
		p.setKey(id);
		p.setLastName("");
		return p;
	}
	
	public static PersonVORequest getMockVORequest() {
		PersonVORequest p =  new PersonVORequest();
		p.setAddress("");
		p.setFirstName("");
		p.setGender("");
		p.setLastName("");
		return p;
	}
	
	public static List<Person> getMockListPerson(){
		List<Person> persons = new ArrayList<Person>();
		for(int i = 0; i < 14; i++) {
			Person per = new Person();
			per.setAddress(""+i);
			per.setFirstName(""+i);
			per.setGender(""+i);
			per.setId(i);
			per.setLastName(""+i);
			persons.add(per);
		}
		return persons;
	}
}

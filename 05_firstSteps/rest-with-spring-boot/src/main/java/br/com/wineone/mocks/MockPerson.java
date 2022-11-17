package br.com.wineone.mocks;

import br.com.wineone.data.vo.v1.PersonVO;
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
	
	public static PersonVO getMockVO(long id) {
		PersonVO p =  new PersonVO();
		p.setAddress("");
		p.setFirstName("");
		p.setGender("");
		p.setKey(id);
		p.setLastName("");
		return p;
	}
}

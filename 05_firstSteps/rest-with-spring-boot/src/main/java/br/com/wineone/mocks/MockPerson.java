package br.com.wineone.mocks;

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
}

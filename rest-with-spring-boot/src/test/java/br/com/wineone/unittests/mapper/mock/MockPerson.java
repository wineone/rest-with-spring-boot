package br.com.wineone.unittests.mapper.mock;

import java.util.ArrayList;
import java.util.List;

import br.com.wineone.data.vo.v1.PersonVORequest;
import br.com.wineone.data.vo.v1.PersonVOResponse;
import br.com.wineone.models.Person;


public class MockPerson {


    public Person mockEntity() {
        return mockEntity(0);
    }
    
    public PersonVOResponse mockVO() {
        return mockVO(0);
    }
    
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonVOResponse> mockVOList() {
        List<PersonVOResponse> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }
    
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

    public PersonVOResponse mockVO(Integer number) {
    	PersonVOResponse person = new PersonVOResponse();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender(((number % 2)==0) ? "Male" : "Female");
        person.setKey((long) number.longValue());
        person.setLastName("Last Name Test" + number);
        return person;
    }

}

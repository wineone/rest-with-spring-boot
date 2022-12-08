package br.com.wineone.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.wineone.data.vo.v1.PersonVO;
import br.com.wineone.exceptions.RequiredObjectIsNullException;
import br.com.wineone.mocks.MockPerson;
import br.com.wineone.models.Person;
import br.com.wineone.repositories.PersonRepository;
import br.com.wineone.services.PersonServices;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {
	
	MockPerson input; 
	
	@InjectMocks
	private PersonServices service;
	
	@Mock
	private PersonRepository repository; 

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testFindById() {
		Person person = input.getMockPerson(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(person));
		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().equals("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals(result.getAddress(),"");
		assertEquals(result.getFirstName(),"");
		assertEquals(result.getGender(),"");
		assertEquals(result.getLastName(),"");
	}

	@Test
	void testFindAll() {
		List<Person> persons = input.getMockListPerson();
		when(repository.findAll()).thenReturn(persons);
		var result = service.findAll();
		assertNotNull(result);
		
		PersonVO p1 = result.get(1);
		
		assertNotNull(p1.getKey());
		assertNotNull(p1.getLinks());
		assertTrue(p1.toString().equals("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals(p1.getAddress(),"1");
		assertEquals(p1.getFirstName(),"1");
		assertEquals(p1.getGender(),"1");
		assertEquals(p1.getLastName(),"1");
		
		PersonVO p7 = result.get(7);
		
		assertNotNull(p7.getKey());
		assertNotNull(p7.getLinks());
		assertTrue(p7.toString().equals("links: [</api/person/v1/7>;rel=\"self\"]"));
		assertEquals(p7.getAddress(),"7");
		assertEquals(p7.getFirstName(),"7");
		assertEquals(p7.getGender(),"7");
		assertEquals(p7.getLastName(),"7");
		
		
	}

	@Test
	void testCreate() {
		Person entity = input.getMockPerson(1L);
		
		Person persisted = input.getMockPerson(1L);
		persisted.setId(1L);
		
		PersonVO pvo = input.getMockVO(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		var result = service.create(pvo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().equals("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals(result.getAddress(),"");
		assertEquals(result.getFirstName(),"");
		assertEquals(result.getGender(),"");
		assertEquals(result.getLastName(),"");
	}
	
	@Test
	void testCreateWithNullPerson() {
		RequiredObjectIsNullException exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It is not allowed to write a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(expectedMessage.equals(actualMessage));
	}
	
	@Test
	void testUpdate() {
		Person entity = input.getMockPerson(1L);
		
		Person persisted = input.getMockPerson(1L);
		persisted.setId(1L);
		
		PersonVO pvo = input.getMockVO(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		when(repository.save(entity)).thenReturn(persisted);
		var result = service.update(pvo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().equals("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals(result.getAddress(),"");
		assertEquals(result.getFirstName(),"");
		assertEquals(result.getGender(),"");
		assertEquals(result.getLastName(),"");
	}
	
	@Test
	void testUpdateWithNullPerson() {
		RequiredObjectIsNullException exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It is not allowed to write a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(expectedMessage.equals(actualMessage));
	}

}
	
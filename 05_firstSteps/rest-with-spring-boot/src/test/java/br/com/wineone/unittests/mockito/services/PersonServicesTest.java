package br.com.wineone.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
		fail("Not yet implemented");
	}

	@Test
	void testCreate() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateV2() {
		fail("Not yet implemented");
	}

}

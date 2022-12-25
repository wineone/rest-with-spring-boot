package br.com.wineone.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import br.com.wineone.data.vo.v1.BookVO;
import br.com.wineone.exceptions.RequiredObjectIsNullException;
import br.com.wineone.models.Book;
import br.com.wineone.models.Person;
import br.com.wineone.repositories.BookRepository;
import br.com.wineone.repositories.PersonRepository;
import br.com.wineone.services.BookServices;
import br.com.wineone.unittests.mapper.mock.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {

	MockBook input = new MockBook();
	
	@InjectMocks
	private BookServices service;
	
	@Mock
	private BookRepository repository;
	@Mock 
	private PersonRepository personRepository;
	
	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Test
	void testFindById() {
		Book book = input.mockEntity((int) 1);
		when(repository.findById(1L)).thenReturn(Optional.of(book));
		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getLinks());
		assertTrue(result.toString().equals("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals(result.getTitle(),"a viagem de chihiro");
	}

	@Test
	void testFindAll() {
		List<Book> books = input.mockEntityList();
		when(repository.findAll()).thenReturn(books);
		var result = service.findAll();
		assertNotNull(result);
		
		BookVO p1 = result.get(1);
		
		assertNotNull(p1);
		assertNotNull(p1.getLinks());
		assertTrue(p1.toString().equals("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals(p1.getTitle(),"a viagem de chihiro");
		
		BookVO p7 = result.get(7);
		
		assertNotNull(p7);
		assertNotNull(p7.getLinks());
		assertTrue(p7.toString().equals("links: [</api/book/v1/7>;rel=\"self\"]"));
		assertEquals(p7.getTitle(),"a viagem de chihiro");
		
		
	}

	@Test
	void testCreate() {
		Book entity = input.mockEntity(1);
		
		Book persisted = input.mockEntity(1);
		
		BookVO pvo = input.mockVO(1);
		Person p = new Person();
		entity.setAuthor(p);
		persisted.setAuthor(p);
		p.setId(1);
		
		when(personRepository.findById(1L)).thenReturn(Optional.of(p));
		when(repository.save(entity)).thenReturn(persisted);
		var result = service.create(pvo);
		assertNotNull(result);
		assertNotNull(result.getLinks());
		assertTrue(result.toString().equals("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals(result.getTitle(),"a viagem de chihiro");
	}
//	
	@Test
	void testCreateWithNullPerson() {
		RequiredObjectIsNullException exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It is not allowed to write a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(expectedMessage.equals(actualMessage));
	}
//	
//	@Test
//	void testUpdate() {
//		Person entity = input.getMockPerson(1L);
//		
//		Person persisted = input.getMockPerson(1L);
//		persisted.setId(1L);
//		
//		PersonVO pvo = input.getMockVO(1L);
//		
//		when(repository.findById(1L)).thenReturn(Optional.of(entity));
//		when(repository.save(entity)).thenReturn(persisted);
//		
//		when(repository.save(entity)).thenReturn(persisted);
//		var result = service.update(pvo);
//		assertNotNull(result);
//		assertNotNull(result.getKey());
//		assertNotNull(result.getLinks());
//		assertTrue(result.toString().equals("links: [</api/person/v1/1>;rel=\"self\"]"));
//		assertEquals(result.getAddress(),"");
//		assertEquals(result.getFirstName(),"");
//		assertEquals(result.getGender(),"");
//		assertEquals(result.getLastName(),"");
//	}
//	
//	@Test
//	void testUpdateWithNullPerson() {
//		RequiredObjectIsNullException exception = assertThrows(RequiredObjectIsNullException.class, () -> {
//			service.create(null);
//		});
//		
//		String expectedMessage = "It is not allowed to write a null object!";
//		String actualMessage = exception.getMessage();
//		
//		assertTrue(expectedMessage.equals(actualMessage));
//	}
	
}

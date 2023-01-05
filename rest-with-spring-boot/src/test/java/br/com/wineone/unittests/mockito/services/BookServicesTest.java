package br.com.wineone.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import br.com.wineone.controllers.BookController;
import br.com.wineone.data.vo.v1.BookVO;
import br.com.wineone.exceptions.RequiredObjectIsNullException;
import br.com.wineone.mapper.DozerMapper;
import br.com.wineone.models.Book;
import br.com.wineone.models.Person;
import br.com.wineone.repositories.BookRepository;
import br.com.wineone.repositories.PersonRepository;
import br.com.wineone.services.BookServices;
import br.com.wineone.unittests.mapper.mock.MockBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class BookServicesTest {

	MockBook input = new MockBook();
	
	@InjectMocks
	private BookServices service;
	
	@Mock
	private BookRepository repository;
	@Mock 
	private PersonRepository personRepository;
	
	@Mock
	private PagedResourcesAssembler<BookVO> assembler;
	
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
		Page<Book> pageBooks = new PageImpl<Book>(books);
		
		Pageable pageable = PageRequest.of(0, 100, Sort.by(Direction.ASC,"firstName"));
		
		
		Page<BookVO> bookVoPage = pageBooks.map(p ->{
			BookVO ret = new BookVO();
			ret.setAuthorId(p.getAuthor().getId());
			ret.setId(p.getId());
			ret.setLaunchDate(p.getLaunchDate());
			ret.setPrice(p.getPrice());
			ret.setTitle(p.getTitle());
			return ret;
		});
		
		bookVoPage.map(
				b -> b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel())
		);
		
		Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		PagedResourcesAssembler<BookVO> assembler1 = new PagedResourcesAssembler<BookVO>(null, null);
		
		var assResult = assembler1.toModel(bookVoPage,link);
		
		
		when(assembler.toModel(bookVoPage,link)).thenReturn(assResult);
		when(repository.findAll(pageable)).thenReturn(pageBooks);
		
		
		var result = service.findAll(pageable);
		
		var r = result.getContent().toArray();
		assertNotNull(r);
		
		@SuppressWarnings("unchecked")
		BookVO p1 = ((EntityModel<BookVO>) r[1]).getContent();
		
		
		assertNotNull(p1);
		assertNotNull(p1.getLinks());
		assertTrue(p1.toString().equals("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals(p1.getTitle(),"a viagem de chihiro");
		
		@SuppressWarnings("unchecked")
		BookVO p7 = ((EntityModel<BookVO>) r[7]).getContent();
		
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

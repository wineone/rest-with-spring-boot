package br.com.wineone.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wineone.data.vo.v1.BookVO;
import br.com.wineone.data.vo.v1.PersonVO;
import br.com.wineone.exceptions.ResourceNotFoundException;
import br.com.wineone.models.Book;
import br.com.wineone.models.Person;
import br.com.wineone.repositories.BookRepository;
import br.com.wineone.repositories.PersonRepository;

@Service
public class BookServices {

	
	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PersonRepository personRepository;
	
	
	public BookVO create(BookVO bookVo) {
		logger.info("creating a book");
		System.out.println(bookVo.getTitle());
		Optional<Person> per = personRepository.findById(bookVo.getAuthorId());
		if(!per.isPresent()) {
			throw new ResourceNotFoundException("this author not exists in the db");
		}
		
		Book book = new Book();
		book.setAuthor(per.get());
		book.setId(0L);
		book.setLaunchDate(bookVo.getLaunchDate());
		book.setPrice(bookVo.getPrice());
		book.setTitle(bookVo.getTitle());
		
		bookRepository.save(book);
		
		return bookVo;
	}
	
	public List<Book> findAll() {
		return bookRepository.findAll();
	}
}

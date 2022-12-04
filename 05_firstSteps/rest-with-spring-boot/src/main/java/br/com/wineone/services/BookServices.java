package br.com.wineone.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wineone.controllers.BookController;
import br.com.wineone.controllers.PersonController;
import br.com.wineone.data.vo.v1.BookVO;
import br.com.wineone.data.vo.v1.PersonVO;
import br.com.wineone.exceptions.ResourceNotFoundException;
import br.com.wineone.mapper.custom.BookMapper;
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

		Optional<Person> per = personRepository.findById(bookVo.getAuthorId());
		if(!per.isPresent()) {
			throw new ResourceNotFoundException("this author not exists in the db");
		}
		
		Book book = BookMapper.convertVoToEntity(bookVo, per.get());
		
		BookVO vo = BookMapper.convertEntityToVo(bookRepository.save(book));
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
		return vo;
	}
	
	public List<BookVO> findAll() {
		List<Book> bookList = bookRepository.findAll();
		List<BookVO> bookVoList = new ArrayList<BookVO>();
		for(Book book: bookList) {
			BookVO vo = BookMapper.convertEntityToVo(book);
			vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
			bookVoList.add(vo);
		}
		return bookVoList;
	}

	public BookVO findById(Long id) {
		Optional<Book> book = bookRepository.findById(id);
		if(book.isEmpty()) {
			throw new ResourceNotFoundException("this book not exists in the db");
		}
		BookVO vo =  BookMapper.convertEntityToVo(book.get());
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		
		Optional<Book> book = bookRepository.findById(id);
		if(book.isEmpty()) {
			throw new ResourceNotFoundException("this book not exists in the db");
		}
		
		bookRepository.delete(book.get());
	}
	
	public BookVO update(BookVO bookVo) {
		Optional<Book> opBook = bookRepository.findById(bookVo.getId());
		if(opBook.isEmpty()) {
			throw new ResourceNotFoundException("this book not exists in the db");
		}
		
		Book book = opBook.get();
		
		if(!(bookVo.getAuthorId() == null)) {
			Optional<Person> per = personRepository.findById(bookVo.getAuthorId());
			if(!per.isPresent()) {
				throw new ResourceNotFoundException("this author not exists in the db");
			}
			book.setAuthor(per.get());
		}
		
		if(!(bookVo.getLaunchDate() == null)) {
			book.setLaunchDate(bookVo.getLaunchDate());
		}
		
		book.setPrice(bookVo.getPrice());
		
		if(!(bookVo.getTitle() == null)) {
			book.setTitle(bookVo.getTitle());
		}
		
		BookVO vo = BookMapper.convertEntityToVo(bookRepository.save(book));
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
		return vo;	
	}
	
}

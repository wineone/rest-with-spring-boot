package br.com.wineone.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wineone.data.vo.v1.BookVO;
import br.com.wineone.models.Book;
import br.com.wineone.services.BookServices;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/book/v1")
@Tag(name = "Book",description = "Endpoint for manage the books")
public class BookController {

	
	@Autowired
	private BookServices bookService;
	
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public BookVO create(@RequestBody() BookVO bookVo) {
		System.out.println(bookVo.getPrice());
		return bookService.create(bookVo);
	}
	
	@GetMapping(value="/all")
	public List<Book> findAll() {
		return bookService.findAll();
	}
}

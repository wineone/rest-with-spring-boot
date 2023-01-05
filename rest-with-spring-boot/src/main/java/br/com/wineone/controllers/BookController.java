package br.com.wineone.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wineone.data.vo.v1.BookVO;
import br.com.wineone.services.BookServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


//@CrossOrigin
@RestController
@RequestMapping("api/book/v1")
@Tag(name = "Book",description = "Endpoint for manage the books")
public class BookController {

	
	@Autowired
	private BookServices bookService;
		
	
	@CrossOrigin(origins = {"http://localhost:8080"})
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "creates a book in the db", description = "create a book", tags = {"people"}, 
		responses = {
			@ApiResponse(
					description = "Created", responseCode = "201", 
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = BookVO.class)
						)	 
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public BookVO create(@RequestBody() BookVO bookVo) {
		System.out.println(bookVo.getPrice());
		return bookService.create(bookVo);
	}
	
	
	@CrossOrigin(origins = {"https://matheuslisboa"})
	@GetMapping(value="/all", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Finds all books", description = "Finds all books", tags = {"book"}, 
		responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
					content = { 
						@Content(
								mediaType = "application/json",
								array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
						) 
					}
				),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<PagedModel<EntityModel<BookVO>>>  findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
	) {
		var sortDirection = "asc".equalsIgnoreCase(direction) ? Direction.ASC : Direction.DESC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"title"));
		return ResponseEntity.ok(bookService.findAll(pageable));
	}
	
	@GetMapping(value="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@Operation(summary = "Finds a book", description = "finds a book by id", tags = {"book"}, 
		responses = {
			@ApiResponse(
					description = "Success", responseCode = "200", 
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = BookVO.class)
						)	 
			),
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public BookVO findById(@PathVariable("id") Long id) {
		return bookService.findById(id);
	}
	
	@DeleteMapping(value="/{id}")
	@Operation(summary = "delete book in the db", description = "delete book on the db", tags = {"book"}, 
		responses = {
			@ApiResponse(description = "No content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		bookService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "updates a book in the db", description = "update book in the db", tags = {"book"}, 
		responses = {
			@ApiResponse(
					description = "Success", responseCode = "200",
					content = 
						@Content(
							mediaType = "application/json",
							schema = @Schema(implementation = BookVO.class)
						)	 
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
		}
	)
	public BookVO updateBook(@RequestBody() BookVO bookVo) {
		return bookService.update(bookVo);
	}
}

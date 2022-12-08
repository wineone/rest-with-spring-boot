package br.com.wineone.unittests.mapper.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.wineone.models.Book;
import br.com.wineone.models.Person;
import br.com.wineone.data.vo.v1.BookVO;


public class MockBook {
	
	public Book mockEntity() {
		return this.mockEntity(0);
	}
	
	public BookVO mockVO() {
		return this.mockVO(0);
	}
	
	public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
        	books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
        	books.add(mockVO(i));
        }
        return books;
    }
	
	public Book mockEntity(Integer id) {
		Book book = new Book();
		Person p = new Person();
		p.setId(1);
		book.setAuthor(p);
		book.setId((long) id);
		book.setLaunchDate(null);
		book.setPrice((float) 999.099);
		book.setTitle("a viagem de chihiro");
		return book;
	}
	
	public BookVO mockVO(Integer id) {
		BookVO book = new BookVO();
		book.setId((long) id);
		book.setAuthorId(1L);
		book.setLaunchDate(null);
		book.setPrice((float) 999.099);
		book.setTitle("a viagem de chihiro");
		return book;
	}
}

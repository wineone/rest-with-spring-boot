package br.com.wineone.mapper.custom;

import br.com.wineone.data.vo.v1.BookVO;
import br.com.wineone.models.Book;
import br.com.wineone.models.Person;

public class BookMapper {

	
	public static BookVO convertEntityToVo(Book book) {
		BookVO vo = new BookVO();
		vo.setAuthorId(book.getAuthor().getId());
		vo.setLaunchDate(book.getLaunchDate());
		vo.setPrice(book.getPrice());
		vo.setTitle(book.getTitle());
		vo.setId(book.getId());
		return vo;
	}
	
	public static Book convertVoToEntity(BookVO bookVo, Person per) {
		Book enti = new Book();
		enti.setAuthor(per);
		if(!(bookVo.getId() == null))
			enti.setId(bookVo.getId());
		if(!(bookVo.getLaunchDate() == null))
			enti.setLaunchDate(bookVo.getLaunchDate());
		enti.setPrice(bookVo.getPrice());
		if(!(bookVo.getTitle() == null))
			enti.setTitle(bookVo.getTitle());
		return enti;
	}
}

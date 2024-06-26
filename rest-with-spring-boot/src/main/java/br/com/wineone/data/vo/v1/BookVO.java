package br.com.wineone.data.vo.v1;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class BookVO extends RepresentationModel<BookVO> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long authorId;
	private Date launchDate;
	private float price;
	private String title;
	
	public BookVO() {
		super();
	}
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authorId, launchDate, price, title);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookVO other = (BookVO) obj;
		return Objects.equals(authorId, other.authorId) && Objects.equals(launchDate, other.launchDate)
				&& Float.floatToIntBits(price) == Float.floatToIntBits(other.price)
				&& Objects.equals(title, other.title);
	}



	public Long getAuthorId() {
		return authorId;
	}



	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}



	public Date getLaunchDate() {
		return launchDate;
	}
	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}

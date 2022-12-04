package br.com.wineone.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="book")
@JsonIgnoreProperties({"author"})
public class Book {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_author_id")
	private Person author;
	
	@Id()
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private Long id;
	@Column(name="launch_date",nullable = false)
	private Date launchDate;
	@Column()
	private float price;
	@Column()
	private String title;
	
	
	public Person getAuthor() {
		return author;
	}
	public void setAuthor(Person author) {
		this.author = author;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

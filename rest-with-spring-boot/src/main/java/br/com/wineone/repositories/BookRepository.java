package br.com.wineone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wineone.models.Book;

@Repository()
public interface BookRepository extends JpaRepository<Book, Long> {}

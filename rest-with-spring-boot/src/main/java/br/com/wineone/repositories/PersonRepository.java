package br.com.wineone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wineone.models.Person;

@Repository()
public interface PersonRepository extends JpaRepository<Person, Long>{}

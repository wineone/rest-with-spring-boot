package br.com.wineone.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.wineone.models.User;

public interface UserRepository extends JpaRepository<User,Long>{
	
	@Query("SELECT u FROM User WHERE u.userName = :userName")
	public User findByUsername(@Param("userName") String userName);
}

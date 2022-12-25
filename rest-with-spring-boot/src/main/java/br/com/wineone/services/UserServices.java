package br.com.wineone.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.wineone.controllers.PersonController;
import br.com.wineone.data.vo.v1.UserVO;
import br.com.wineone.exceptions.ResourceNotFoundException;
import br.com.wineone.mapper.DozerMapper;
import br.com.wineone.models.User;
import br.com.wineone.repositories.UserRepository;

@Service
public class UserServices implements UserDetailsService {

	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	@Autowired
	private UserRepository userRespository;
	
	public UserVO findById(Long id) {
		logger.info("finding one User!");
		Optional<User> query =  userRespository.findById(id);
		UserVO vo = DozerMapper.parseObject(query.orElseThrow(() -> new ResourceNotFoundException("no records found for this id")), UserVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("finding user by name");
		
		User user = userRespository.findByUsername(username);
		
		if (user != null) {
			return user;
		}else {
			throw new UsernameNotFoundException("Username " +username+" not found!");
		}
	}	
}

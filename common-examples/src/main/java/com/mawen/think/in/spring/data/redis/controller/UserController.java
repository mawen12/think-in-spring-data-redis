package com.mawen.think.in.spring.data.redis.controller;

import java.util.List;

import com.mawen.think.in.spring.data.redis.repository.UserRepository;
import com.mawen.think.in.spring.data.redis.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/2
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	@Autowired
	private UserRepository userRepository;

	@PutMapping("/{key}")
	public void save(@PathVariable String key, @RequestBody User user) {
		redisTemplate.opsForValue().set(key, user);
	}

	@GetMapping("/{key}")
	public User get(@PathVariable String key) {
		return redisTemplate.opsForValue().get(key);
	}

	@DeleteMapping("/{key}")
	public Boolean delete(@PathVariable String key) {
		return redisTemplate.delete(key);
	}


	//================================================================================================
	// Repository
	//================================================================================================

	@PutMapping("/repo/batch")
	public void repoBatchSave(@RequestBody List<User> users) {
		userRepository.saveAll(users);
	}

	@PutMapping("/repo")
	public void repoSave(@RequestBody User user) {
		userRepository.save(user);
	}

	@GetMapping("/repo/{id}")
	public User repoGet(@PathVariable Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@PostMapping("/repo/ids")
	public List<User> repoList(@RequestBody List<Long> ids) {
		return userRepository.findAllById(ids);
	}

	@DeleteMapping("/repo/{id}")
	public void repoDelete(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
}

package com.slashcode.supportticket.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slashcode.supportticket.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	boolean existsByEmail(String email);
}
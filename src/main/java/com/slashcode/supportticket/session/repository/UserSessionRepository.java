package com.slashcode.supportticket.session.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.slashcode.supportticket.session.model.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID>{

}

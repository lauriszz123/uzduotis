package com.laurynas.uzduotis.repository;

import com.laurynas.uzduotis.api.models.SessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionModel, Long> {
    SessionModel findByToken(String token);
}

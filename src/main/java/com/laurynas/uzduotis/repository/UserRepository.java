package com.laurynas.uzduotis.repository;

import com.laurynas.uzduotis.api.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    void deleteByUsername(String username);
}
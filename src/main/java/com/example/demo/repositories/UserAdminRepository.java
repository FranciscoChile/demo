package com.example.demo.repositories;

import com.example.demo.entities.UserAdmin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAdminRepository extends CrudRepository<UserAdmin, Integer> {
    Optional<UserAdmin> findByEmail(String email);
}

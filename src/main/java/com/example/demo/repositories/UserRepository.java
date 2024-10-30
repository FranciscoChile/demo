package com.example.demo.repositories;



import com.example.demo.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    List<User> findByName(String name);

    List<User> findByEmail(String email);
}

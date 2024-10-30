package com.example.demo.repositories;


import com.example.demo.entities.Phone;
import org.springframework.data.repository.CrudRepository;

public interface PhoneRepository extends CrudRepository<Phone, Long> {
    
}

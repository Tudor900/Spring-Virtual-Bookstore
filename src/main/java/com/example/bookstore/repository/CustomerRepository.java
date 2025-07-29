package com.example.bookstore.repository;

import com.example.bookstore.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository

public interface CustomerRepository extends CrudRepository <Customer,Long>{

}

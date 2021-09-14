package com.leo.boot.jpa.repo;

import com.leo.boot.jpa.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CustomerRepository extends JpaRepository<Customer, String>, QuerydslPredicateExecutor<Customer> {

}
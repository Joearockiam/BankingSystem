package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, String>{

}

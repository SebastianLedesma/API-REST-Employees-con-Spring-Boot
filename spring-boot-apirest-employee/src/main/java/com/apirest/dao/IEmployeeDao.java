package com.apirest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apirest.entity.Employee;

@Repository
public interface IEmployeeDao extends JpaRepository<Employee, Long>{

}

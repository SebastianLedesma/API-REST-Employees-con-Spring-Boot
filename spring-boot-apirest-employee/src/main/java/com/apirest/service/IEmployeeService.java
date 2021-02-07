package com.apirest.service;

import java.util.List;

import com.apirest.entity.Employee;

public interface IEmployeeService {

	List<Employee> getAllEmployees();
	
	Employee saveEmployee(Employee employee);
	
	Employee getEmployeeById(Long id);
	
	void deleteEmployee(Long id);
	
}

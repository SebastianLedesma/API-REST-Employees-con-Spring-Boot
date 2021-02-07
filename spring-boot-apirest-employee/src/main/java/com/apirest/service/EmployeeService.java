package com.apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apirest.dao.IEmployeeDao;
import com.apirest.entity.Employee;

@Service
public class EmployeeService implements IEmployeeService {

	@Autowired
	private IEmployeeDao employeeDao;
	
	
	@Override
	@Transactional(readOnly=true)
	public List<Employee> getAllEmployees() {
		return this.employeeDao.findAll();
	}

	@Override
	public Employee saveEmployee(Employee employee) {
		return this.employeeDao.save(employee);
	}


	@Override
	@Transactional(readOnly=true)
	public Employee getEmployeeById(Long id) {
		return this.employeeDao.findById(id).orElse(null);
	}

	@Override
	public void deleteEmployee(Long id) {
		this.employeeDao.deleteById(id);
	}

}

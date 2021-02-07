package com.apirest.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.entity.Employee;
import com.apirest.service.IEmployeeService;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

	@Autowired
	private IEmployeeService employeeService;
	
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> showAll(){
		return ResponseEntity.status(HttpStatus.OK).body(this.employeeService.getAllEmployees());
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> showById(@PathVariable Long id){
		Employee employee = this.employeeService.getEmployeeById(id);
		
		if(employee != null) {
			return ResponseEntity.ok(employee);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PostMapping("/employees")
	public ResponseEntity<Employee> create(@RequestBody Employee employee) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.employeeService.saveEmployee(employee));
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> update(@RequestBody Employee employeeUpdate,@PathVariable Long id){
		Employee employee = this.employeeService.getEmployeeById(id);
		
		if(employee != null) {
			employee.setName(employeeUpdate.getName());
			employee.setRole(employeeUpdate.getRole());
			employee.setSalary(employeeUpdate.getSalary());
			return ResponseEntity.ok(this.employeeService.saveEmployee(employee));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		Employee employee = this.employeeService.getEmployeeById(id);
		
		if(employee != null) {
			this.employeeService.deleteEmployee(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
}

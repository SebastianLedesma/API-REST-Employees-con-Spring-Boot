package com.apirest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
	public ResponseEntity<?> showById(@PathVariable Long id){
		Employee employee = null;
		Map<String,Object> response = new HashMap<String, Object>();
		
		try {
			employee = this.employeeService.getEmployeeById(id);
		}catch(DataAccessException e) {
			response.put("Error", "Error al realizar la consulta en la base de datos.");
			response.put("Mensaje", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(employee == null) {
			response.put("mensaje", "El employee con ID ".concat(id.toString()).concat(" no existe."));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Employee>(employee,HttpStatus.OK);
	}
	
	
	
	@PostMapping("/employees")
	public ResponseEntity<?> create(@Valid @RequestBody Employee employee,BindingResult result) {
		Employee employeeNew = null;
		Map<String,Object> response = new HashMap<String,Object>();
		
		if(result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			for(FieldError err : result.getFieldErrors()) {
				errors.add("El campo '".concat(err.getField().concat("' ").concat(err.getDefaultMessage())));
			}
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			employeeNew = this.employeeService.saveEmployee(employee);
		}catch(DataAccessException e) {
			response.put("Error", "Error al realizar la inserción en la base de datos.");
			response.put("Mensaje", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El employee ha sido creado con éxito.");
		response.put("employee", employeeNew);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Employee employeeUpdate,BindingResult result,@PathVariable Long id){
		Employee employeeActual = this.employeeService.getEmployeeById(id);
		Employee employeeActualizado = null;
		Map<String,Object> response = new HashMap<String,Object>();
		
		if(result.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			for(FieldError err : result.getFieldErrors()) {
				errors.add("El campo '".concat(err.getField().concat("' ").concat(err.getDefaultMessage())));
			}
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		if(employeeActual == null) {
			response.put("error", "No existe el employee con ID ".concat(id.toString()).concat(" en la base de datos."));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			employeeActual.setName(employeeUpdate.getName());
			employeeActual.setRole(employeeUpdate.getRole());
			employeeActual.setSalary(employeeUpdate.getSalary());
			employeeActualizado = this.employeeService.saveEmployee(employeeActual);
			
		}catch(DataAccessException e) {
			response.put("Error", "Error al actualizar el employee en la base de datos.");
			response.put("Mensaje", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Se ha actualizado el employee con éxito.");
		response.put("employee", employeeActualizado);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		Map<String,Object> response = new HashMap<String,Object>();
		
		try {
			this.employeeService.getEmployeeById(id);
		}catch(DataAccessException e) {
			response.put("Error", "Error al elimnar el employee en la base de datos.");
			response.put("Mensaje", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Se ha eliminado al employee de la base de datos.");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		
	}
}

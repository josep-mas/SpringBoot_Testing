package com.jmas.springboottesting.service.impl;

import com.jmas.springboottesting.exception.ResourceNotFoundException;
import com.jmas.springboottesting.model.Employee;
import com.jmas.springboottesting.repository.EmployeeRepository;
import com.jmas.springboottesting.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl( EmployeeRepository employeeRepository ) {
        this.employeeRepository= employeeRepository;
    }

    @Override
    public Employee saveEmployee( Employee employee ) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee already exists with given email:" + employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById( long id ) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee( Employee employee ) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee( long id ) {
        employeeRepository.deleteById(id);
    }

}

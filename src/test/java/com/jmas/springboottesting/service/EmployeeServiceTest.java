package com.jmas.springboottesting.service;

import com.jmas.springboottesting.exception.ResourceNotFoundException;
import com.jmas.springboottesting.model.Employee;
import com.jmas.springboottesting.repository.EmployeeRepository;
import com.jmas.springboottesting.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    @BeforeEach
    public void setup(){
        // employeeRepository = Mockito.mock(EmployeeRepository.class);
        // employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Perico")
                .lastName("Palotes")
                .email("perico@palotes.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){

        //-- Given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //-- When
        Employee employeeBD = employeeService.saveEmployee(employee);

        //-- Then
        assertThat(employeeBD).isNotNull();
    }

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){

        //-- Given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //-- When
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        //-- Then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){

        //-- Given
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Pablo")
                .lastName("Picapiedra")
                .email("pablo@email.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        //-- When
        List<Employee> employeeList = employeeService.getAllEmployees();

        //-- Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){

        //-- Given
        given(employeeRepository.findAll()).willReturn(List.of());

        //-- When
        List<Employee> employeeList = employeeService.getAllEmployees();

        //-- Then
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){

        //-- Given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //-- When
        Optional<Employee> optEmployee = employeeService.getEmployeeById(employee.getId());

        //-- Then
        assertTrue(optEmployee.isPresent());
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        //-- Given
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("anselm@gmail.com");
        employee.setFirstName("Anselm");

        //-- When
        Employee employeeBD = employeeService.updateEmployee(employee);

        //-- Then
        assertThat(employeeBD).isNotNull();
        assertThat(employeeBD.getEmail()).isEqualTo("anselm@gmail.com");
        assertThat(employeeBD.getFirstName()).isEqualTo("Anselm");
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){

        //-- Given
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        //-- When
        employeeService.deleteEmployee(employeeId);

        //-- Then
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

}
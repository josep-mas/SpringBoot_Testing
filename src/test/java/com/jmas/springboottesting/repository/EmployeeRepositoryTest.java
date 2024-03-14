package com.jmas.springboottesting.repository;

import com.jmas.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        this.employee = Employee.builder()
                .firstName("Perico")
                .lastName("Palotes")
                .email("perico@palotes.com")
                .build();
    }
    
    @DisplayName("JUnit test for save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //-- Given

        //-- When
        Employee savedEmployee =  employeeRepository.save(employee);

        //-- Then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNegative();
    }

    @Test
    void givenEmployeesList_whenFindAll_thenEmployeesList(){

        //-- Given
        Employee employee1 = Employee.builder()
                .firstName("Anselm")
                .lastName("Clave")
                .email("anselm@clave.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //-- When
        List<Employee> employeeList = employeeRepository.findAll();

        //-- Then
        assertThat(employeeList).isNotNull()
            .hasSize(2);
    }

    @DisplayName("JUnit test for get employee by id operation")
    @Test
    void givenEmployee_whenFindById_thenReturnEmployeeObject(){

        //-- Given
        employeeRepository.save(employee);

        //-- When
        Optional<Employee> employeeDB = employeeRepository.findById(employee.getId());

        //-- Then
        assertThat(employeeDB).isPresent();
    }

    @Test
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){

        //-- Given
        employeeRepository.save(employee);

        //-- When
        Optional<Employee> employeeDB = employeeRepository.findByEmail("perico@palotes.com");

        //-- Then
        assertThat(employeeDB).isPresent();
    }

    @Test
    void givenEmployeeObject_whenUpdateemployee_thenReturnUpdatedEmployee(){

        //-- Given
        employeeRepository.save(employee);

        //-- When
        employee.setLastName("Palotes2");
        Employee employeeDB = employeeRepository.save(employee);

        //-- Then
        assertThat(employeeDB.getLastName()).isEqualTo("Palotes2");
    }


    @Test
    void givenEmployeeObject_whenDelete_thenRemoveEmployee(){

        //-- Given
        employeeRepository.save(employee);

        //-- When
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //-- Then
        assertThat(employeeOptional).isEmpty();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){

        //-- Given
        employeeRepository.save(employee);
        String firstName = "Perico";
        String lastName = "Palotes";

        //-- When
        Employee employeeDB = employeeRepository.findByJPQL(firstName, lastName );

        //-- Then
        assertThat(employeeDB).isNotNull();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){

        //-- Given
        employeeRepository.save(employee);
        String firstName = "Perico";
        String lastName = "Palotes";

        //-- When
        Employee employeeDB = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        //-- Then
        assertThat(employeeDB).isNotNull();
    }

    @Test
    void givenFirstNameAndLastName_whenFindByNativeSPQL_thenReturnEmployeeObject(){

        //-- Given
        employeeRepository.save(employee);
        String firstName = "Perico";
        String lastName = "Palotes";

        //-- When
        Employee employeeDB = employeeRepository.findByNativeSQL(firstName, lastName );

        //-- Then
        assertThat(employeeDB).isNotNull();
    }

    @Test
    void givenFirstNameAndLastName_whenFindBySQLNamedParams_thenReturnEmployeeObject(){

        //-- Given
        employeeRepository.save(employee);
        String firstName = "Perico";
        String lastName = "Palotes";

        //-- When
        Employee employeeDB = employeeRepository.findByNativeSQLNamed(firstName, lastName);

        //-- Then
        assertThat(employeeDB).isNotNull();
    }
}
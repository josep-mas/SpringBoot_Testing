package com.jmas.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmas.springboottesting.model.Employee;
import com.jmas.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //-- given
        Employee employee = Employee.builder()
                .firstName("Perico")
                .lastName("Palotes")
                .email("perico@mail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //-- when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //-- then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

        //-- given
        List<Employee> lstEmployee = new ArrayList<>();
        lstEmployee.add(Employee.builder().firstName("Perico").lastName("Palotes").email("perico@mail.com").build());
        lstEmployee.add(Employee.builder().firstName("Anselm").lastName("Clave").email("anselm@mail.com").build());
        given(employeeService.getAllEmployees()).willReturn(lstEmployee);

        //-- when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //-- then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(lstEmployee.size())));

    }


    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //-- given
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Perico")
                .lastName("Palotes")
                .email("perico@mail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //-- when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //-- then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void givenImvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //-- given
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //-- when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //-- then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

}
package com.example.demo3.mapper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class EmployeeMapperTest {

    @Autowired
    EmployeeMapper employeeMapper;
    @Test
    public void getEmpById() {
        employeeMapper.selectByIdAll();
        System.out.println(1);
    }

    @Test
    public void insertEmp() {
    }

    @Test
    public void selectByIdAll() {
    }
}
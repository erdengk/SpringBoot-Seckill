package com.example.demo3.mapper;

import com.example.demo3.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void insertEmp(Employee employee);

    public List<Employee> selectByIdAll();
}

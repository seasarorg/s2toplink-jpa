package example.dao;

import java.util.List;

import example.dto.EmpDto;
import example.entity.Employee;

public interface EmployeeDao {
    Employee find(Integer id);
    List<Employee> findAll();
    List<Employee> findByName(String name);
    List<Employee> findByExample(Employee employee);
    List<EmpDto> findByNameSql(String name);
}

package lambdaspecial.java.hr;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();

    List<DepartmentDto> getAllDepartments();

    List<EmployeeDto> getEmployeesByDepartment(String departmentId);

    List<EmployeeDto> getEmployeesByDepartment(DepartmentDto department);

    Optional<DepartmentDto> getDepartmentById(String departmentId);

}

package lambdaspecial.java.hr;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {

    @Test
    public void testFlatMap() {

        EmployeeService employeeService = new MockEmployeeService();

        // SELECT E.name || '(' || D.name || ')' employeeName
        // FROM employee E,
        //      department D
        // WHERE E.departmentId = D.id
        // ORDER BY 1

        List<DepartmentDto> departments = employeeService.getAllDepartments();
        List<String> employees = departments.stream()
                .flatMap((depart) ->
                        employeeService.getEmployeesByDepartment(depart).stream())
                .map((empl) ->
                        empl.getName() + " (" + empl.getDepartment().getName() + ")")
                .sorted()
                .toList();

        List<String> expected = List.of(
                "Alice (HR)",
                "Bob (HR)",
                "Charlie (Engineering)"
        );
        assertEquals(expected, employees);
    }

    @Test
    public void testMapMulti() {

        EmployeeService employeeService = new MockEmployeeService();

        // SELECT E.name || '(' || D.name || ')' employeeName
        // FROM employee E,
        //      department D
        // WHERE E.departmentId = D.id
        // ORDER BY 1

        List<DepartmentDto> departments = employeeService.getAllDepartments();
        List<String> employees = departments.stream()
                .<EmployeeDto>mapMulti((depart, consumer) -> {
                    employeeService.getEmployeesByDepartment(depart)
                            .forEach(consumer);
                })
                .map(empl -> empl.getName() + " (" + empl.getDepartment().getName() + ")")
                .sorted()
                .toList();

        List<String> expected = List.of(
                "Alice (HR)",
                "Bob (HR)",
                "Charlie (Engineering)"
        );
        assertEquals(expected, employees);
    }
}

class MockEmployeeService implements EmployeeService {

    private static final List<DepartmentDto> DEPARTMENTS = List.of(
            DepartmentDto.builder().id("D1").name("HR").build(),
            DepartmentDto.builder().id("D2").name("Engineering").build()
    );

    private static final List<EmployeeDto> EMPLOYEES = List.of(
            EmployeeDto.builder().id("1").name("Alice").departmentId("D1").build(),
            EmployeeDto.builder().id("2").name("Bob").departmentId("D1").build(),
            EmployeeDto.builder().id("3").name("Charlie").departmentId("D2").build()
    );

    static {
        // Set department for each employee
        EMPLOYEES.forEach(employee -> {
            DEPARTMENTS.stream()
                .filter(department -> department.getId().equals(employee.getDepartmentId()))
                .findFirst()
                .ifPresent(employee::setDepartment);
        });
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return Collections.unmodifiableList(EMPLOYEES);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return Collections.unmodifiableList(DEPARTMENTS);
    }

    @Override
    public List<EmployeeDto> getEmployeesByDepartment(String departmentId) {
        return getAllEmployees().stream()
                .filter(e -> e.getDepartmentId().equals(departmentId))
                .toList();
    }

    @Override
    public List<EmployeeDto> getEmployeesByDepartment(DepartmentDto department) {
        return getEmployeesByDepartment(department.getId());
    }

    @Override
    public Optional<DepartmentDto> getDepartmentById(String departmentId) {
        return getAllDepartments().stream()
                .filter(d -> d.getId().equals(departmentId))
                .findFirst();
    }
}

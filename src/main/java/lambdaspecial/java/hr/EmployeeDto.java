package lambdaspecial.java.hr;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmployeeDto {

    private final String id;
    private final String name;
    private final String departmentId;
    private DepartmentDto department;

}

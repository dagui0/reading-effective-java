package lambdaspecial.java.hr;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DepartmentDto {

    private final String id;
    private final String name;
    private List<EmployeeDto> employees;

}

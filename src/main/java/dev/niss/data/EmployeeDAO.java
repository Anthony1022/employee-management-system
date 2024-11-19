package dev.niss.data;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import dev.niss.App;
import dev.niss.models.Department;
import dev.niss.models.Employee;
import dev.niss.models.Job;
import dev.sol.db.DBService;
import dev.sol.util.CoreLocalDateUtils;
import javafx.collections.ObservableList;

public class EmployeeDAO {
    public static final String TABLE = "employee";
    public static final DBService DB = App.DB_EMPLOYEE;

    private static final ObservableList<Department> departmentlist = App.COLLECTIONS_REGISTER.getList("DEPARTMENT");

    private Employee data(CachedRowSet crs) {
        try {
            String id = crs.getString("emp_id");
            String name = crs.getString("emp_name");
            Job job = Job.valueOf(crs.getString("job_name").toUpperCase());
            LocalDate hireDate = CoreLocalDateUtils.parse(
                    crs.getString("hire_date"), "yyyy-MM-dd");
            long salary = crs.getLong("salary");
            long commission = crs.getLong("commission");
            Department department = departmentlist.stream()
                    .filter(dept -> {
                        try {
                            return dept.getDep_ID().equals(crs.getString("dep_id"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }).findFirst().get();

            return new Employee(id, name, job, hireDate, salary, commission, department);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Employee> getEmployeeList() {
        CachedRowSet crs = DB.select_all(TABLE);
        List<Employee> list = new LinkedList<>();

        try {
            while (crs.next()) {
                Employee employee = data(crs);
                if (employee != null)
                    list.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        list.forEach(employee -> {
            String manager_id = employee.getManager().getEmp_ID();
            employee.setManager(
                    list.stream()
                            .filter(e -> e.getEmp_ID().equals(manager_id))
                            .findFirst().get());
            employee.rebaseline();
        });

        return list;
    }
}

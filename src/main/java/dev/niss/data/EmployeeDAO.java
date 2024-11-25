package dev.niss.data;

import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat.Style;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import dev.niss.App;
import dev.niss.models.Department;
import dev.niss.models.Employee;
import dev.niss.models.Job;
import dev.sol.db.DBParam;
import dev.sol.db.DBService;
import dev.sol.util.CoreDateUtils;

import javafx.collections.ObservableList;

public class EmployeeDAO {
    public static final String TABLE = "employee";
    public static final DBService DB = App.DB_EMPLOYEE;

    private static final ObservableList<Department> departmentlist = App.COLLECTIONS_REGISTER.getList("DEPARTMENT");

    public static Employee data(CachedRowSet crs) {
        try {
            String id = crs.getString("emp_id");
            String name = crs.getString("emp_name");
            Job job = Job.valueOf(crs.getString("job_name").toUpperCase().trim());
            Employee manager = new Employee(crs.getString("manager_id"));
            LocalDate hireDate = CoreDateUtils.parse(
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

            return new Employee(id, name, job, manager, hireDate, salary, commission, department);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DBParam[] paramList(Employee employee) {
        List<DBParam> paramList = new LinkedList<>();
        paramList.add(new DBParam(Types.VARCHAR, "emp_id", employee.getEmp_ID()));
        paramList.add(new DBParam(Types.VARCHAR, "emp_name", employee.getName()));
        paramList.add(new DBParam(Types.VARCHAR, "job_name", employee.getJob()));
        paramList.add(new DBParam(Types.VARCHAR, "manager_id", employee.getManager().getEmp_ID()));
        paramList.add(
                new DBParam(Types.VARCHAR, "hire_date", CoreDateUtils.format(employee.getHire_Date(), "yyyy-MM-dd")));
        paramList.add(new DBParam(Types.BIGINT, "salary", employee.getSalary()));
        paramList.add(new DBParam(Types.BIGINT, "commission", employee.getCommision()));
        paramList.add(new DBParam(Types.VARCHAR, "dep_id", employee.getDepartment().getDep_ID()));
        return paramList.toArray(new DBParam[0]);

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
            if (!manager_id.isEmpty()) {
                employee.setManager(
                        list.stream()
                                .filter(e -> e.getEmp_ID().equals(manager_id))
                                .findFirst().get());
                employee.rebaseline();
            }
        });

        return list;
    }

    public static void insert(Employee employee) {

        DB.insert(TABLE, paramList(employee));
    }
    public static void delete(Employee employee){
        DB.insert(TABLE, new DBParam(Types.VARCHAR, "emp_id", employee.getEmp_ID()));
    }
    public static void update(Employee employee){
      DB.update(TABLE, new DBParam(Types.VARCHAR, "emp_id", employee.getEmp_ID())
      ,new DBParam(Types.VARCHAR, "manager_id", employee.getManager().getEmp_ID()));
      
    }
}
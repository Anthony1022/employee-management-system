package dev.niss.models;

import java.time.LocalDate;

import dev.sol.core.application.FXModel;
import dev.sol.core.properties.beans.FXLongProperty;
import dev.sol.core.properties.beans.FXObjectProperty;
import dev.sol.core.properties.beans.FXStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class Employee extends FXModel {

    public static class Department_TABLECELL extends TableCell<Department, Employee> {
        @Override
        protected void updateItem(Employee item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                return;
            }
            setGraphic(new Label(item.getDepartment().getName()));
        }

    }

    private FXStringProperty emp_id;
    private FXStringProperty name;
    private FXObjectProperty<Job> job;
    private FXObjectProperty<Employee> manager;
    private FXObjectProperty<LocalDate> hire_date;
    private FXLongProperty salary;
    private FXLongProperty commission;
    private FXObjectProperty<Department> department;

    public Employee(String emp_id, String name, Job job, LocalDate hire_date, Long salary, Long commission,
            Department department) {
        this.emp_id = new FXStringProperty(emp_id);
        this.name = new FXStringProperty(name);
        this.job = new FXObjectProperty<Job>(job);
        this.manager = new FXObjectProperty<Employee>();
        this.hire_date = new FXObjectProperty<LocalDate>(hire_date);
        this.salary = new FXLongProperty(salary);
        this.commission = new FXLongProperty(commission);
        this.department = new FXObjectProperty<Department>(department);

        track_properties(this.emp_id,
                this.name,
                this.job,
                this.manager,
                this.hire_date,
                this.salary,
                this.commission,
                this.department);

    }

    public FXStringProperty emp_IDProperty() {
        return emp_id;
    }

    public String getEmp_ID() {
        return emp_IDProperty().get();
    }

    public void setEmp_ID(String emp_id) {
        emp_IDProperty().set(getEmp_ID());
    }

    public FXStringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        nameProperty().set(getName());
    }

    public FXObjectProperty<Job> jobProperty() {
        return job;
    }

    public Job getJob() {
        return jobProperty().get();
    }

    public void setJob(Job job) {
        jobProperty().set(getJob());
    }

    public FXObjectProperty<Employee> managerProperty() {
        return manager;
    }

    public Employee getManager() {
        return managerProperty().get();
    }

    public void setManager(Employee manager) {
        managerProperty().set(getManager());
    }

    public FXObjectProperty<LocalDate> hire_dateProperty() {
        return hire_date;
    }

    public LocalDate getHire_Date() {
        return hire_dateProperty().get();
    }

    public void setHire_Date(LocalDate hire_date) {
        hire_dateProperty().set(getHire_Date());
    }

    public FXLongProperty salaryProperty() {
        return salary;
    }

    public long getSalary() {
        return salaryProperty().get();
    }

    public void setSalary(Long salary) {
        salaryProperty().set(getSalary());
    }

    public FXLongProperty commissionProperty() {
        return commission;
    }

    public long getCommision() {
        return commissionProperty().get();
    }

    public void setCommission(long commission) {
        commissionProperty().set(getCommision());
    }

    public FXObjectProperty<Department> departmentProperty() {
        return department;
    }

    public Department getDepartment() {
        return departmentProperty().get();
    }

    public void setDepartment(Department department) {
        departmentProperty().set(getDepartment());
    }

    @Override
    public FXModel clone() {
        Employee employee = new Employee(getEmp_ID(), getName(), getJob(), getHire_Date(), getSalary(), getCommision(),
                getDepartment());
        if (getManager() != null)
            employee.setManager(getManager());
        return employee;
    }

    @Override
    public void copy(FXModel arg0) {
        Employee c = (Employee) arg0;

        setEmp_ID(c.getEmp_ID());
        setName(c.getName());
        setJob(c.getJob());
        setManager(c.getManager());
        setHire_Date(getHire_Date());
        setDepartment(getDepartment());
        setSalary(getSalary());
        setCommission(getCommision());
    }

}

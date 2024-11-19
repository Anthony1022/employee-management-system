package dev.niss.app;

import dev.niss.App;
import dev.niss.data.DepartmentDAO;
import dev.niss.models.Department;
import dev.niss.models.Employee;
import dev.niss.models.Job;
import dev.sol.core.application.FXController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RootController extends FXController {
    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<String, Employee> empIDColumn;
    @FXML
    private TableColumn<String, Employee> empFirstNameColumn;
    @FXML
    private TableColumn<String, Employee> empLastNamecolumn;
    @FXML
    private TableColumn<Job, Employee> empJobcolumn;
    @FXML
    private TableColumn<Employee, Employee> empManagerColumn;
    @FXML
    private TableColumn<Department, Employee> departmentcolumn;

    @FXML
    private ComboBox<Department> departmentDropdown;

    private ObservableList<Department> department_masterlist;
    private ObservableList<Employee> employee_masterlist;

    @Override
    protected void load_bindings() {
        employee_masterlist = FXCollections.observableArrayList();
        department_masterlist = App.COLLECTIONS_REGISTER.getList("DEPARTMENT");

        departmentDropdown.setButtonCell(new Department.LIST_CELL());
        departmentDropdown.setCellFactory(cell -> new Department.LIST_CELL());
        departmentDropdown.setItems(department_masterlist);

        departmentcolumn.setCellFactory(cell -> new Employee.Department_TABLECELL());
    }

    @Override
    protected void load_fields() {

    }

    @Override
    protected void load_listeners() {
        departmentDropdown.getSelectionModel().selectFirst();
    }
}
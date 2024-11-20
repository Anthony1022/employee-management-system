package dev.niss.app;

import dev.niss.App;
import dev.niss.models.Department;
import dev.niss.models.Employee;
import dev.niss.models.Job;
import dev.sol.core.application.FXController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class RootController extends FXController {
    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> empIDColumn;
    @FXML
    private TableColumn<Employee, String> empNameColumn;
    @FXML
    private TableColumn<Employee, Job> empJobcolumn;
    @FXML
    private TableColumn<Employee, Employee> empManagerColumn;
    @FXML
    private TableColumn<Employee, Department> departmentcolumn;

    @FXML
    private ComboBox<Job> jobField;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Employee> managerField;

    @FXML
    private ComboBox<Department> departmentField;

    @FXML
    TextField filterEmployeeField;

    @FXML
    private ComboBox<Employee> newManagerField;

    private ObservableList<Department> department_masterlist;
    private ObservableList<Employee> employee_masterlist;
    private FilteredList<Employee> managerlist;

    private static class MANAGER_CELL extends ListCell<Employee> {

        @Override
        protected void updateItem(Employee item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);
                setGraphic(null);
                return;
            }
            setGraphic(new Label(item.getName()));
        };

    }

    @Override
    protected void load_bindings() {
        employee_masterlist = App.COLLECTIONS_REGISTER.getList("EMPLOYEE");
        department_masterlist = App.COLLECTIONS_REGISTER.getList("DEPARTMENT");

        managerlist = new FilteredList<>(employee_masterlist, employeee -> {
            return employeee.getJob() == Job.PRESIDENT || employeee.getJob() == Job.MANAGER;
        });
        managerField.setButtonCell(new MANAGER_CELL());
        managerField.setCellFactory(cell -> new MANAGER_CELL());
        managerField.setItems(managerlist);

        newManagerField.setButtonCell(new MANAGER_CELL());
        newManagerField.setCellFactory(cell -> new MANAGER_CELL());
        newManagerField.setItems(managerlist);

        ObservableList<Job> joblList = FXCollections.observableArrayList(Job.values());
        if (employee_masterlist.stream().anyMatch(e -> e.getJob().equals(Job.PRESIDENT))) {
            jobField.setItems(FXCollections.observableArrayList(joblList.subList(1, joblList.size())));
        } else
            jobField.setItems(joblList);

        departmentField.setButtonCell(new Department.LIST_CELL());
        departmentField.setCellFactory(cell -> new Department.LIST_CELL());
        departmentField.setItems(department_masterlist);

        empIDColumn.setCellValueFactory(cell -> cell.getValue().emp_IDProperty());
        empNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        empJobcolumn.setCellValueFactory(cell -> cell.getValue().jobProperty());

        empManagerColumn.setCellFactory(cell -> {
            TableCell<Employee, Employee> tableCell = new TableCell<>() {
                @Override
                protected void updateItem(Employee item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }

                    setGraphic(new Label(item.getName()));
                }

            };
            return tableCell;
        });
        empManagerColumn.setCellValueFactory(cell -> cell.getValue().managerProperty());

        departmentcolumn.setCellFactory(cell -> new Employee.Department_TABLECELL());
        departmentcolumn.setCellValueFactory(cell -> cell.getValue().departmentProperty());

        employeeTable.setItems(employee_masterlist);
    }

    @Override
    protected void load_fields() {
    }

    @Override
    protected void load_listeners() {
        managerField.getSelectionModel().selectFirst();
        jobField.getSelectionModel().selectFirst();
        departmentField.getSelectionModel().selectFirst();

        newManagerField.getSelectionModel().selectFirst();
    }
}
package dev.niss.app;

import java.util.Collections;
import java.util.Comparator;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import dev.niss.App;
import dev.niss.data.EmployeeDAO;
import dev.niss.models.Department;
import dev.niss.models.Employee;
import dev.niss.models.Job;
import dev.sol.core.application.FXController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

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

    @FXML
    private void handleAddEmployee() {
        if (nameField.getText().isEmpty()) {
            nameField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            Animations.flash(nameField).playFromStart();
            return;
        }
        Collections.sort(employee_masterlist, Comparator.comparing(Employee::getEmp_ID));
        int id_int = Integer.parseInt(employee_masterlist.getLast().getEmp_ID()) + 1;
        String emp_id = Integer.toString(id_int);

        Employee employee = new Employee(emp_id,
                nameField.getText(),
                jobField.getValue(),
                managerField.getValue(),
                departmentField.getValue());
        EmployeeDAO.insert(employee);
        employee_masterlist.add(employee);
        reset_newEmployeeFields();
    }

    @FXML
    private void handleSearchEmployee() {
        if (filterEmployeeField.getText().isEmpty()) {
            filterEmployeeField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            Animations.flash(filterEmployeeField).playFromStart();
        }
        employeeFilteredlist.setPredicate(employee -> {
            return employee.getEmp_ID().trim().toUpperCase().equals(filterEmployeeField.getText());
        });
    }

    @FXML
    private void handleAllSeacrh() {
        employeeFilteredlist.setPredicate(p -> true);
    }

    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Employee Delete Error");
            alert.setHeaderText("Null Selection Error Occurred");
            alert.setContentText("No employee selected from table. Must select employee to delete");
            alert.initOwner(scene.getWindow());
            alert.show();
            return;
        }
        if (employee_masterlist.stream().anyMatch(e -> {
            return e.getManager().getEmp_ID().equals(selectedEmployee.getEmp_ID());
        })) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Employee Delete Error");
            alert.setHeaderText("Selection Error Occurred");
            alert.setContentText("Employee selected from table. Must delete");
            alert.initOwner(scene.getWindow());
            alert.show();
            return;
        }
        employee_masterlist.remove(selectedEmployee);
        EmployeeDAO.delete(selectedEmployee);
    }

    @FXML
    private void handleUpdateEmployee() {
        if (newManagerField.getValue() == null) {
            newManagerField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            Animations.flash(departmentField);
        }

        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Update Employee");
            alert.setHeaderText("Null Selection Error Occurred");
            alert.setContentText("No employee selected from table. Must select employee to delete");
            alert.initOwner(scene.getWindow());
            alert.show();
            return;
        }
        selectedEmployee.setManager(newManagerField.getValue());
        EmployeeDAO.update(selectedEmployee);

    }

    private Scene scene;

    private ObservableList<Department> department_masterlist;
    private ObservableList<Employee> employee_masterlist;

    private FilteredList<Employee> managerlist;
    private FilteredList<Employee> employeeFilteredlist;

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
        }

    }

    @Override
    protected void load_fields() {
        scene = (Scene) getParameter("SCENE");
        employee_masterlist = App.COLLECTIONS_REGISTER.getList("EMPLOYEE");
        department_masterlist = App.COLLECTIONS_REGISTER.getList("DEPARTMENT");
        employeeFilteredlist = new FilteredList<>(employee_masterlist, p -> true);

        managerlist = new FilteredList<>(employee_masterlist, employeee -> {
            return employeee.getJob() == Job.PRESIDENT || employeee.getJob() == Job.MANAGER
                    || employeee.getJob() == Job.ANALYST;
        });
        managerField.setButtonCell(new MANAGER_CELL());
        managerField.setCellFactory(cell -> new MANAGER_CELL());
        managerField.setItems(managerlist);

        newManagerField.setButtonCell(new MANAGER_CELL());
        newManagerField.setCellFactory(cell -> new MANAGER_CELL());
        newManagerField.getItems().add(null);
        newManagerField.getItems().addAll(managerlist);

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

        employeeTable.setItems(employeeFilteredlist);
    }

    @Override
    protected void load_bindings() {

    }

    @Override
    protected void load_listeners() {
        managerField.getSelectionModel().selectFirst();
        jobField.getSelectionModel().selectFirst();
        departmentField.getSelectionModel().selectFirst();
        nameField.textProperty().addListener((o, ov, nv) -> {
            nameField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        });

        filterEmployeeField.textProperty().addListener((o, ov, nv) -> {
            filterEmployeeField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        });
        newManagerField.valueProperty().addListener((o, ov, nv) -> {
            newManagerField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        });

        employeeTable.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
            newManagerField.setValue(nv.getManager());
        });

    }

    private void reset_newEmployeeFields() {
        nameField.setText("");
        jobField.getSelectionModel().selectFirst();
        managerField.getSelectionModel().selectFirst();
        departmentField.getSelectionModel().selectFirst();
    }
}
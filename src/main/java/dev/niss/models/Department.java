package dev.niss.models;

import dev.sol.core.application.FXModel;
import dev.sol.core.properties.beans.FXStringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class Department extends FXModel {
    public static class LIST_CELL extends ListCell<Department> {
        @Override
        protected void updateItem(Department department, boolean empty) {
            super.updateItem(department, empty);

            if (department == null || empty) {
                setText(null);
                setGraphic(null);
                return;
            }
            setGraphic(new Label(department.getName()));
        }
    }

    private FXStringProperty dep_id;
    private FXStringProperty name;
    private FXStringProperty location;

    public Department(String dep_id, String name, String location) {
        this.dep_id = new FXStringProperty(dep_id);
        this.name = new FXStringProperty(name);
        this.location = new FXStringProperty(location);

        track_properties(this.dep_id, this.name, this.location);
    }

    public FXStringProperty dep_IDProperty() {
        return dep_id;
    }

    public String getDep_ID() {
        return dep_IDProperty().get();
    }

    public void setDep_ID(String dep_id) {
        dep_IDProperty().set(getDep_ID());
    }

    public FXStringProperty NameProperty() {
        return name;
    }

    public String getName() {
        return NameProperty().get();
    }

    public void setName(String name) {
        NameProperty().set(getDep_ID());
    }

    public FXStringProperty locationProperty() {
        return location;
    }

    public String getLocation() {
        return locationProperty().get();
    }

    public void setLocation(String location) {
        locationProperty().set(getDep_ID());
    }

    @Override
    public FXModel clone() {
        return new Department(getDep_ID(), getName(), getLocation());
    }

    @Override
    public void copy(FXModel arg0) {
        Department c = (Department) arg0;

        setDep_ID(c.getDep_ID());
        setName(c.getName());
        setLocation(c.getLocation());
    }

}

package dev.niss;

import dev.niss.app.RootLoader;
import dev.niss.data.DepartmentDAO;
import dev.niss.data.EmployeeDAO;
import dev.sol.core.application.FXApplication;
import dev.sol.core.application.loader.FXLoaderFactory;
import dev.sol.core.registry.FXCollectionsRegister;
import dev.sol.core.registry.FXControllerRegister;
import dev.sol.core.registry.FXNodeRegister;
import dev.sol.core.scene.FXSkin;
import dev.sol.core.thread.FXThreadService;
import dev.sol.db.DBService;
import javafx.collections.FXCollections;

public class App extends FXApplication {
    // App Registers
    public static final FXControllerRegister CONTROLLER_REGISTER = FXControllerRegister.INSTANCE;
    public static final FXCollectionsRegister COLLECTIONS_REGISTER = FXCollectionsRegister.INSTANCE;
    public static final FXNodeRegister NODE_REGISTER = FXNodeRegister.INSTANCE;

    public static final FXThreadService THREAD_SERVICE = FXThreadService.INSTANCE;

    public static final DBService DB_EMPLOYEE = DBService.INSTANCE
            .initialize("jdbc:mysql://localhost/employee?user=root&passwords=");

    @Override
    public void initialize() throws Exception {
        setTitle("EmployeeFX JDBC");
        setSkin(FXSkin.DRACULA);
        applicationStage.setResizable(false);

        _initialize_datatest();
        _initialize_application();
    }

    private void _initialize_datatest() {
        COLLECTIONS_REGISTER.register("DEPARTMENT",
                FXCollections.observableArrayList(DepartmentDAO.getDepartmentList()));
        COLLECTIONS_REGISTER.register("EMPLOYEE", FXCollections.observableArrayList(EmployeeDAO.getEmployeeList()));
    }

    private void _initialize_application() {
        RootLoader rootLoader = (RootLoader) FXLoaderFactory.createInstance(RootLoader.class,
                App.class.getResource("/dev/niss/app/ROOT.fxml"))
                .addParameter("scene", applicationScene)
                .initialize();
        rootLoader.load();
    }
}

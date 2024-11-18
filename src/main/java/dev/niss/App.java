package dev.niss;

import dev.niss.app.RootLoader;
import dev.sol.core.application.FXApplication;
import dev.sol.core.application.loader.FXLoaderFactory;
import dev.sol.core.scene.FXSkin;
import dev.sol.db.DBService;

public class App extends FXApplication {
    public static final DBService DB_EMPLOYEE = DBService.INSTANCE
            .initialize("jdbc:mysql://localhost/employee?user=root&passwords=");

    @Override
    public void initialize() throws Exception {
        setTitle("EmployeeFX JDBC");
        setSkin(FXSkin.DRACULA);

        _initialize_application();
    }

    private void _initialize_application() {
        RootLoader rootLoader = (RootLoader) FXLoaderFactory.createInstance(RootLoader.class,
                App.class.getResource("/dev/niss/app/ROOT.fxml"))
                .addParameter("scene", applicationScene)
                .initialize();
        rootLoader.load();
    }
}

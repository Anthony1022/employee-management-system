package dev.niss.app;

import java.io.IOException;

import dev.niss.App;
import dev.sol.core.application.loader.FXLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class RootLoader extends FXLoader {

    @Override
    public void load() {
        Scene scene = (Scene) params.get("scene");
        try {
            Parent root = loader.load();
            scene.setRoot(root);

            RootController controller = loader.getController();
            App.CONTROLLER_REGISTER.register("ROOT", controller);
            controller.load();

            controller.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

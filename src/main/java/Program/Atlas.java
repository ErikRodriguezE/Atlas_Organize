package Program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Atlas extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(Atlas.class.getResource("/inicio.fxml"));
        // Crear la escena con el contenido del FXML
        Scene scene = new Scene(fxmlLoader.load(),  349, 250 ); // ancho, alto
        // Configurar la ventana
        primaryStage.setTitle("Atlas Organize");
        primaryStage.setScene(scene);
        // Mostrar la ventana
        primaryStage.show();
    }//es donde inicia la aplicacion

    public static void main(String[] args) {
        launch(args);
    }

}

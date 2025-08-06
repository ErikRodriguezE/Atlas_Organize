package UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.nio.file.Path;

public class inicioControl {

    Path ruta ;

    @FXML
    private Button Iniciar ;

    @FXML
    protected void buscar(){
        DirectoryChooser directorioOrganizar = new DirectoryChooser() ; //abre el directorio para escoger
        directorioOrganizar.setTitle("escoja una carpeta para organizar"); //el mensaje de la ventana emergente

        File carpetaSeleccionada  = directorioOrganizar.showDialog(Iniciar.getScene().getWindow()); //guardo el directorio

        if (carpetaSeleccionada != null){
            ruta = carpetaSeleccionada.toPath();
            try{
                FXMLLoader cargar  = new FXMLLoader(getClass().getResource("/principal.fxml"));//crea un objeto fxmlLoader de principal dxml
                Parent root = cargar.load();//carga todo lo De el fxml y su controlador
                principalControl principal  = cargar.getController();//crea el objeto controlador lo que permite acceder al metodo De modificacion
                principal.setRuta(ruta);//metodo para mandar la ruta desde inicio

                Stage stage = (Stage)Iniciar.getScene().getWindow();//obtiene la ventana donde estoy parado
                stage.setScene(new Scene(root));//cambia la escena la ventana
                stage.show();//la muestra
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else {
            JOptionPane.showMessageDialog(null,  "Ups! error al escoger tu carpeta");
        }


    }

}

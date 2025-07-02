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

//import java.lang.classfile.Label;  se usa para archivos .class

public class inicioControl {

    private File carpetaSeleccionada ;

    @FXML
    private Button Iniciar ;

    @FXML
    protected void buscar(){
        DirectoryChooser directorioOrganizar = new DirectoryChooser() ; //abre el directorio para escoger
        directorioOrganizar.setTitle("escoja una carpeta para organizar"); //el mensaje de la ventana emergente

        carpetaSeleccionada  = directorioOrganizar.showDialog(null); //guardo el directorio

        if (carpetaSeleccionada != null){
            System.out.println( "se imprimio bien ;" + carpetaSeleccionada.getAbsolutePath());
            try{
                Parent root  = FXMLLoader.load(getClass().getResource("/principal.fxml")); //carga el archivo principal
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

    public File getCarpetaSeleccionada() {
        return carpetaSeleccionada;
    }
}

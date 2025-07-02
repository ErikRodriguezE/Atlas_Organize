package UI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

//import java.lang.classfile.Label;  se usa para archivos .class

public class inicioControl {
    @FXML //asi lllamo los fxm del otro archivo
    private Label texto;

    @FXML
    protected void mostrar(){
        texto.setText("inicio del todo");
    }


}

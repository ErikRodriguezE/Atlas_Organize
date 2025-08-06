package UI;


import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.swing.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class principalControl {

    private Path ruta ;
    private static ArrayList <String> listaExtenciones = new ArrayList<>();

    @FXML
    private Button organizar;

    @FXML
    private void organizar (){
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(ruta)){
            for (Path archivo : stream){
                if (Files.isRegularFile(archivo)){
                    String nombre = archivo.getFileName().toString();
                    int punto = nombre.lastIndexOf(".");
                    //verifica si existe algo despues del .
                    if (punto != -1 && punto < nombre.length()-1){
                        String ext = nombre.substring(punto+1).toLowerCase();
                        arrayExtencion(ext);// agrega a un arraylist
                    }
                } else if (Files.isDirectory(archivo)) {
                    System.out.println("subcarpetas: "+archivo.getFileName());
                }
            }
            String[] opcionesExtencion = listaExtenciones.toArray(new String[0]);

            String opcionOrganizar = (String) JOptionPane.showInputDialog(
                    null,"Sellecciona el tipo de archivo que quieres organizar",
                    "Archivos",JOptionPane.QUESTION_MESSAGE,null, opcionesExtencion, opcionesExtencion[0]);

            if (opcionOrganizar!=null){
                System.out.println("se escogio"+opcionOrganizar);
            }


        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(ruta);



    }

    public void arrayExtencion(String ext){
        if (!listaExtenciones.contains(ext)){ //busca si la extencion ya existe en el array
            listaExtenciones.add(ext);
        }
    }

    public void imprimir(){

        for (String ext : listaExtenciones){
            System.out.println(ext);
        }
    }

    public void setRuta(Path ruta) {
        this.ruta = ruta;
    }
}

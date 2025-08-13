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
import java.io.IOException;
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
        //verifica todos los archivos en una carpeta y los mete en un array para luego preguntar por que tipo de archivo quiere organizar, si no hay mas archivos vuelve a el inicio
        try{
            //llena el array con las extenciones de los archivos que haya por primera vez, si no hay archivos vuelve a inicio
            extencionesArray();

            //JOption que permite escoger entre todos los archvos
            String[] opcionesExtencion = listaExtenciones.toArray(new String[0]);

            String opcionOrganizar = (String) JOptionPane.showInputDialog(
                    null,"Sellecciona el tipo de archivo que quieres organizar",
                    "Archivos",JOptionPane.QUESTION_MESSAGE,null, opcionesExtencion, opcionesExtencion[0]);

            //si no sale null va hacer la accion de mover los archivos escogidos, una vez terminado borra los datos del array para actualizarlo despues
            if (opcionOrganizar!=null){
                moverArchivos(opcionOrganizar);
            }

            extencionesArray();//si no hay mas archivos el array sera vacio y cambiara de escena, si aun hay solo actualiza


        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(ruta);



    }
    //metodo que llena el array y verifica que la carpeta se quedo vacia
    public void extencionesArray(){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ruta)) {
            for (Path archivo : stream) {
                if (Files.isRegularFile(archivo)) {
                    String nombre = archivo.getFileName().toString();
                    int punto = nombre.lastIndexOf(".");
                    if (punto != -1 && punto < nombre.length() - 1) {
                        String ext = nombre.substring(punto + 1).toLowerCase();

                        if (!listaExtenciones.contains(ext)){ //busca si la extencion ya existe en el array
                            listaExtenciones.add(ext);
                        }
                    }
                } else if (Files.isDirectory(archivo)) {
                    System.out.println("subcarpetas: " + archivo.getFileName());
                }
            }
            if (listaExtenciones.isEmpty()) {
                JOptionPane.showMessageDialog(null, "La quedo vacia \n Volviendo a inicio.......");
                terminoElProceso();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo que mueve los archivos escogidos -------------- completar y corregir error de repeticion logica
    public void moverArchivos (String extencion){
        //pregunta si quiere buscar la carpeta, crearla o cancelar las opciones
        int opcion = JOptionPane.showConfirmDialog(null,"Quieres buscar la carpeta donde llevar los archivos escogidos \n Si --- abrir explorador de archivos \n No --- Se crea una carpeta nueva donde meterlo",
                "Confirme", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        //si busca la carpeta hace el proceso de  movelos de forma normal, si existe uno repetido les agreaga un numero para evistar fallos
        if (opcion == JOptionPane.YES_OPTION){
            DirectoryChooser directorioOrganizar = new DirectoryChooser() ; //abre el directorio para escoger
            directorioOrganizar.setTitle("escoja una carpeta para organizar"); //el mensaje de la ventana emergente

            File carpetaSeleccionada  = directorioOrganizar.showDialog((Stage) organizar.getScene().getWindow()); //aca se escoge la carpeta

            if (carpetaSeleccionada != null){
                Path rutaMover = carpetaSeleccionada.toPath(); //convertimos de tipo file a path
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(ruta)){ //recorre la carpeta "ruta" (la carpeta que escojimos al principio)
                    for (Path archivo : stream){//recorremos la lista
                        if (Files.isRegularFile(archivo) && archivo.toString().endsWith("."+extencion)){
                            Path destino = rutaMover.resolve(archivo.getFileName());
                            int contador = 1;
                            //por si exite otro archivo con el mismo nombre
                            while (Files.exists(destino)){
                                String nombre = archivo.getFileName().toString();
                                int punto = nombre.lastIndexOf(".");
                                // "?" y ":" significa un if y else
                                // ej: resultado = (condición) ? valorSiVerdadero : valorSiFalso;
                                String base = (punto == -1) ? nombre : nombre.substring(0,punto); //si no tiene un punto coje el string completo y ya, si tiene punto coje todo el String  que va antes del punto
                                String extension = (punto == -1) ? "" : nombre.substring(punto);// lo mismo pero solo coje lo que va despues del punto

                                destino = rutaMover.resolve(base + "(" + contador + ")" + extension);
                                contador++;
                            }

                            Files.move(archivo,destino);
                            listaExtenciones.clear();
                            System.out.println("movido: "+archivo.getFileName());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                JOptionPane.showMessageDialog(null,  "Ups! error al escoger tu carpeta");
            }
        }
        else if (opcion == JOptionPane.NO_OPTION) {
            System.out.println("no");
        }else {
            JOptionPane.showMessageDialog(null,  "Ups! error al escoger tu carpeta");
        }
    }

    public void setRuta(Path ruta) {
        this.ruta = ruta;
    }

    //cambia la escena
    public void terminoElProceso(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/inicio.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual desde cualquier nodo que ya tengas
            Stage stage = (Stage) organizar.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

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
                //pregunta si quiere buscar la carpeta, crearla o cancelar las opciones
                int opcion = JOptionPane.showConfirmDialog(null,"Quieres buscar la carpeta donde llevar los archivos escogidos \n Si --- abrir explorador de archivos \n No --- Se crea una carpeta nueva donde meterlo",
                        "Confirme", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (opcion == JOptionPane.YES_OPTION){
                    procesoDeMover(opcionOrganizar,1);
                } else if (opcion == JOptionPane.NO_OPTION) {
                    procesoDeMover(opcionOrganizar,0);
                }
            }
            //si no hay mas archivos el array sera vacio y cambiara de escena, si aun hay solo actualiza la lista
            extencionesArray();


        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,  "Ups! error");
        }
    }

    //metodo que llena el array y verifica que la carpeta se quedo vacia
    public void extencionesArray(){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ruta)) {
            //recorre la carpeta actual
            for (Path archivo : stream) {
                //si encuentra un archivo que no sea un directorio lo va validar para agregarlo
                if (Files.isRegularFile(archivo)) {
                    //adquiere el nombre de el archivo
                    String nombre = archivo.getFileName().toString();
                    int punto = nombre.lastIndexOf(".");
                    //adquiere la extencion (lo que va despues del punto)
                    if (punto != -1 && punto < nombre.length() - 1) {
                        String ext = nombre.substring(punto + 1).toLowerCase();
                        //busca si la extencion ya existe en el array, si no existe lo agrega
                        if (!listaExtenciones.contains(ext)){
                            listaExtenciones.add(ext);
                        }
                    }
                }
            }
            //si ve que no exita mas archivos en el directorio te manda a inicio
            if (listaExtenciones.isEmpty()) {
                JOptionPane.showMessageDialog(null, "La carpeta quedo vacia \n\nVolviendo a inicio..........");
                terminoElProceso();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo que mueve los archivos escogidos
    public void procesoDeMover(String extencion, int opcion ){
        //opcion "si" para buscar la carpeta
        if (opcion == 1){
            DirectoryChooser directorioOrganizar = new DirectoryChooser() ; //abre el directorio para escoger
            directorioOrganizar.setTitle("escoja una carpeta para organizar"); //el mensaje de la ventana emergente

            File carpetaSeleccionada  = directorioOrganizar.showDialog((Stage) organizar.getScene().getWindow()); //aca se escoge la carpeta

            if (carpetaSeleccionada != null){
                //convertimos de tipo file a path para poder enviarlo
                Path rutaMover = carpetaSeleccionada.toPath();
                //metodo de mover archivos (ruta donde se movera que extrajimos del directoriChooser, la extencion de los archivos que movera)
                moverArchivos(rutaMover, extencion);
            }
            else{
                JOptionPane.showMessageDialog(null,  "Ups! error al escoger tu carpeta");
            }
        }

        //crea una carpeta dentro del directorio en que esta y mueve los archivos
        else if (opcion == 0) {
            //se pide el nombre de la nueva carpeta
            String nombre = JOptionPane.showInputDialog("Digita el nombre la carpeta" );
            //para evitar errores con variables null
            if (nombre == null){
                return;
            }
            //verifica si existe
            if (existeCarpeta(nombre)){
                //ya que la carpeta consigue la nueva ruta solo agregandole la nueva carpeta que ya existia
                Path rutaMover = ruta.resolve(nombre);
                //metodo de mover archivos (ruta donde se movera, la extencion de los archivos que movera)
                moverArchivos(rutaMover, extencion);

            }
            //si no existe la carpeta
            else{
                try {
                    //crea el directorio
                    Files.createDirectory(ruta.resolve(nombre));
                    //consigue la nueva ruta
                    Path rutaMover = ruta.resolve(nombre);
                    //metodo de mover carpeta (ruta donde se movera, la extencion de  los archivos que movera)
                    moverArchivos(rutaMover, extencion);

                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,  "Ups! error");
                }
            }
        }
    }

    public void setRuta(Path ruta) {
        this.ruta = ruta;
    }

    //cambia la escena a el inicio
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

    public void moverArchivos (Path rutaMover, String extencion){
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

    //verifica si existe una carpeta en el directorio actual llamada igual a al que quieren crear
    public boolean existeCarpeta(String nombreCarpeta){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(ruta)) {
            //recorre el directorio
            for (Path p : stream) {
                if (Files.isDirectory(p) && p.getFileName().toString().equalsIgnoreCase(nombreCarpeta)) {
                    return true; // Retorna true si encuentra una subcarpeta llamda igual
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; //si no encuentra nada retorna
    }

}

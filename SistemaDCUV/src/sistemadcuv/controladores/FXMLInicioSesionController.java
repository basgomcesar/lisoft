package sistemadcuv.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
<<<<<<< Updated upstream
=======
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sistemadcuv.utils.Utilidades;
>>>>>>> Stashed changes


public class FXMLInicioSesionController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
<<<<<<< Updated upstream
=======

    @FXML
    private void iniciarSesion(ActionEvent event) {
        if(!esCamposVacios()){
            if(esFormatoValido()){
                if(tfUsuario.getText().matches(formato)){
                   
                }
            }else{
                Utilidades.mostrarAletarSimple("Formato invalido",
                        "El usuario no coincide con el formato de una matricula o numero de personal\n"
                                + "Ej.matricula valida: 'zsXXXXXXX", 
                        Alert.AlertType.INFORMATION);
            }
        }else{
            Utilidades.mostrarAletarSimple("Campos vacios", 
                    "Por favor llene los campos faltantes", 
                    Alert.AlertType.INFORMATION);
        }
    }

    private boolean esCamposVacios() {
        return tfUsuario.getText().trim().isEmpty() || tfContrasenia.getText().trim().isEmpty();
    }

    private boolean esFormatoValido() {
        return tfUsuario.getText().matches(formato);
    }
>>>>>>> Stashed changes
    
}

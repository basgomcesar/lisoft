package sistemadcuv.controladores;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sistemadcuv.modelo.dao.CambioDAO;
import sistemadcuv.modelo.pojo.Cambio;
import sistemadcuv.modelo.pojo.Desarrollador;
import sistemadcuv.modelo.pojo.ResponsableDeProyecto;
import sistemadcuv.utils.Utilidades;


public class FXMLBitacoraDeCambiosGeneralController implements Initializable {

    private Desarrollador desarrolladorSesion;
    private ResponsableDeProyecto responsableSesion;
    @FXML
    private Label lbUsuarioActivo;
    @FXML
    private TableView<Cambio> tvCambios;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colDesarrollador;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaFin;
    @FXML
    private TextField tfNombre;
    
    private ObservableList<Cambio> cambios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
    }    

    public void inicializarInformacion(Desarrollador desarrollador, ResponsableDeProyecto responsable) {
        this.desarrolladorSesion = desarrollador;
        this.responsableSesion = responsable;
        cargarInformacionCambios(desarrollador, responsable);
        if(desarrollador != null){
            lbUsuarioActivo.setText("Usuario: " + desarrollador.getNombreCompleto());
        }else{
            lbUsuarioActivo.setText("Usuario: " + responsable.getNombreCompleto());
        }
    }
    
    private void configurarTabla(){
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estado"));
        this.colDesarrollador.setCellValueFactory(new PropertyValueFactory("desarrollador"));
        this.colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        this.colFechaFin.setCellValueFactory(new PropertyValueFactory("fechaFin"));
    }
    
    private void cargarInformacionCambios(Desarrollador desarrollador, ResponsableDeProyecto responsable){
        HashMap<String, Object> respuesta = new HashMap<>();
        if(desarrollador != null){
            respuesta = CambioDAO.obtenerListadoCambiosDesarrollador(desarrolladorSesion.getIdDesarrollador());
        } else{
            respuesta = CambioDAO.obtenerListadoCambios();
        }
        
        if(!(boolean) respuesta.get("error")){
            cambios = FXCollections.observableArrayList();
            ArrayList<Cambio> lista = (ArrayList<Cambio>) respuesta.get("cambios");
            cambios.addAll(lista);
            tvCambios.setItems(cambios);
        }else{
            Utilidades.mostrarAletarSimple("Error", respuesta.get("mensaje").toString(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnActividades(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaActividades(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnCambios(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaListadoDeCambios(escenarioBase, desarrolladorSesion, responsableSesion);
    }


    @FXML
    private void btnDefectos(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaDefectos(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnParticipantes(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaParticipantes(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnBitacora(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaBitacoraGeneral(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnSolicitudes(MouseEvent event) {
        Stage escenarioBase = (Stage)lbUsuarioActivo.getScene().getWindow();
        Utilidades.irVentanaSolicitudes(escenarioBase, desarrolladorSesion, responsableSesion);
    }

    @FXML
    private void btnExportarBitacoraCambios(ActionEvent event) throws DocumentException {
        DirectoryChooser directorioSeleccion = new DirectoryChooser();
        File directorio = directorioSeleccion.showDialog(tfNombre.getScene().getWindow());

        try {
            if (directorio != null) {
                String rutaArchivo = directorio.getAbsolutePath() + "/BitacoraDeCambios.pdf";
                Document documento = new Document();
                PdfWriter.getInstance(documento, new FileOutputStream(rutaArchivo));
                documento.open();

                // Agrega el encabezado
                documento.add(new Paragraph("Bitácora de Cambios"));

                // Agrega un espacio en blanco entre el encabezado y la tabla
                documento.add(new Paragraph(" "));

                // Crea la tabla y establece el número de columnas
                PdfPTable tabla = new PdfPTable(5); // 5 columnas

                // Ajusta el ancho de las columnas (puedes ajustar estos valores según tus necesidades)
                float[] columnWidths = {2f, 2f, 2f, 2f, 2f};
                tabla.setWidths(columnWidths);

                // Ajusta el espaciado antes y después de la tabla
                tabla.setSpacingBefore(10f);
                tabla.setSpacingAfter(10f);
                
                // Ajusta la alineación del texto en la tabla
                tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                // Agrega encabezados de columna y establece el fondo de las celdas
                tabla.addCell(getCell("Nombre", true));
                tabla.addCell(getCell("Estado", true));
                tabla.addCell(getCell("Desarrollador", true));
                tabla.addCell(getCell("Fecha Inicio", true));
                tabla.addCell(getCell("Fecha Fin", true));

                // Agrega los datos y establece el fondo de las celdas
                for (Cambio cambio : cambios) {
                    tabla.addCell(getCell(cambio.getNombre(), false));
                    tabla.addCell(getCell(cambio.getEstado(), false));
                    tabla.addCell(getCell(cambio.getDesarrollador(), false));
                    tabla.addCell(getCell(cambio.getFechaInicio(), false));
                    tabla.addCell(getCell(cambio.getFechaFin(), false));
                }

                // Agrega la tabla al documento
                documento.add(tabla);

                documento.close();
                Utilidades.mostrarAletarSimple("Archivo Exportado", "La información se exportó correctamente"
                        + " en el directorio: " + rutaArchivo, Alert.AlertType.INFORMATION);
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAletarSimple("Error de Exportación", "No se ha podido guardar el archivo.",
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnBitacoraPorDesarrollador(MouseEvent event) {
        try {
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
            FXMLLoader loader = Utilidades.cargarVista("vistas/FXMLListadoDeParticipantesParaBitacora.fxml");
            Parent vista = loader.load();
            FXMLListadoDeParticipantesParaBitacoraController controlador = loader.getController();
            controlador.inicializarInformacion(desarrolladorSesion, responsableSesion);
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.show();
            escenarioBase.setTitle("Listado de participantes para bitacora");
        } catch (IOException ex) {
            Utilidades.mostrarAletarSimple("Error al cargar ventana", 
                    ex.getMessage(), 
                    Alert.AlertType.INFORMATION);
        }
    // Método para obtener una celda con formato personalizado
    private PdfPCell getCell(String contenido, boolean esEncabezado) {
        PdfPCell cell = new PdfPCell(new Paragraph(contenido));

        // Ajusta la alineación del texto en la celda
        cell.setHorizontalAlignment(esEncabezado ? PdfPCell.ALIGN_CENTER : PdfPCell.ALIGN_CENTER);

        // Ajusta el fondo de la celda (usando colores de ejemplo)
        cell.setBackgroundColor(esEncabezado ? BaseColor.LIGHT_GRAY : BaseColor.WHITE);

        return cell;
    }
}

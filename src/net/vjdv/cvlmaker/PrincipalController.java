package net.vjdv.cvlmaker;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import net.vjdv.cvlmaker.modelos.CVL;

/**
 *
 * @author Jassiel
 */
public class PrincipalController implements Initializable {
  //Variables
  private final FileChooser cvlChooser = new FileChooser();
  private final FileChooser pngChooser = new FileChooser();
  private VistaCVL current_view;
  private TabView current_tab;
  private File current_file;
  private CVL current_cvl;
  @FXML
  private TabPane tab_pane;
  @FXML
  public void nuevo(ActionEvent evt){
    CVL cvl = new CVL();
    TabView tab = new TabView(cvl);
    tab_pane.getTabs().add(tab);
    tab_pane.getSelectionModel().select(tab);
  }
  @FXML
  public void abrir(ActionEvent evt){
    File file = cvlChooser.showOpenDialog(Main.stage);
    if(file!=null){
      CVL x = TraductorCVL.file2modelo(file);
      if(x==null){
        JOptionPane.showMessageDialog(null, "Error al abrir archivo");
        return;
      }
      TabView tab = new TabView(x);
      tab.setFile(file);
      tab_pane.getTabs().add(tab);
      tab_pane.getSelectionModel().select(tab);
    }
  }
  @FXML
  private void guardar(ActionEvent event){
    if(current_file==null) guardarComo(event);
    else guardarArchivo();
  }
  @FXML
  private void guardarComo(ActionEvent event){
    File file = cvlChooser.showSaveDialog(Main.stage);
    if(file!=null){
      current_file = file;
      guardarArchivo();
    }
  }
  private void guardarArchivo(){
    boolean exito = TraductorCVL.modelo2file(current_cvl, current_file);
    if(exito) current_tab.setFile(current_file);
    else JOptionPane.showMessageDialog(null, "Error al guardar CVL");
  }
  @FXML
  private void test(ActionEvent event){
    File f = new File("C:/Users/Jassiel/Desktop/Domotica.cvl");
    CVL cvl = TraductorCVL.file2modelo(f);
    TabView tab = new TabView(cvl);
    tab.setFile(f);
    tab_pane.getTabs().add(tab);
  }
  @FXML
  private void zoomIn(ActionEvent event){
    double escala = current_view.getScaleX()+0.1;
    current_view.setScaleX(escala);
    current_view.setScaleY(escala);
  }
  @FXML
  private void zoomOut(ActionEvent event){
    if(current_view.getScaleX()<0.2) return;
    double escala = current_view.getScaleX()-0.1;
    current_view.setScaleX(escala);
    current_view.setScaleY(escala);
  }
  @FXML
  private void zoomReset(ActionEvent event){
    double escala = 1;
    current_view.setScaleX(escala);
    current_view.setScaleY(escala);
  }
  @FXML
  private void exportar(ActionEvent event){
    File file = pngChooser.showSaveDialog(Main.stage);
    if(file!=null){
      boolean exito = TraductorCVL.vista2image(current_view, file);
      if(exito) JOptionPane.showMessageDialog(null, "Exportado");
      else JOptionPane.showMessageDialog(null, "Error de exportación");
    }
  }
  @FXML
  private void irSitio(ActionEvent event){
    try{
      Desktop.getDesktop().browse(new URI("http://jassiel.vjdv.net"));
    }catch(URISyntaxException | IOException ex){
      System.out.println("No se puede abrir web");
    }
  }
  @FXML
  private void acercaDe(ActionEvent event) throws IOException{
    ImageView icon = new ImageView(getClass().getResource("ito.png").toString());
    icon.setFitWidth(120);
    icon.setFitHeight(120);
    Parent root = FXMLLoader.load(getClass().getResource("About.fxml"));
    Alert dialog = new Alert(AlertType.INFORMATION);
    dialog.initStyle(StageStyle.UTILITY);
    dialog.setTitle("Acerca de CVL Maker");
    dialog.setHeaderText(null);
    dialog.getDialogPane().setContent(root);
    dialog.setGraphic(icon);
    dialog.showAndWait();
  }
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    tab_pane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
      if(newValue==null) return;
      TabView tab = (TabView) newValue;
      current_tab = tab;
      current_cvl = tab.getCVL();
      current_file = tab.getFile();
      current_view = tab.getView();
    });
    cvlChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CVL (xml o cvl)","*.cvl","*.xml"));
    cvlChooser.setInitialFileName("*.cvl");
    pngChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagen PNG", "*.png"));
    pngChooser.setInitialFileName("*.png");
  }
}
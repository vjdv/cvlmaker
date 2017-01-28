package net.vjdv.cvlmaker;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javax.swing.JOptionPane;
import net.vjdv.cvlmaker.modelos.CVL;
import net.vjdv.cvlmaker.modelos.Child;
import net.vjdv.cvlmaker.modelos.Choice;
import net.vjdv.cvlmaker.modelos.Multiplicity;
import net.vjdv.cvlmaker.modelos.VSpec;

/**
 * Crea un panel capaz de mostrar y editar gráficamente un modelo CVL
 * @author Jassiel
 * @version 1.0
 * @see http://jassiel.vjdv.net/apps/cvlmaker/
 */
public class VistaCVL extends Pane{
  private final List<ListenerView> listeners = new ArrayList<>();
  private static ChoiceView portapapeles;
  private ChoiceView raiz;
  private CVL cvl;
  public VistaCVL(){
    getStylesheets().add("net/vjdv/cvlmaker/Estilo.css");
    setPadding(new Insets(5));
  }
  /**
   * Establece el modelo CVL a controlar
   * @param cvl Modelo CVL
   */
  public void setCVL(CVL cvl){
    this.cvl = cvl;
    raiz = new ChoiceView(this.cvl);
    getChildren().add(raiz);
    leerHijos(raiz);
    raiz.dibujar();
  }
  private void leerHijos(ChoiceView padre){
    for (Choice hijo : padre.choice.obtenerHijos()) {
      ChoiceView l = new ChoiceView(hijo);
      padre.agregarHijo(l);
      leerHijos(l);
    }
  }
  /**
   * Limpia la pantalla y modelo CVL
   */
  public void clear(){
    getChildren().clear();
    cvl = null;
    raiz = null;
  }
  public void addOnUpdateListener(ListenerView listener){
    listeners.add(listener);
  }
  public void removeOnUpdateListener(ListenerView listener){
    listeners.remove(listener);
  }
  public void notificarListeners(){
    listeners.stream().forEach((l)->{l.updatedModel();});
  }
  public class ChoiceView extends Pane{
    private final List<ChoiceView> hijos = new ArrayList<>();
    private final int hspace = 5, vspace = 90;
    public Choice choice;
    public final Label label;
    private ChoiceView padre;
    private Line linea;
    public ChoiceView(Choice choice){
      this.choice = choice;
      if(choice instanceof CVL){
        label = new Label(choice.obtenerNombre()+" : ConfigurableUnit");
        label.getStyleClass().add("cvl_unit");
        setLayoutX(10);
        setLayoutY(10);
      }else{
        label = new Label(choice.obtenerNombre());
        label.getStyleClass().add("cvl_choice");
      }
      getChildren().add(label);
      getStyleClass().add("choice_panel");
      setWidth(label.getWidth());
      label.setOnMouseClicked((MouseEvent event) -> {
        if(event.getButton()!=MouseButton.PRIMARY)return;
        String nombre = JOptionPane.showInputDialog("Renombrar característica", this.choice.obtenerNombre());
        if(nombre!=null){
          choice.setNombre(nombre);
          if(choice instanceof CVL) label.setText(nombre+" : ConfigurableUnit");
          else label.setText(nombre);
          raiz.dibujar();
          notificarListeners();
        }
      });
      label.setContextMenu(new MenuChoice(this));
    }
    public void agregarHijo(ChoiceView hijo){
      hijos.add(hijo);
      hijo.setPadre(this);
      getChildren().add(hijo);
    }
    public void acomodar(){
      double width = aproxWidth();
      double xpos = 0;
      double inicio_x = 0d;
      if(width>labelWidth()) xpos = (width-labelWidth())/2;
      else inicio_x = (width-childsWidth())/2;
      label.setLayoutX(xpos);
      for (ChoiceView h : hijos){
        if(choice.obtenerNombre().startsWith("Blue")) System.out.println("Blue: "+inicio_x);
        h.setLayoutX(inicio_x);
        h.setLayoutY(vspace);
        inicio_x = inicio_x + h.aproxWidth() + hspace;
        h.acomodar();
      }
    }
    public void dibujarLineas(){
      hijos.stream().forEach((h) -> {
        Line line = h.getLinea();
        line.setStartX(label.getLayoutX()+(labelWidth()/2));
        line.setStartY(choice.hasMultiplicity() ? 58 : 38);
        line.setEndX(h.getLayoutX()+(h.aproxWidth()/2));
        line.setEndY(h.getLayoutY());
        if(!getChildren().contains(line)) getChildren().add(line);
        h.dibujarLineas();
      });
    }
    public void dibujarTriangulos(){
      if(choice.hasMultiplicity()){
        Polygon p = getTriangulo();
        p.setLayoutX(label.getLayoutX()+(labelWidth()/2)-15);
        p.setLayoutY(38);
        triangulo_lb.setText(choice.getMultiplicity().upper==1 ? "1..1" : "1..-1");
        triangulo_lb.setLayoutX(label.getLayoutX()+(labelWidth()/2)+15);
        triangulo_lb.setLayoutY(42);
        if(!getChildren().contains(p)) getChildren().add(p);
        if(!getChildren().contains(triangulo_lb)) getChildren().add(triangulo_lb);
      }else{
        if(triangulo!=null) getChildren().remove(getTriangulo());
        getChildren().remove(triangulo_lb);
      }
      hijos.stream().forEach((h) -> {
        h.dibujarTriangulos();
      });
    }
    private Polygon triangulo;
    private Label triangulo_lb = new Label();
    private Polygon getTriangulo(){
      if(triangulo==null){
        triangulo = new Polygon(15,0,30,20,0,20);
        triangulo.setStroke(Color.web("#444444"));
        triangulo.setFill(Color.web("#444444"));
      }
      return triangulo;
    }
    public double aproxWidth(){
      return Math.max(childsWidth(), labelWidth());
    }
    private double childsWidth(){
      if(hijos.isEmpty()) return labelWidth();
      else{
        double posibleWidth = 0;
        for (ChoiceView h : hijos) posibleWidth = posibleWidth + h.aproxWidth();
        posibleWidth += (hijos.size()-1)*hspace;
        return posibleWidth;
      }
    }
    FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
    Font font = Font.font("System", FontWeight.THIN, FontPosture.REGULAR, 14);
    public double labelWidth(){
      return fontLoader.computeStringWidth(label.getText(), font)+18;
    }
    public Line getLinea(){
      if(linea==null){
        linea = new Line();
        linea.setStrokeWidth(2);
        linea.setStroke(Color.web("#444444"));
      }
      if(choice!= null && choice.esObligatorio() && !getPadre().choice.hasMultiplicity()) linea.getStrokeDashArray().clear();
      else linea.getStrokeDashArray().addAll(5d,5d);
      return linea;
    }
    public boolean remover(ChoiceView c){
      if(hijos.contains(c)){
        hijos.remove(c);
        getChildren().remove(c);
        getChildren().remove(c.getLinea());
        choice.remover(c.choice);
        return true;
      }
      boolean r = false;
      for(ChoiceView h : hijos){
        r = h.remover(c);
        if(r) break;
      }
      return r;
    }
    public void setPadre(ChoiceView padre){
      this.padre = padre;
    }
    public ChoiceView getPadre(){
      return padre;
    }
    public void dibujar(){
      acomodar();
      dibujarTriangulos();
      dibujarLineas();
    }
  }
  public class MenuChoice extends ContextMenu{
    public MenuChoice(ChoiceView choice_view){
      MenuItem opt1 = new MenuItem("Agregar hijo");
      MenuItem opt2 = new MenuItem("Eliminar");
      MenuItem opt3 = new MenuItem("Opcional/Obligatorio");
      Menu opt4 = new Menu("Restricción del grupo");
      MenuItem opt5 = new MenuItem("Ninguna 0..*");
      MenuItem opt6 = new MenuItem("OR 1..*");
      MenuItem opt7 = new MenuItem("XOR 1..1");
      MenuItem opt8 = new MenuItem("Cortar");
      MenuItem opt9 = new MenuItem("Pegar como hijo");
      MenuItem opt10 = new MenuItem("Pegar como hermano");
      opt4.getItems().addAll(opt5,opt6,opt7);
      opt1.setOnAction((ActionEvent event) -> {
        String nuevo_nombre = JOptionPane.showInputDialog("Nombre de característica:");
        if(nuevo_nombre==null) return;
        Choice nueva_choice;
        if(choice_view.choice instanceof CVL) nueva_choice = new VSpec();
        else nueva_choice = new Child();
        nueva_choice.setNombre(nuevo_nombre);
        choice_view.choice.agregarHijo(nueva_choice);
        ChoiceView nueva_vista = new ChoiceView(nueva_choice);
        choice_view.agregarHijo(nueva_vista);
        raiz.dibujar();
        notificarListeners();
      });
      opt2.setOnAction((ActionEvent event) -> {
        raiz.remover(choice_view);
        raiz.dibujar();
        notificarListeners();
      });
      opt3.setOnAction((ActionEvent event) -> {
        choice_view.choice.setObligatorio(!choice_view.choice.esObligatorio());
        raiz.dibujarLineas();
        notificarListeners();
      });
      opt5.setOnAction((ActionEvent event) -> {
        choice_view.choice.setMultiplicity(Multiplicity.NORMAL);
        raiz.dibujarTriangulos();
        raiz.dibujarLineas();
        notificarListeners();
      });
      opt6.setOnAction((ActionEvent event) -> {
        choice_view.choice.setMultiplicity(Multiplicity.OR);
        raiz.dibujarTriangulos();
        raiz.dibujarLineas();
        notificarListeners();
      });
      opt7.setOnAction((ActionEvent event) -> {
        choice_view.choice.setMultiplicity(Multiplicity.XOR);
        raiz.dibujarTriangulos();
        raiz.dibujarLineas();
        notificarListeners();
      });
      opt8.setOnAction((ActionEvent event) -> {
        VistaCVL.portapapeles = choice_view;
        choice_view.getPadre().remover(choice_view);
        raiz.dibujar();
        notificarListeners();
      });
      opt9.setOnAction((ActionEvent event) -> {
        if(VistaCVL.portapapeles==null){
          JOptionPane.showMessageDialog(null, "Portapapeles vacío");
          return;
        }
        Choice xchoice = VistaCVL.portapapeles.choice;
        if(choice_view.choice instanceof CVL && VistaCVL.portapapeles.choice instanceof Child)
          xchoice = TraductorCVL.child2vspec((Child)VistaCVL.portapapeles.choice);
        else if((choice_view.choice instanceof VSpec || choice_view.choice instanceof Child) && VistaCVL.portapapeles.choice instanceof VSpec)
          xchoice = TraductorCVL.vspec2child((VSpec)VistaCVL.portapapeles.choice);
        VistaCVL.portapapeles.choice = xchoice;
        choice_view.choice.agregarHijo(xchoice);
        choice_view.agregarHijo(VistaCVL.portapapeles);
        VistaCVL.portapapeles = null;
        raiz.dibujar();
        notificarListeners();
      });
      opt10.setOnAction((ActionEvent event) -> {
        if(VistaCVL.portapapeles==null){
          JOptionPane.showMessageDialog(null, "Portapapeles vacío");
          return;
        }
        ChoiceView target = choice_view.getPadre();
        Choice xchoice = VistaCVL.portapapeles.choice;
        if(target.choice instanceof CVL && VistaCVL.portapapeles.choice instanceof Child)
          xchoice = TraductorCVL.child2vspec((Child)VistaCVL.portapapeles.choice);
        else if((target.choice instanceof VSpec || target.choice instanceof Child) && VistaCVL.portapapeles.choice instanceof VSpec)
          xchoice = TraductorCVL.vspec2child((VSpec)VistaCVL.portapapeles.choice);
        target.choice.agregarHijo(xchoice);
        VistaCVL.portapapeles.choice = xchoice;
        target.agregarHijo(VistaCVL.portapapeles);
        VistaCVL.portapapeles = null;
        raiz.dibujar();
        notificarListeners();
      });
      if(choice_view.choice instanceof CVL) getItems().addAll(opt1,new SeparatorMenuItem(),opt9);
      else{
        getItems().addAll(opt1,opt2,new SeparatorMenuItem());
        getItems().addAll(opt8,opt9,opt10,new SeparatorMenuItem());
        getItems().addAll(opt3,opt4);
      }
    }
  }
}
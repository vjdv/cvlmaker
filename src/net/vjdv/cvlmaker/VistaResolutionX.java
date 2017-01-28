package net.vjdv.cvlmaker;

import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javax.swing.JOptionPane;
import net.vjdv.cvlmaker.modelos.CVL;
import net.vjdv.cvlmaker.modelos.Child;
import net.vjdv.cvlmaker.modelos.Choice;
import net.vjdv.cvlmaker.modelos.VSpec;

/**
 * Crea un panel capaz de mostrar y editar gráficamente un modelo CVL
 * @author Jassiel
 * @version 1.0
 * @see http://jassiel.vjdv.net/apps/cvlmaker/
 */
public class VistaResolutionX extends VistaCVL implements ListenerView{
  private final Map<String,Choice> choice_map = new HashMap<>();
  private ChoiceViewX raiz;
  private CVL cvl, master_cvl;
  /**
   * Utilice setCVL(CVL master, CVL model)
   * @param cvl Modelo CVL
   */
  @Override
  public void setCVL(CVL cvl){
    this.cvl = cvl;
    raiz = new ChoiceViewX(this.cvl);
    getChildren().add(raiz);
    leerHijos(raiz);
    raiz.dibujar();
  }
  public void setMaster(CVL master){
    master_cvl = master;
    updatedModel();
  }
  public void setCVL(CVL master, CVL model){
    setMaster(master);
    setCVL(model);
  }
  private void leerHijos(ChoiceView padre){
    for (Choice hijo : padre.choice.obtenerHijos()) {
      ChoiceViewX l = new ChoiceViewX(hijo);
      padre.agregarHijo(l);
      leerHijos(l);
    }
  }
  @Override
  public void updatedModel(){
    choice_map.clear();
    choice_map.put(master_cvl.obtenerNombre(), master_cvl);
    indexarHijos(master_cvl);
  }
  private void indexarHijos(Choice padre){
    for (Choice hijo : padre.obtenerHijos()) {
      choice_map.put(hijo.obtenerNombre(), hijo);
      indexarHijos(hijo);
    }
  }
  public class ChoiceViewX extends ChoiceView{
    
    public ChoiceViewX(Choice choice){
      super(choice);
      label.setOnMouseClicked(null);
      label.setContextMenu(new MenuChoice(this));
    }
    
  }
  public class MenuChoice extends ContextMenu{
    private final ChoiceView view;
    private final Menu opt1;
    public MenuChoice(ChoiceView choice_view){
      view = choice_view;
      opt1 = new Menu("Agregar");
      MenuItem opt2 = new MenuItem("Quitar");
      opt2.setOnAction((ActionEvent event) -> {
        choice_view.getPadre().remover(view);
        view.getPadre().choice.remover(view.choice);
        raiz.dibujar();
      });
      getItems().addAll(opt1,opt2);
      actualizarMenu();
    }
    public final void actualizarMenu(){
      Choice choice = choice_map.get(view.choice.obtenerNombre());
      if(choice==null) return;
      for(Choice hijo : choice.obtenerHijos()){
        MenuItem opt = new MenuItem(hijo.obtenerNombre());
        opt.setOnAction((ActionEvent event)->{
          Choice clon = hijo.clonar();
          ChoiceViewX new_view = new ChoiceViewX(clon);
          view.agregarHijo(new_view);
          view.choice.agregarHijo(clon);
          raiz.dibujar();
          notificarListeners();
        });
        opt1.getItems().add(opt);
      }
    }
  }
}
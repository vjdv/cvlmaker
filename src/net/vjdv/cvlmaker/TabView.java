package net.vjdv.cvlmaker;

import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import net.vjdv.cvlmaker.modelos.CVL;

/**
 *
 * @author Jassiel
 */
public class TabView extends Tab{
  private final Tooltip tooltip = new Tooltip();
  private final VistaCVL cvl_view = new VistaCVL();
  private final ScrollPane cvl_scroll = new ScrollPane(cvl_view);
  private static int count = 1;
  private File cvl_file;
  private final CVL cvl;
  public TabView(CVL cvl){
    this.cvl = cvl;
    cvl_view.setCVL(cvl);
    setContent(cvl_scroll);
    setText("Documento "+count);
    tooltip.setText("documento sin guardar");
    setTooltip(tooltip);
    count++;
  }
  public void setFile(File f){
    cvl_file = f;
    setText(f.getName());
    tooltip.setText(f.getAbsolutePath());
  }
  public File getFile(){
    return cvl_file;
  }
  public CVL getCVL(){
    return cvl;
  }
  public VistaCVL getView(){
    return cvl_view;
  }
}
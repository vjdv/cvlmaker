package net.vjdv.cvlmaker;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import net.vjdv.cvlmaker.modelos.CVL;
import net.vjdv.cvlmaker.modelos.Child;
import net.vjdv.cvlmaker.modelos.VSpec;

/**
 *
 * @author Jassiel
 */
public class TraductorCVL {
  public static boolean modelo2file(CVL cvl, File file){
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(CVL.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(cvl,System.out);
      jaxbMarshaller.marshal(cvl, file);
      return true;
    }catch(Exception ex){
      Logger.getLogger("CVLMAKER").log(Level.SEVERE,"Problema al guardar",ex);
      return false;
    }
  }
  public static CVL file2modelo(File file){
    CVL cvl;
    try{
      JAXBContext context = JAXBContext.newInstance(CVL.class);
      Unmarshaller u = context.createUnmarshaller();
      cvl = (CVL) u.unmarshal(new FileInputStream(file));
      return cvl;
    }catch(JAXBException | FileNotFoundException ex){
      Logger.getLogger("CVLMAKER").log(Level.SEVERE, "Error al importar CVL", ex);
      return null;
    }
  }
  public static String modelo2string(CVL cvl){
    try {
      StringWriter sw = new StringWriter();
      JAXBContext jaxbContext = JAXBContext.newInstance(CVL.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      jaxbMarshaller.marshal(cvl,System.out);
      jaxbMarshaller.marshal(cvl, sw);
      return sw.toString();
    }catch(Exception ex){
      Logger.getLogger("CVLMAKER").log(Level.SEVERE,"Problema al convertir a cadena",ex);
      return null;
    }
  }
  public static boolean vista2image(VistaCVL view, File file) {
    if(view==null || file==null) return false;
    WritableImage snapshot = view.snapshot(new SnapshotParameters(), null);
    BufferedImage image = javafx.embed.swing.SwingFXUtils.fromFXImage(snapshot, null);
    try {
      Graphics2D gd = (Graphics2D) image.getGraphics();
      gd.translate(view.getWidth(), view.getHeight());
      ImageIO.write(image, "png", file);
      return true;
    } catch (IOException ex) {
      Logger.getLogger("CVLMAKER").log(Level.SEVERE, null, ex);
      return false;
    }
  }
  public static VSpec child2vspec(Child child){
    VSpec vspec = new VSpec();
    vspec.name = child.name;
    vspec.isImpliedByParent = child.isImpliedByParent;
    vspec.multiplicity = child.multiplicity;
    child.hijos.stream().forEach((h) -> {
      vspec.agregarHijo(h);
    });
    return vspec;
  }
  public static Child vspec2child(VSpec vspec){
    Child child = new Child();
    child.name = vspec.name;
    child.isImpliedByParent = vspec.isImpliedByParent;
    child.multiplicity = vspec.multiplicity;
    vspec.hijos.stream().forEach((h) -> {
      child.agregarHijo(h);
    });
    return child;
  }
}
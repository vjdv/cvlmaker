package net.vjdv.cvlmaker.modelos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Representa a los hijos de cada unidad configurable en un CVL
 * @author Jassiel
 */
public class VSpec implements Choice{
  //Variables normales
  private CVL padre;
  @XmlAttribute(namespace = "http://www.w3.org/2001/XMLSchema-instance")
  public String type = "cvl:Choice";
  @XmlAttribute
  public String name = "";
  @XmlAttribute
  public Boolean isImpliedByParent = false;
  @XmlElement(name="groupMultiplicity")
  public Multiplicity multiplicity = null;
  @XmlElement(name = "child")
  public List<Child> hijos = new ArrayList<>();
  @Override
  public Choice[] obtenerHijos() {
    return hijos.toArray(new Child[hijos.size()]);
  }
  @Override
  public String obtenerNombre() {
    return name;
  }
  @Override
  public Boolean esObligatorio() {
    return isImpliedByParent!=null && isImpliedByParent;
  }
  @Override
  public void setNombre(String n) {
    name = n;
  }
  @Override
  public void setObligatorio(boolean b) {
    isImpliedByParent = b;
  }
  @Override
  public void agregarHijo(Choice choice) {
    hijos.add((Child)choice);
  }
  @Override
  public void remover(Choice choice) {
    hijos.remove((Child)choice);
  }
  @Override
  public boolean hasMultiplicity() {
    return multiplicity!=null;
  }
  @Override
  public Multiplicity getMultiplicity() {
    return multiplicity;
  }
  @Override
  public void setMultiplicity(int m) {
    switch(m){
      case Multiplicity.NORMAL:
        multiplicity = null;
        break;
      case Multiplicity.OR:
        if(multiplicity==null) multiplicity = Multiplicity.getOR();
        else multiplicity.upper = -1;
        break;
      case Multiplicity.XOR:
        if(multiplicity==null) multiplicity = Multiplicity.getXOR();
        else multiplicity.upper = 1;
        break;
    }
  }

  @Override
  public Choice clonar() {
    VSpec clon = new VSpec();
    clon.name = name;
    clon.type = type;
    clon.isImpliedByParent = true;
    return clon;
  }
}
package net.vjdv.cvlmaker.modelos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jassiel
 */
@XmlRootElement(name="ConfigurableUnit",namespace = "http:///cvl.ecore")
public class CVL implements Choice{
  @XmlAttribute(namespace = "http://www.omg.org/XMI")
  public String version = "2.0";
  @XmlAttribute
  public String name = "UnidadConfigurable";
  @XmlElement(name="ownedVSpec")
  public List<VSpec> hijos = new ArrayList<>();
  @Override
  public Choice[] obtenerHijos() {
    return hijos.toArray(new VSpec[hijos.size()]);
  }
  @Override
  public String obtenerNombre() {
    return name;
  }
  @Override
  public Boolean esObligatorio() {
    return false;
  }
  @Override
  public void setNombre(String n) {
    name = n;
  }
  @Override
  public void setObligatorio(boolean b) {
    throw new UnsupportedOperationException("Sin propiedad obligatorio");
  }
  @Override
  public void agregarHijo(Choice choice) {
    hijos.add((VSpec)choice);
  }
  @Override
  public void remover(Choice choice) {
    if(choice instanceof VSpec) hijos.remove((VSpec)choice);
  }
  @Override
  public boolean hasMultiplicity() {
    return false;
  }
  @Override
  public Multiplicity getMultiplicity() {
    return null;
  }
  @Override
  public void setMultiplicity(int m) {
    throw new UnsupportedOperationException("No soporta multiplicidad");
  }
  @Override
  public Choice clonar() {
    throw new UnsupportedOperationException("Raiz no se puede clonar.");
  }
}
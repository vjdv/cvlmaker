package net.vjdv.cvlmaker.modelos;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Jassiel
 */
public class Multiplicity {
  //Variables estáticas
  public final static int NORMAL = 1, OR = 2, XOR = 3;
  @XmlAttribute
  public int lower = 0, upper = -1;
  //Prefabricados
  public static Multiplicity getDefault(){
    return new Multiplicity();
  }
  public static Multiplicity getOR(){
    Multiplicity m = new Multiplicity();
    m.lower = 1;
    return m;
  }
  public static Multiplicity getXOR(){
    Multiplicity m = new Multiplicity();
    m.lower = 1;
    m.upper = 1;
    return m;
  }
}
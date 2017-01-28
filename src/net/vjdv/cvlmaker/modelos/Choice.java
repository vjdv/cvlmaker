package net.vjdv.cvlmaker.modelos;

/**
 * Cada opción
 * @author Jassiel
 */
public interface Choice {
  Choice[] obtenerHijos();
  String obtenerNombre();
  Boolean esObligatorio();
  void agregarHijo(Choice choice);
  void setNombre(String n);
  void setObligatorio(boolean b);
  void remover(Choice choice);
  boolean hasMultiplicity();
  Multiplicity getMultiplicity();
  void setMultiplicity(int m);
  Choice clonar();
}
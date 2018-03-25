package qcalc.function.fstack;

import java.lang.Double;

/**
 * Wraps around the Double class to allow it to refrence different values. This is nessecary for
 * use in the Function stack.
 */
public class Operand implements Symbol {
  
  private Double val;
  
  /**
   * Creates an Operand instance.
   * 
   * @param  tmp  The value of this Operand.
   */
  public Operand(double tmp) {
    val = tmp;
  }
  
  /**
   * Returns the value of this Operand.
   */
  public double getVal() {
    return val.doubleValue();
  }
  
  /**
   * Sets the value of this Operand.
   * 
   * @param  newVal  The new value of this Operand.
   */
  public void setVal(double newVal) {
    val = newVal;
  }
  
  /**
   * Returns a String representation of this Operand.
   */
  public String toString() {
    return val.toString();
  }
}

package qcalc.function.fstack;

import java.lang.IllegalArgumentException;
import java.util.Comparator;


/**
 * Operator definitions.   
 */
public class Operator implements Symbol {
  
  /**
   * Valid Operators. All operators that appear in this array must also be defined in the execute
   * method of this class. 
   */
  private static final char[][] DEFAULT_OPERATORS = new char[][] {
      {'^'},
      {'*', '/', '%'},
      {'+', '-'}
  };
  
  /**
   * Returns a comparator for this object backed by order of operations.
   */
  public static final Comparator<Operator> COMPARATOR = new Comparator<Operator>() {
      
      public int compare(Operator thisOp, Operator thatOp) {
        
        int delta = thisOp.priority - thatOp.priority;
        
        if(delta == 0) {
          return 0;
          
        } else {
          return delta > 0 ? 1 : -1;
        }
      }
  };
  
  //doesn't need to be public yet needs to be accesible from the outside
  final int priority;
  
  /**
   * Character represntation. The character representation of this operator.
   */
  public final char operator;
  
  /**
   * Constructor for Operators.
   * 
   * @param  op  Char representation of the Operator.
   */
  public Operator(char op) {
    
    int testPrio = getPriority(op);
    
    if(testPrio == -1) {
      throw new IllegalArgumentException("unsupported operator " + op);
      
    } else {
      priority = testPrio;
      operator = op;
    }
  }
  
  /**
   * Returns the priority of this operator.
   */
  private static final int getPriority(char op) {
    
    int priority = -1;
    for(int j = 0; j < DEFAULT_OPERATORS.length; j++) {
      
      String test = new String(DEFAULT_OPERATORS[j]);
      priority = test.indexOf(op) != -1 ? j : -1;
      if(priority != -1) {
        break;
      }
    }
    return priority;
  }
  
  /**
   * Takes adjacent operators, and preforms the operation represented by this operator upon the
   * operands.
   * 
   * @param  lHand  The left hand operator.
   * @param rHand  the right hand operator.
   * @return Returns the value of this operation.
   */
  public double execute(double lHand, double rHand) {
    
    switch(operator) {
      
      case '^' : return Math.pow(lHand, rHand);
      case '*' : return lHand * rHand;
      case '/' : return lHand / rHand;
      case '%' : return lHand % rHand;
      case '+' : return lHand + rHand;
      case '-' : return lHand - rHand;
      default : throw new IllegalArgumentException("execution failure for operator " + operator);
    }
  }
  
  /**
   * Returns list of all operators.
   */
  public static String listOperators() {
    
    String ret = new String(DEFAULT_OPERATORS[0]);
    for(int i = 1; i < DEFAULT_OPERATORS.length; i++) {
      
      ret = ret.concat(new String(DEFAULT_OPERATORS[i]));
    }
    return ret;
  }
  
  /**
   * Returns the String representation of this Operator.
   */
  public String toString() {
    char[] opString = {operator};
    return new String(opString);
  }
  
}

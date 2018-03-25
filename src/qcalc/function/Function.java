package qcalc.function;

import java.lang.IllegalArgumentException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import qcalc.function.fstack.Operator;

/**
 * A linear elementerary function for evaluation.
 */
public class Function {
  
  public static final String REGEX_DECIMAL = "(\\d++(\\.\\d++)?+)|(\\.\\d++)";
  public static final String REGEX_OPERATOR;
  
  static {
    String tmp = "[\\";
    tmp = tmp.concat(Operator.listOperators());
    tmp = tmp.concat("]");
    REGEX_OPERATOR = tmp;
  }
  
  /**
   * A regex String that matches to a real number.
   */
  static final String NORMAL_TERMS = 
      "((?<operand>" + "(^\\-)?+" + REGEX_DECIMAL + ")" 
      + "(?<operator>" + REGEX_OPERATOR + "?+))+?";
   
  /**
   * An ArrayList of all unexhausted symbols in the function stack. This 
   * ArrayList is not directly modifiable, but instead linked to a stack
   * of Operators. This ArrayList pops terms correlated with its operator
   * stack.
   */
  private final FunctionStack stack;
  
  private static final Double compile(String canidate, boolean verbose, boolean child) {
    Function toSolve = new Function(canidate, verbose, child);
    toSolve.solve(verbose);
    return new Double(toSolve.getStack().toString());
  }
  
  /**
   * Wraps compile(String canidate, false).
   * 
   * @param  canidate  The Function to evaluate.
   * @return  The Double of this Function's solution.
   }
   */
  public static final Double compile(String canidate) {
    return compile(canidate, false, false);
  }
  
  /**
   * Creates Function, returns value. The Function class obeys an order of
   * operations set by the Operators it contains, with the exception of 
   * terms enclosed in paranthesese(such terms are evluated first).
   * 
   * @param  canidate  The Function to evaluate.
   * @param  verbose  Print out each increment of simplification.
   * @return  The Double of this Function's solution.
   */
  public static final Double compile(String canidate, boolean verbose) {
    return compile(canidate, verbose, false);
  }
  
  private Function(String canidate, boolean verbose, boolean child) {
    
    if(verbose) {
    
      if(child) {
        System.out.println('(' + canidate + ')');
      } else {
        System.out.println(canidate);
      }
    }
    
    for(int openIndex = canidate.indexOf('('); openIndex != -1; openIndex = canidate.indexOf('(')) {
      String inner = resolveInner(openIndex, canidate);
      
      String innerSolution = 
          compile(inner.substring(1, inner.length() - 1), verbose, true).toString();
      if(verbose) {
        System.out.println(innerSolution);
      }
      canidate = canidate.replace(inner, innerSolution);
      if(verbose) {
        System.out.println(canidate);
      }
    }
    
    String validate = NORMAL_TERMS.substring(0, NORMAL_TERMS.length() - 2) + "+";
    if(!canidate.matches(validate)) {
      throw new IllegalArgumentException("function incorrectly formatted:\n" + canidate);
    }
    
    stack = new FunctionStack(canidate);
  }
  
  private static String resolveInner(int openingIndex, String canidate) {
    canidate =  canidate.substring(openingIndex, canidate.length());
    Matcher opening = Pattern.compile("\\(").matcher(canidate);
    Matcher closing = Pattern.compile("\\)").matcher(canidate);
    
    opening.find();
    
    int i = 1;
    do {
      
      if(closing.find()) {
        i--;
      }
      
      if(opening.find() && opening.start() < closing.start()) {
        i++;
      }            
    
      if(closing.hitEnd() && opening.hitEnd() && i > 0) {
        throw new IllegalArgumentException("unmatched () operator...");
      }
      
    } while(i > 0);
    
    return canidate.substring(0, closing.end());
  }
  
  private void solve(boolean verbose) {
    
    while(stack.simplify()) {
      
      if(verbose) {
        System.out.println(stack);
      }
    }
  }
  
  private FunctionStack getStack() {
    return stack;
  }
}

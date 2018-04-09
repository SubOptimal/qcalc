package function;

import function.Operator;
import java.lang.Double;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utility.ParenthResolver;

/**
 * Represents a function consisting of an alternating string of numerical values and operators.
 */
public class Function {
  
  private static final String REGEX_DECIMAL = "(?<Operand>(^-)?+(\\d++(\\.\\d++)?+)|(\\.\\d++))";
  private static final String OPERATOR = "(?<Operator>(?!^-)(?=\\D)[^.])";
  private static final String FUNCTION_MOULD = "(" + REGEX_DECIMAL + OPERATOR + "?+)++";

  //default operators
  private Operator[] operators = new Operator[] {
    new Operator('^', 0, (lOperand, rOperand) ->  Math.pow(lOperand, rOperand)),
    new Operator('*', 1, (lOperand, rOperand) -> lOperand * rOperand),
    new Operator('/', 1, (lOperand, rOperand) -> lOperand / rOperand),
    new Operator('%', 1, (lOperand, rOperand) -> lOperand % rOperand),
    new Operator('+', 2, (lOperand, rOperand) -> lOperand + rOperand),
    new Operator('-', 2, (lOperand, rOperand) -> lOperand - rOperand)
  };
  private final List<Object> fstack = new ArrayList<Object>();
  private final Stack<Operator> opstack = new Stack<Operator>();
  private final boolean verbose;
  
  /**
   * The constructor for this function.
   * 
   * @param  canidate  The input to be evaluated if it is a valid function.
   * @param  verbose  To print the solution verbosly.
   */
  public Function(String canidate, boolean verbose) {
    
    this.verbose = verbose;
    
    if(canidate.indexOf('(') != -1) {
      canidate = simplifyInner(canidate);
    }
    
    if(!isFunction(canidate)) {
      throw new IllegalArgumentException(canidate + " is invalid or contains invalid characters");
    }
    parseTerms(canidate);
  }

  private void setOperators(Operator[] operators) {
    this.operators = operators;
  }
  
  private  void parseTerms(String canidate) {
  
    Matcher operandParser = Pattern.compile(REGEX_DECIMAL).matcher(canidate);
    Matcher operatorParser = Pattern.compile(OPERATOR).matcher(canidate);
    while(operandParser.find()) {
      fstack.add(new Double(operandParser.group()));
      if(operatorParser.find()) {
        fstack.add(getMatchingOp(operatorParser.group().charAt(0)));
      }
    }
    
    ArrayList<Object> tmp = (ArrayList)fstack;
    tmp = (ArrayList)tmp.clone();
    tmp.removeIf((x) -> x.getClass() != Operator.class);
    ArrayList<Operator> toStack = (ArrayList)tmp;
    toStack.sort(Operator.COMPARATOR);
    
    ListIterator<Operator> reverseIterate = toStack.listIterator(toStack.size());
    while(reverseIterate.hasPrevious()) {
      opstack.push(reverseIterate.previous());
    }
  }
  
  private Operator getMatchingOp(char op) {
    
    for(Operator operator : operators) {
      if(operator.symbol == op) {
        return operator;
      }
    }
    throw new IllegalArgumentException(op + " is not a valid operator");
  }
  
  /**
   * Returns true if the string represents a Function.
   */
  public static boolean isFunction(String canidate) {
    return canidate.matches(FUNCTION_MOULD);
  }
  
  private String simplifyInner(String canidate) {
    for(int i = canidate.indexOf('('); i != -1; i = canidate.indexOf('(')) {
      String inner = ParenthResolver.resolveInner(i, canidate);
      String soltn = new Function(inner, verbose).solve().toString();
      inner = "(" + inner + ")";
      canidate = canidate.replace(inner, soltn);
    }
    return canidate;
  }
  
  /**
   * Reduces the Function by one level of complexity.
   */
  public boolean simplify() {
    
    if(opstack.isEmpty()) {
      return false;
    }
    Operator toConsume = opstack.pop();
    int opIndex = fstack.indexOf(toConsume);
    Double lHand = (Double)fstack.get(opIndex - 1);
    Double rHand = (Double)fstack.get(opIndex + 1);
    
    double simplified = toConsume.execute(lHand, rHand);
    fstack.set(opIndex - 1, simplified);
    fstack.remove(rHand);
    fstack.remove(toConsume);
    
    return true;
  }
  
  /**
   * Returns the solution of this function.
   */
  public Double solve() {
    
    do {
      if(verbose) {
        System.out.println(toString());
      }
    } while(simplify());
    
    return (Double)(fstack.get(0));
  }
  
  /**
   * Returns a string representing the current state of this Function.
   */
  @Override
  public String toString() {
    String ret = new String();
    for(Object term : fstack) {
      ret = ret.concat(term.toString());
    }
    return ret;
  }
}

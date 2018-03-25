package qcalc.function;

import static qcalc.function.Function.NORMAL_TERMS;

import java.lang.Double;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import qcalc.function.fstack.Operand;
import qcalc.function.fstack.Operator;
import qcalc.function.fstack.Symbol;


public class FunctionStack {
  
  private final ArrayList<Symbol> fstack = new ArrayList<>();
  private final Stack<Operator> opStack = new Stack<Operator>();
  
  FunctionStack(String validatedFun) {
    populateStack(validatedFun);
  }
  
  private void populateStack(String validatedFun) {
    
    Matcher parseTerm = Pattern.compile(NORMAL_TERMS).matcher(validatedFun);
    while(!parseTerm.hitEnd()) {
      
      parseTerm.find();
      String operand = parseTerm.group("operand");
      fstack.add(new Operand(new Double(operand)));

      if(parseTerm.group("operator") != null && !parseTerm.group("operator").isEmpty()) {
        String operator = parseTerm.group("operator");
        fstack.add(new Operator(operator.charAt(0)));
      }
    }
            
    ArrayList<Symbol> tmp = (ArrayList)fstack.clone();
    tmp.removeIf((x) -> x.getClass() != Operator.class);
    ArrayList<Operator> toStack = (ArrayList)tmp;
    toStack.sort(Operator.COMPARATOR);
    
    ListIterator<Operator> reverseIterate = toStack.listIterator(toStack.size());
    while(reverseIterate.hasPrevious()) {
      opStack.push(reverseIterate.previous());
    }
  }
  
  boolean simplify() {
    Operator toConsume = opStack.pop();
    int opIndex = fstack.indexOf(toConsume);
    Operand lHand = (Operand)fstack.get(opIndex - 1);
    Operand rHand = (Operand)fstack.get(opIndex + 1);
    
    double simplified = toConsume.execute(lHand.getVal(), rHand.getVal());
    lHand.setVal(simplified);
    fstack.remove(rHand);
    fstack.remove(toConsume);
    
    return !opStack.isEmpty();
  }
  
  @Override
  public String toString() {

    String ret = new String();
    for(Symbol s : fstack) {
      ret = ret.concat(s.toString());
    }
    return ret;
  }
  
}

package function;

import java.lang.Double;
import java.util.Comparator;
import java.util.function.BiFunction;


public class Operator {
  
  /**
   * The symbol which represents this Operator.
   */
  public final char symbol;
  /**
   * The priority of this operator. (0-inf). 0 is the greatest priority.
   */
  private final int priority;
  private final BiFunction<Double, Double, Double> operation;
  
  /**
   * Allows for Operators to be sorted by their priority.
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
  
  /**
   * The constructor for Operator instances. 
   * 
   * @param  symbol  The character representation of the operator.
   * @param  priority  The priority of the operator.
   * @param  operation  The operation to be carried about by Operation.execute().
   */
  Operator(char symbol, int priority, BiFunction<Double, Double, Double> operation) {
    this.priority = priority;
    this.symbol = symbol;
    this.operation = operation;
  }
  
  /**
   * Returns the Double value of the operation represented by this operator.
   */
  public Double execute(Double lOperand, Double rOperand) {
    return operation.apply(lOperand, rOperand);
  }
  
  /**
   * Returns the symbol representation of this operator.
   */
  public String toString() {
    return new String(new char[] {symbol});
  }
}

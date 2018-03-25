package main;

import java.util.Arrays;
import qcalc.function.Function;


/**
 * Entry point and args parser.
 */
@SuppressWarnings({"checkstyle:javadocmethod"})
public class Main {
  
  /**
   * Reads input. This entry point method parses user input into a valid
   * string to be passed to Funciton.compile().
   * 
   * @param  args  flags
   * @param  args  function String
   */
  public static void main(String[] args) {
    
    boolean verbose = false;
    if(args[0].equals("-v")) {
      verbose = true;
      args = Arrays.copyOfRange(args, 1, args.length);
    }
    
    String func = args[0];
    System.out.println(Function.compile(func, verbose));
  } 
}

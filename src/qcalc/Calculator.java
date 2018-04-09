package qcalc;

import function.Function;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import plugin.UserPlugin;

/**
 * Calculates a <code>function</code>.
 */
public class Calculator {

  private List<Flag> flags;
  private UserPlugin udef;
  private List<String> args;
  private boolean verbose = false;
  
  /**
   * The constructor for this calculator object.
   */
  public Calculator(UserPlugin plugin) {
    udef = plugin;
  }
  
  /**
   * Adds flags to be executed before function is processed.
   */
  public void addFlag(Flag newFlag) {
    
    if(!Optional.ofNullable(flags).isPresent()) {
      flags = new ArrayList<Flag>();
    }
    
    for(Flag defined : flags) {
      if(defined.name.equals(newFlag.name)) {
        throw new InputMismatchException(defined.name + "cannot be defined twice");
      }
    }
    flags.add(newFlag);
  }
  
  private void consumeFlag(Flag flag) {
    flag.execute();
  }
  
  /**
   * Processes the input. Prints the solution to the function if any was passed.
   */
  public void proccess(String[] tmp) {
    args = new ArrayList<String>(Arrays.asList(tmp));
    List<Flag> detected = flags.stream().filter((flag) -> Arrays.asList(args).contains(flag.name))
        .collect(Collectors.toList());
    detected.forEach(this::consumeFlag);
    
    String finalArg = args.get(args.size() - 1);
    
    if(finalArg.charAt(0) != '-' || finalArg.matches(".*\\d.*")) {
      String rawFunction = udef.mapToActual(finalArg);
      System.out.println(new Function(rawFunction, verbose).solve().toString());
    }
  }
  
  public UserPlugin getPlugin() {
    return udef;
  }
  
  public void setVerbosity(boolean verbose) {
    this.verbose = verbose;
  }
  
  public List<String> getArgs() {
    return args;
  }
}

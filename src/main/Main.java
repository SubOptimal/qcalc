package main;

import java.lang.Integer;
import java.util.List;
import plugin.UserDef;
import plugin.UserPlugin;
import qcalc.Calculator;
import qcalc.Flag;


public class Main {
  
  /**
   * Creates and configures a calculator for users to interact with.
   */
  public static void main(String[] args) {
    
    //set up calculator**********************************************
    Calculator calc = new Calculator(new UserDef());
    UserPlugin plugin = calc.getPlugin();
    
    calc.addFlag(new Flag("v", () -> calc.setVerbosity(true)));
    calc.addFlag(new Flag("D", () -> {
      int paramIndex = calc.getArgs().indexOf("-D") + 1;
      String param = calc.getArgs().get(paramIndex);
      plugin.writeDefinition(param);
      calc.getArgs().remove(paramIndex);
    }));
    calc.addFlag(new Flag("l", () -> {
      List<String> lines = plugin.getDefinitions();
      for(int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        System.out.println(i + ": " + line);
      }
    }));
    calc.addFlag(new Flag("rm", () -> {
      int paramIndex = calc.getArgs().indexOf("-rm") + 1;
      String param = calc.getArgs().get(paramIndex);
      plugin.removeDefinition(new Integer(param).intValue());
    }));
    //*****************************************************************
    
    calc.proccess(args);
  }
}

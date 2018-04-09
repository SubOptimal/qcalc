package qcalc;

/**
 * The Flag contains two fields. The name of the Flag and the method that it executes.
 */
public class Flag {
  
  public final String name;
  private final Procedure procedure;
  
  public Flag(String name, Procedure procedure) {
    this.name = '-' + name;
    this.procedure = procedure;
  }
  
  public void execute() {
    procedure.run();
  }
}

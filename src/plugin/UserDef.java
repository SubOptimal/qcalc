package plugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utility.ParenthResolver;


public final class UserDef implements UserPlugin {
  
  private static String UDEF_PATH;
  private static final ArrayList<String> definitions = new ArrayList<String>();
  private static final String PARAM = "(?<params>(\\p{Alpha}++,\\s)*+\\p{Alpha}++)";
  private static final String HEADER = "(?<methodName>\\p{Alpha}++)\\(" + PARAM + "\\)\\s";
  private static final String LAMDA = "\\-\\>\\s";
  private static final String BLUEPRINT = HEADER + LAMDA + "(?<function>.++)";
  private static Pattern validate = Pattern.compile(BLUEPRINT);
  
  static {
    
    BufferedReader readIn = null;
    try {
  
      String path = UserDef.class.getProtectionDomain().getCodeSource()
          .getLocation().toURI().getPath();
      path = path.substring(0, path.lastIndexOf(System.getProperty("file.separator").charAt(0)));
      path += "/udefs.clc";
      UDEF_PATH = path;
      readIn = new BufferedReader(new FileReader(path));
      
      readIn.lines().forEach(definitions::add);
      
    } catch(Exception e) {
      ;
    } finally {
      close(readIn);
      if(UDEF_PATH == null) {
        throw new java.lang.RuntimeException("path loading failed!!!");
      }
    }
  }
  
  private static void close(BufferedReader reader) {
    
    if(reader == null) {
      return;
    }
    
    try {
      reader.close();
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Writes a user defined function to the plugin script.
   */
  @Override
  public void writeDefinition(String canidate) {
    
    Matcher check = validate.matcher(canidate);
    if(check.matches()) {
      
      try(PrintWriter writer = new PrintWriter(new FileWriter(UDEF_PATH, true), true)) {
        writer.println(canidate);
      
      } catch(Exception e) {
        e.printStackTrace();
      }
        
    } else {
        
      System.out.println("Regex user function mould failed!!!\t" + canidate);
    }
  }
  
  /**
   * Returns true if the function appears to contain a user definiton.
   */
  @Override
  public boolean hasDefinition(String udef) {
    Matcher defParser = Pattern.compile("\\p{Alpha}++").matcher(udef);
    return defParser.find();
  }

  private String getDefName(String header) {
   
    Pattern methodName = Pattern.compile("^\\p{Alpha}++");
    Matcher findLName = methodName.matcher(header);
    String lName;
    if(findLName.find()) {
      lName = findLName.group();
      
    } else {
      throw new IllegalArgumentException("invalid def name in " + header);
    }
    
    return lName;
  }
  
  /**
   * Reduces to the definition to the series of operations that makes it up.
   * 
   * @return  actual  The constituent string of operands and operators.
   */
  @Override
  public String mapToActual(String udef) {
    
    if(!hasDefinition(udef)) {
      return udef;
    }
    
    String prototype = getMatching(udef);
    
    String localParams = getParams(udef);
    Scanner localParser = new Scanner(localParams);
    localParser.useDelimiter(",\\s");
    String remoteParams = getParams(prototype);
    Scanner remoteParser = new Scanner(remoteParams);
    remoteParser.useDelimiter(",\\s");
    
    Matcher getFunc = Pattern.compile(BLUEPRINT).matcher(prototype);
    getFunc.matches();
    String function = getFunc.group("function");
    
    while(localParser.hasNext() && remoteParser.hasNext()) {
      
      String localParam = localParser.next();
      String remoteParam = remoteParser.next();
      function = function.replaceAll(remoteParam, localParam);
    }
    
    localParser.close();
    remoteParser.close();
    
    return mapToActual(function);
  }
  
  private String getMatching(String udef) {
    
    int localParamCount = paramCount(udef);
    String localName = getDefName(udef);
    
    for(String remoteH : definitions) {
      
      int remoteParamCount = paramCount(remoteH);
      String remoteName = getDefName(remoteH);
      
      if(localParamCount == remoteParamCount && localName.equals(remoteName)) {
        return remoteH;
      }
    }
    
    return null;
  }
  
  private String getParams(String udef) {
    
    String params = ParenthResolver.resolveInner(udef.indexOf('('), udef);
    Scanner parser = new Scanner(params);
    parser.useDelimiter(",\\s");
    ArrayList<String> tmp = new ArrayList<String>();
    
    while(parser.hasNext()) {
      tmp.add(parser.next());
    }
    parser.close();
    
    return String.join(", ", tmp);
  }
  
  private int paramCount(String header) {
    
    String params = ParenthResolver.resolveInner(header.indexOf('('), header);
    Scanner parser = new Scanner(params);
    parser.useDelimiter(",\\s");
    
    int i = 0;
    while(parser.hasNext()) {   
      i++;
      parser.next();
      
    }
    
    parser.close();
    return i;
  }
  
  /**
   * Returns a list of all user definitions.
   */
  public List<String> getDefinitions() {
    return definitions;
  }
  
  /**
   * Removes the specified user definition.
   */
  public void removeDefinition(int line) {
    
    definitions.remove(line);
    
    try(PrintWriter writer = new PrintWriter(new FileWriter(UDEF_PATH, false), true)) {
      definitions.forEach(writer::println);
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}

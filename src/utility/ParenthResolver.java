package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class's only function is the resolution of a string contained within ().
 */
public final class ParenthResolver {
  
  /**
   * Returns String such that (String).
   */
  public static String resolveInner(int openingIndex, String canidate) {
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
    
    return canidate.substring(1, closing.end() - 1);
  }
}
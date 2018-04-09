package plugin;

import java.util.List;


public interface UserPlugin {
  
  /**
   * Returns arrayList of all definitions used by the implementing plugin.
   */
  List<String> getDefinitions();
  
  /**
   * Writes a defintion to this plugin's storage mechanism.
   */
  void writeDefinition(String def);
  
  /**
   * Removes a previously save defintion.
   */
  void removeDefinition(int line);
  
  /**
   * Returns true if the input uses a plugin-owned definition.
   */
  boolean hasDefinition(String input);
  
  /**
   * Maps a input String to an a userdefined output String.
   */
  String mapToActual(String func);
}

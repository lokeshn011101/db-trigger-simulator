package TriggerSimulator.QueryEngine.Commons;

public class Utils {
  public static String[] tokenize(String line) {
    return line.split("\\s+");
  }

  public static String[] tokenize(String line, String delimiter) {
    return line.split(delimiter);
  }

  public static QueryType getQueryType(String query) {
    switch (query) {
      case "CREATE":
        return QueryType.CREATE;
      case "INSERT":
        return QueryType.INSERT;
      case "UPDATE":
        return QueryType.UPDATE;
      case "DELETE":
        return QueryType.DELETE;
      default:
        return QueryType.READ;
    }
  }
}

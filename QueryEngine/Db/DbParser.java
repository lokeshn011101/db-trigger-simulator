package TriggerSimulator.QueryEngine.Db;

import TriggerSimulator.QueryEngine.Commons.QueryType;
import TriggerSimulator.QueryEngine.Commons.Utils;
import java.util.ArrayList;
import java.util.Arrays;

public class DbParser {

  private static String[] parseInsertQueryValues(String[] tokens) {
    ArrayList<String> values = new ArrayList<String>();
    for (int i = 5; i < tokens.length; ++i) values.add(tokens[i]);
    String valueString = String.join(" ", values.toArray(new String[0]));
    return Utils.tokenize(valueString, ",");
  }

  private static String[] parseInsertQueryColumns(String[] tokens) {
    ArrayList<String> values = new ArrayList<String>();
    for (
      int i = 3;
      i < Arrays.asList(tokens).indexOf("VALUES");
      ++i
    ) values.add(tokens[i]);
    String valueString = String.join(" ", values.toArray(new String[0]));
    return Utils.tokenize(valueString, ",");
  }

  private static String[] parseUpdateQueryConditions(String[] tokens) {
    ArrayList<String> conditions = new ArrayList<String>();
    for (
      int i = 3;
      i < Arrays.asList(tokens).indexOf("SET");
      ++i
    ) conditions.add(tokens[i]);
    return conditions.toArray(new String[0]);
  }

  private static String[] parseUpdateQueryValues(String[] tokens) {
    ArrayList<String> values = new ArrayList<String>();
    for (
      int i = Arrays.asList(tokens).indexOf("SET") + 1;
      i < tokens.length;
      ++i
    ) values.add(tokens[i]);
    return Utils.tokenize(String.join(" ", values.toArray(new String[0])), ",");
  }

  public static DbQuery getParsedQuery(String line) {
    String[] tokens = Utils.tokenize(line);
    QueryType queryType = Utils.getQueryType(tokens[0]);
    String table;

    if (queryType == QueryType.INSERT) {
      table = tokens[2];
    } else {
      table = tokens[1];
    }

    switch (queryType) {
      case INSERT:
        return new InsertQuery(
          queryType,
          table,
          parseInsertQueryValues(tokens),
          parseInsertQueryColumns(tokens)
        );
      case UPDATE:
        return new UpdateQuery(
          queryType,
          table,
          parseUpdateQueryConditions(tokens),
          parseUpdateQueryValues(tokens)
        );
      case DELETE:
        return new DeleteQuery(queryType, table);
      default:
        return new ReadQuery(queryType, table);
    }
  }
}

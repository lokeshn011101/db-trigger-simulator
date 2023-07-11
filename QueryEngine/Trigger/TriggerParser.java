package TriggerSimulator.QueryEngine.Trigger;

import TriggerSimulator.QueryEngine.Commons.QueryType;
import TriggerSimulator.QueryEngine.Commons.Utils;
import java.util.ArrayList;
import java.util.Arrays;

public class TriggerParser {

  private static String[] parseCreateQueryConditions(String[] tokens) {
    ArrayList<String> conditions = new ArrayList<String>();
    for (int i = 6; i < Arrays.asList(tokens).indexOf("DO"); ++i) {
      conditions.add(tokens[i]);
    }
    return conditions.toArray(new String[0]);
  }

  private static String[] parseCreateQueryActions(String[] tokens) {
    ArrayList<String> actions = new ArrayList<String>();
    for (
      int i = Arrays.asList(tokens).indexOf("DO") + 1;
      i < tokens.length;
      ++i
    ) actions.add(tokens[i]);
    return Utils.tokenize(
      String.join(" ", actions.toArray(new String[0])),
      ","
    );
  }

  public static TriggerQuery getParsedQuery(String line) {
    String[] tokens = Utils.tokenize(line);
    QueryType queryType = Utils.getQueryType(tokens[0]);
    String triggerName = tokens[2];

    switch (queryType) {
      case CREATE:
        {
          String table = tokens[4];
          return new CreateQuery(
            queryType,
            triggerName,
            table,
            parseCreateQueryConditions(tokens),
            parseCreateQueryActions(tokens)
          );
        }
      case DELETE:
        return new DeleteQuery(queryType, triggerName, "");
      default:
        return new ReadQuery(queryType, triggerName, "");
    }
  }
}

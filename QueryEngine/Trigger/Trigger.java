package TriggerSimulator.QueryEngine.Trigger;

import java.util.HashMap;

public class Trigger {
  private String name, table;
  private String[] columns;
  HashMap<String, String> actions, conditions;

  public Trigger(
    String name,
    String table,
    String[] conditions,
    String[] actions
  ) {
    this.name = name;
    this.table = table;
    this.columns = new String[conditions.length - 1];
    this.conditions = new HashMap<String, String>();
    this.actions = new HashMap<String, String>();

    for (int i = 0; i < conditions.length; i += 2) {
      String[] tokens;
      if (conditions[i].contains("=")) {
        tokens = conditions[i].split("=");
      } else {
        tokens = conditions[i].split("!=");
      }
      columns[i / 2] = tokens[0];
      this.conditions.put(tokens[0], tokens[1]);
    }
    for (int i = 0; i < actions.length; ++i) {
      String[] tokens = actions[i].split("=");
      this.actions.put(tokens[0], tokens[1]);
    }
  }

  public String getName() {
    return name;
  }

  public String getTable() {
    return table;
  }

  public HashMap<String, String> getConditions() {
    return conditions;
  }

  public String[] getTriggerColumns() {
    return columns;
  }

  public HashMap<String, String> getActions() {
    return actions;
  }

  @Override
  public String toString() {
    return (
      "Trigger" +
      "\nname=" +
      name +
      "\nactions=" +
      actions +
      "\nconditions=" +
      conditions +
      "\ntable=" +
      table +
      "\ncolumns=" +
      java.util.Arrays.toString(columns)
    );
  }
}

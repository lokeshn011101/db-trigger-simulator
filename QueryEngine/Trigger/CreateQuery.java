package TriggerSimulator.QueryEngine.Trigger;

import TriggerSimulator.QueryEngine.Commons.QueryType;

public class CreateQuery extends TriggerQuery {
  // Format: CREATE TRIGGER <trigger_name> ON <table_name> WHERE <conditions> DO <actions>
  // where conditions are separated by conditional operator AND and actions are separated by commas
  // Eg: CREATE TRIGGER dum ON dumt WHERE a=1 AND b=a AND x=ew AND ewr=234 DO a=1,c=2,d=4

  private String[] conditions, actions;

  public CreateQuery(
    QueryType queryType,
    String triggerName,
    String table,
    String[] conditions,
    String[] actions
  ) {
    super(queryType, triggerName, table);
    this.conditions = conditions;
    this.actions = actions;
  }

  public String[] getConditions() {
    return conditions;
  }

  public String[] getActions() {
    return actions;
  }

  @Override
  public String toString() {
    return (
      super.toString() +
      "\nConditions: " +
      String.join(", ", conditions) +
      "\nActions: " +
      String.join(", ", actions) +
      "\n"
    );
  }
}

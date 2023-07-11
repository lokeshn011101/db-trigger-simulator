package TriggerSimulator.QueryEngine.Trigger;

import TriggerSimulator.QueryEngine.Commons.QueryType;

public class DeleteQuery extends TriggerQuery {

  public DeleteQuery(QueryType queryType, String triggerName, String table) {
    super(queryType, triggerName, table);
  }
}

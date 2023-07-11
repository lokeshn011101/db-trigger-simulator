package TriggerSimulator.QueryEngine.Trigger;

import TriggerSimulator.QueryEngine.Commons.QueryType;

public class ReadQuery extends TriggerQuery {

  public ReadQuery(QueryType queryType, String triggerName, String table) {
    super(queryType, triggerName, table);
  }
}

package TriggerSimulator.QueryEngine.Trigger;

import TriggerSimulator.QueryEngine.Commons.AbstractQuery;
import TriggerSimulator.QueryEngine.Commons.QueryClass;
import TriggerSimulator.QueryEngine.Commons.QueryType;

public abstract class TriggerQuery extends AbstractQuery {
  private String triggerName, table;

  public TriggerQuery(QueryType queryType, String triggerName, String table) {
    super(queryType, QueryClass.TRIGGER);
    this.triggerName = triggerName;
    this.table = table;
  }

  public String getTriggerName() {
    return triggerName;
  }

  public String getTable() {
    return table;
  }

  @Override
  public String toString() {
    return (
      super.toString() + "\nTrigger Name: " + triggerName + "\nTable: " + table
    );
  }
}

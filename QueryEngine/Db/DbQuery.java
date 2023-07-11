package TriggerSimulator.QueryEngine.Db;

import TriggerSimulator.QueryEngine.Commons.AbstractQuery;
import TriggerSimulator.QueryEngine.Commons.QueryClass;
import TriggerSimulator.QueryEngine.Commons.QueryType;

public class DbQuery extends AbstractQuery {
  private String table;

  public DbQuery(QueryType queryType, String table) {
    super(queryType, QueryClass.DB);
    this.table = table;
  }

  public String getTable() {
    return table;
  }

  @Override
  public String toString() {
    return super.toString() + "\nTable: " + table;
  }
}

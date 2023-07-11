package TriggerSimulator.QueryEngine.Db;

import TriggerSimulator.QueryEngine.Commons.QueryType;

public class ReadQuery extends DbQuery {

  public ReadQuery(QueryType queryType, String table) {
    super(queryType, table);
  }
}

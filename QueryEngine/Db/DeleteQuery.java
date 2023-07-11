package TriggerSimulator.QueryEngine.Db;

import TriggerSimulator.QueryEngine.Commons.QueryType;

public class DeleteQuery extends DbQuery {

  public DeleteQuery(QueryType queryType, String table) {
    super(queryType, table);
  }
}

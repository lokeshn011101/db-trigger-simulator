package TriggerSimulator.QueryEngine.Db;

import java.util.HashMap;

import TriggerSimulator.QueryEngine.Commons.QueryType;

public class InsertQuery extends DbQuery {
  // Format - INSERT INTO <tableName> VALUES <values>
  // where values are comma separated
  // Eg: INSERT INTO dum c1,c2,c3,c4 VALUES a,b,c,d

  private String[] values, columns;

  public InsertQuery(QueryType queryType, String table, String[] values, String[] columns) {
    super(queryType, table);
    this.values = values;
    this.columns = columns;
  }

  public String[] getValues() {
    return values;
  }

  public String[] getColumns() {
    return columns;
  }

  public HashMap<String, String> getValuesMap() {
    HashMap<String, String> valuesMap = new HashMap<String, String>();
    for (int i = 0; i < columns.length; ++i) {
      valuesMap.put(columns[i], values[i]);
    }
    return valuesMap;
  }

  @Override
  public String toString() {
    return (super.toString() + "\nValues: " + String.join(", ", values) + "\n");
  }
}

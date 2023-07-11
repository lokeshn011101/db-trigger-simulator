package TriggerSimulator.QueryEngine.Db;

import TriggerSimulator.QueryEngine.Commons.QueryType;
import java.util.HashMap;

public class UpdateQuery extends DbQuery {
  // Format - UPDATE <tableName> SET <values> WHERE <conditions>
  // where conditions are comma separated and values are separated by conditional operator AND
  // Eg: UPDATE dum WHERE a=b AND b=c AND c=d SET d=123,e=1234

  public HashMap<String, String> values, conditions;
  String[] plainConditions, plainValues;

  public UpdateQuery(
    QueryType queryType,
    String table,
    String[] _conditions,
    String[] _values
  ) {
    super(queryType, table);
    this.plainConditions = _conditions;
    this.plainValues = _values;
    this.conditions = new HashMap<String, String>();
    this.values = new HashMap<String, String>();

    for (int i = 0; i < _conditions.length; i += 2) {
      String[] tokens;
      if (_conditions[i].contains("=")) {
        tokens = _conditions[i].split("=");
      } else {
        tokens = _conditions[i].split("!=");
      }
      this.conditions.put(tokens[0], tokens[1]);
    }
    for (int i = 0; i < _values.length; ++i) {
      String[] tokens = _values[i].split("=");
      this.values.put(tokens[0], tokens[1]);
    }
  }

  public HashMap<String, String> getConditions() {
    return this.conditions;
  }

  public HashMap<String, String> getValues() {
    return this.values;
  }

  @Override
  public String toString() {
    return (
      super.toString() +
      "\nConditions: " +
      String.join(", ", plainConditions) +
      "\nValues: " +
      String.join(", ", plainValues) +
      "\n"
    );
  }
}

package TriggerSimulator;

import TriggerSimulator.QueryEngine.Commons.AbstractQuery;
import TriggerSimulator.QueryEngine.Trigger.Trigger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

public class Store {
  private LinkedBlockingQueue<AbstractQuery> queryQueue;

  // Trigger name -> Trigger
  private HashMap<String, Trigger> triggers;

  // Table name -> Column name -> Trigger names
  private HashMap<String, HashMap<String, HashSet<String>>> tableTriggerHashMap;

  // Table name -> Rows
  private HashMap<String, ArrayList<HashMap<String, String>>> db;

  public Store() {
    queryQueue = new LinkedBlockingQueue<AbstractQuery>();
    triggers = new HashMap<String, Trigger>();
    tableTriggerHashMap =
      new HashMap<String, HashMap<String, HashSet<String>>>();
    db = new HashMap<String, ArrayList<HashMap<String, String>>>();
  }

  public void clear() {
    queryQueue.clear();
    triggers.clear();
    tableTriggerHashMap.clear();
    db.clear();
  }

  public void addIncomingQuery(AbstractQuery query) {
    queryQueue.add(query);
  }

  public AbstractQuery popQuery() {
    return queryQueue.poll();
  }

  public boolean isQueueEmpty() {
    return queryQueue.isEmpty();
  }

  public void addTrigger(Trigger trigger) {
    triggers.put(trigger.getName(), trigger);
    Arrays
      .stream(trigger.getTriggerColumns())
      .forEach(
        column -> {
          if (!tableTriggerHashMap.containsKey(trigger.getTable())) {
            tableTriggerHashMap.put(
              trigger.getTable(),
              new HashMap<String, HashSet<String>>()
            );
          }
          if (
            !tableTriggerHashMap.get(trigger.getTable()).containsKey(column)
          ) {
            tableTriggerHashMap
              .get(trigger.getTable())
              .put(column, new HashSet<String>());
          }
          tableTriggerHashMap
            .get(trigger.getTable())
            .get(column)
            .add(trigger.getName());
        }
      );
  }

  public void deleteTrigger(String triggerName) {
    Trigger trigger = triggers.get(triggerName);
    triggers.remove(triggerName);
    Arrays
      .stream(trigger.getTriggerColumns())
      .forEach(
        column -> {
          tableTriggerHashMap
            .get(trigger.getTable())
            .get(column)
            .remove(triggerName);
        }
      );
  }

  public Trigger getTrigger(String triggerName) {
    return triggers.get(triggerName);
  }

  public void associateTriggerWithTable(
    String triggerName,
    String tableName,
    String[] columnNames
  ) {
    if (!tableTriggerHashMap.containsKey(tableName)) {
      tableTriggerHashMap.put(
        tableName,
        new HashMap<String, HashSet<String>>()
      );
    }
    Arrays
      .stream(columnNames)
      .forEach(
        columnName -> {
          tableTriggerHashMap.get(tableName).get(columnName).add(triggerName);
        }
      );
  }

  public void insertRowInTable(String tableName, HashMap<String, String> row) {
    if (!db.containsKey(tableName)) {
      db.put(tableName, new ArrayList<HashMap<String, String>>());
    }
    db.get(tableName).add(row);
  }

  public void deleteRowInTable(
    String tableName,
    String columnName,
    String value
  ) {
    db.get(tableName).removeIf(row -> row.get(columnName).equals(value));
  }

  public void updateRowInTable(
    String tableName,
    String[] columnNames,
    String[] values,
    HashMap<String, String> row
  ) {
    db
      .get(tableName)
      .removeIf(oldRow -> checkForValueMatch(oldRow, columnNames, values));
    db.get(tableName).add(row);
  }

  private boolean checkForValueMatch(
    HashMap<String, String> row,
    String[] columnNames,
    String[] values
  ) {
    for (int i = 0; i < columnNames.length; i++) {
      if (!row.get(columnNames[i]).equals(values[i])) {
        return false;
      }
    }
    return true;
  }

  public boolean doesQueryTriggerTrigger(String table) {
    return (
      tableTriggerHashMap.containsKey(table) &&
      !tableTriggerHashMap.get(table).isEmpty()
    );
  }

  public HashSet<Trigger> getTriggersForTable(String table) {
    HashSet<Trigger> triggers = new HashSet<Trigger>();
    tableTriggerHashMap
      .getOrDefault(table, new HashMap<String, HashSet<String>>())
      .forEach(
        (column, triggerNames) -> {
          triggerNames.forEach(
            triggerName -> {
              triggers.add(this.triggers.get(triggerName));
            }
          );
        }
      );
    return triggers;
  }

  public void updateRowInTableWithConditions(
    String tableName,
    HashMap<String, String> actions,
    HashMap<String, String> conditions
  ) {
    ArrayList<HashMap<String, String>> rows = db.get(tableName);
    for (HashMap<String, String> row : rows) {
      for (String key : actions.keySet()) {
        if (!row.containsKey(key) || !conditionsMatchRow(row, conditions)) {
          continue;
        }
        row.put(key, actions.get(key));
      }
    }
    db.put(actions.get(tableName), rows);
  }

  private boolean conditionsMatchRow(
    HashMap<String, String> row,
    HashMap<String, String> conditions
  ) {
    for (String key : conditions.keySet()) {
      if (!row.containsKey(key) || !row.get(key).equals(conditions.get(key))) {
        return false;
      }
    }
    return true;
  }

  public void deleteTable(String tableName) {
    db.remove(tableName);
    HashSet<Trigger> triggers = getTriggersForTable(tableName);
    triggers.forEach(trigger -> this.deleteTrigger(trigger.getName()));
  }

  public ArrayList<HashMap<String, String>> getTable(String tableName) {
    return db.get(tableName);
  }
}

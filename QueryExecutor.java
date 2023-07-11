package TriggerSimulator;

import TriggerSimulator.QueryEngine.Commons.AbstractQuery;
import TriggerSimulator.QueryEngine.Commons.QueryClass;
import TriggerSimulator.QueryEngine.Db.DeleteQuery;
import TriggerSimulator.QueryEngine.Db.InsertQuery;
import TriggerSimulator.QueryEngine.Db.UpdateQuery;
import TriggerSimulator.QueryEngine.Trigger.CreateQuery;
import TriggerSimulator.QueryEngine.Trigger.ReadQuery;
import TriggerSimulator.QueryEngine.Trigger.Trigger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class QueryExecutor extends Thread {
  private Store store;

  @Override
  public void run() {
    while (true) {
      if (store.isQueueEmpty()) {
        continue;
      }

      AbstractQuery query = store.popQuery();
      switch (query.getQueryType()) {
        case INSERT:
          insertDbQuery((InsertQuery) query);
          break;
        case UPDATE:
          updateDbQuery((UpdateQuery) query);
          break;
        case DELETE:
          if (query.getQueryClass() == QueryClass.TRIGGER) {
            deleteTrigger(
              (TriggerSimulator.QueryEngine.Trigger.DeleteQuery) query
            );
          } else {
            deleteDbQuery((DeleteQuery) query);
          }
          break;
        case CREATE:
          createTrigger((CreateQuery) query);
          break;
        default:
          if (query.getQueryClass() == QueryClass.TRIGGER) {
            readTrigger((ReadQuery) query);
          } else {
            readDbQuery((TriggerSimulator.QueryEngine.Db.ReadQuery) query);
          }
          break;
      }
    }
  }

  private void createTrigger(CreateQuery query) {
    this.store.addTrigger(
        new Trigger(
          query.getTriggerName(),
          query.getTable(),
          query.getConditions(),
          query.getActions()
        )
      );
  }

  private void deleteTrigger(
    TriggerSimulator.QueryEngine.Trigger.DeleteQuery query
  ) {
    this.store.deleteTrigger(query.getTriggerName());
  }

  private Trigger readTrigger(ReadQuery query) {
    return this.store.getTrigger(query.getTriggerName());
  }

  private void insertDbQuery(InsertQuery query) {
    this.store.insertRowInTable(query.getTable(), query.getValuesMap());

    if (!this.store.doesQueryTriggerTrigger(query.getTable())) {
      return;
    }
    HashSet<Trigger> triggers =
      this.store.getTriggersForTable(query.getTable());
    triggers.forEach(trigger -> this.executeTrigger(trigger));
  }

  private void executeTrigger(Trigger trigger) {
    this.store.updateRowInTableWithConditions(
        trigger.getTable(),
        trigger.getActions(),
        trigger.getConditions()
      );
  }

  private void updateDbQuery(UpdateQuery query) {
    this.store.updateRowInTableWithConditions(
        query.getTable(),
        query.getValues(),
        query.getConditions()
      );
    HashSet<Trigger> triggers =
      this.store.getTriggersForTable(query.getTable());
    triggers.forEach(trigger -> this.executeTrigger(trigger));
  }

  private void deleteDbQuery(DeleteQuery query) {
    this.store.deleteTable(query.getTable());
  }

  private void readDbQuery(TriggerSimulator.QueryEngine.Db.ReadQuery query) {
    ArrayList<HashMap<String, String>> rows =
      this.store.getTable(query.getTable());
    System.out.println("Rows:\n" + rows);
  }

  public QueryExecutor(Store store) {
    this.store = store;
  }
}

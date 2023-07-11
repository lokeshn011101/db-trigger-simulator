package TriggerSimulator;

import static org.junit.Assert.assertEquals;

import TriggerSimulator.QueryEngine.Db.DbParser;
import TriggerSimulator.QueryEngine.Trigger.TriggerParser;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {

  public static void causeDelay() {
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (Exception e) {
      System.out.println("Oops! Something went wrong!");
    }
  }

  public static void reinitializeStore() {
    Main.store.clear();
    populateStore();
  }

  public static void populateStore() {
    int i = 0, j = 100;
    for (; i < 10; ++i) {
      String q = String.format(
        "INSERT INTO t1 c1,c2,c3,c4,c5 VALUES a%d,%d,b%d,%d,%d",
        i,
        i,
        i,
        j,
        i % 2
      );
      Main.store.addIncomingQuery(DbParser.getParsedQuery(q));
      ++j;
    }
    causeDelay();
  }

  @BeforeClass
  public static void setup() {
    Main.queryExecutor.start();
    populateStore();
  }

  @Test
  public void testUpdate() {
    reinitializeStore();
    String q = "UPDATE t1 WHERE c5=1 SET c2=111,c4=222";
    Main.store.addIncomingQuery(DbParser.getParsedQuery(q));
    causeDelay();
    assertEquals(
      "",
      "[{c3=b0, c4=100, c5=0, c1=a0, c2=0}, {c3=b1, c4=222, c5=1, c1=a1, c2=111}, {c3=b2, c4=102, c5=0, c1=a2, c2=2}, {c3=b3, c4=222, c5=1, c1=a3, c2=111}, {c3=b4, c4=104, c5=0, c1=a4, c2=4}, {c3=b5, c4=222, c5=1, c1=a5, c2=111}, {c3=b6, c4=106, c5=0, c1=a6, c2=6}, {c3=b7, c4=222, c5=1, c1=a7, c2=111}, {c3=b8, c4=108, c5=0, c1=a8, c2=8}, {c3=b9, c4=222, c5=1, c1=a9, c2=111}]",
      Main.store.getTable("t1").toString()
    );
  }

  @Test
  public void testTriggerExecution() {
    reinitializeStore();
    String q = "CREATE TRIGGER tr1 ON t1 WHERE c3=b0 AND c2=0 DO c1=abcdef";
    Main.store.addIncomingQuery(TriggerParser.getParsedQuery(q));
    q = "UPDATE t1 WHERE c3=b0 AND c2=0 SET c4=333";
    Main.store.addIncomingQuery(DbParser.getParsedQuery(q));
    causeDelay();
    assertEquals(
      "",
      "[{c3=b0, c4=333, c5=0, c1=abcdef, c2=0}, {c3=b1, c4=101, c5=1, c1=a1, c2=1}, {c3=b2, c4=102, c5=0, c1=a2, c2=2}, {c3=b3, c4=103, c5=1, c1=a3, c2=3}, {c3=b4, c4=104, c5=0, c1=a4, c2=4}, {c3=b5, c4=105, c5=1, c1=a5, c2=5}, {c3=b6, c4=106, c5=0, c1=a6, c2=6}, {c3=b7, c4=107, c5=1, c1=a7, c2=7}, {c3=b8, c4=108, c5=0, c1=a8, c2=8}, {c3=b9, c4=109, c5=1, c1=a9, c2=9}]",
      Main.store.getTable("t1").toString()
    );
  }

  @Test
  public void testTableContents() {
    reinitializeStore();
    assertEquals(
      "",
      "[{c3=b0, c4=100, c5=0, c1=a0, c2=0}, {c3=b1, c4=101, c5=1, c1=a1, c2=1}, {c3=b2, c4=102, c5=0, c1=a2, c2=2}, {c3=b3, c4=103, c5=1, c1=a3, c2=3}, {c3=b4, c4=104, c5=0, c1=a4, c2=4}, {c3=b5, c4=105, c5=1, c1=a5, c2=5}, {c3=b6, c4=106, c5=0, c1=a6, c2=6}, {c3=b7, c4=107, c5=1, c1=a7, c2=7}, {c3=b8, c4=108, c5=0, c1=a8, c2=8}, {c3=b9, c4=109, c5=1, c1=a9, c2=9}]",
      Main.store.getTable("t1").toString()
    );
  }

  @Test
  public void testTriggerCreation() {
    reinitializeStore();
    String q = "CREATE TRIGGER tr1 ON t1 WHERE c5=0 AND c2=111 DO c3=abcdef";
    Main.store.addIncomingQuery(TriggerParser.getParsedQuery(q));
    causeDelay();
    assertEquals(
      "q",
      "Trigger\n" +
      "name=tr1\n" +
      "actions={c3=abcdef}\n" +
      "conditions={c5=0, c2=111}\n" +
      "table=t1\n" +
      "columns=[c5, c2]",
      Main.store.getTrigger("tr1").toString()
    );
  }
}

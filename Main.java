package TriggerSimulator;

import TriggerSimulator.QueryEngine.Db.DbParser;
import TriggerSimulator.QueryEngine.Trigger.TriggerParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
  public static Scanner scanner = new Scanner(System.in);
  public static Store store = new Store();
  public static QueryExecutor queryExecutor = new QueryExecutor(store);

  public static void main(String[] args) {
    queryExecutor.start();

    while (true) {
      ArrayList<String> inputs = getUserInput();
      String processedInput = processUserInput(inputs.get(0), inputs.get(1));
      if (processedInput.equals("EXIT")) {
        System.out.println("Exiting...");
        break;
      }
    }
  }

  private static ArrayList<String> getUserInput() {
    System.out.print(
      "Enter query class:\n1. TRIGGER\n2. DB\n3. EXIT\nYour choice: "
    );
    String query = scanner.nextLine();
    System.out.print("Enter query: ");
    String queryLine = scanner.nextLine();

    return new ArrayList<>(Arrays.asList(query, queryLine));
  }

  private static String processUserInput(String query, String queryLine) {
    switch (query) {
      case "EXIT":
      case "3":
        return "EXIT";
      case "TRIGGER":
      case "1":
        store.addIncomingQuery(TriggerParser.getParsedQuery(queryLine));
        break;
      case "DB":
      case "2":
        store.addIncomingQuery(DbParser.getParsedQuery(queryLine));
        break;
      default:
        return "EXIT";
    }

    return query;
  }
}

# Database Triggers Simulator

This is an attempt to simulate database triggers on the application side during any update or insert operations on the DB.

The DB is an in-memory hashmap with a very naive specification language for queries.

To run this, clone the repo and execute the below command.

1. For Windows
   `Remove-Item -Path .\bin -Recurse ; javac -d ./bin ./TriggerSimulator/Main.java  ; java -cp .\bin TriggerSimulator.Main`
2. For Linux
   `sudo rm -rf ./bin ; javac -d ./bin ./TriggerSimulator/Main.java  ; java -cp ./bin TriggerSimulator.Main`

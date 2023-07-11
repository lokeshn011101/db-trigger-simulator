package TriggerSimulator.QueryEngine.Commons;

public abstract class AbstractQuery {
  private QueryType queryType;
  private QueryClass queryClass;

  public AbstractQuery(QueryType queryType, QueryClass queryClass) {
    this.queryType = queryType;
    this.queryClass = queryClass;
  }

  public QueryType getQueryType() {
    return queryType;
  }

  public QueryClass getQueryClass() {
    return queryClass;
  }

  @Override
  public String toString() {
    return (
      "\nQuery Class: " +
      queryClass.toString() +
      "\nQuery Type: " +
      queryType.toString()
    );
  }
}

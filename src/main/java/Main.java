import java.io.IOException;
import java.util.List;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

public class Main {
  static final String TABLE = System.getProperty("bigtable.table");
  static final String FAMILY = System.getProperty("bigtable.family");

  public static void main(String[] args) throws IOException {
    Connection connection = ConnectionFactory.createConnection(HBaseConfiguration.create());

    Table table = connection.getTable(TableName.valueOf(TABLE));

    while (true) {
      System.out.println("readRow");
      System.out.println(table.get(new Get("greeting0".getBytes())));

      System.out.println("ReadRows (multiget)");
      Result[] results = table.get(List.of(
          new Get("greeting0".getBytes()),
          new Get("greeting1".getBytes())
      ));
      for (Result result : results) {
        System.out.println(result);
      }

      System.out.println("ReadRows (scan)");
      ResultScanner scanner = table.getScanner(
          new Scan().setRowPrefixFilter("greeting".getBytes()));
      for (Result result : scanner) {
        System.out.println(result);
      }

      System.out.println("MutateRow (put)");
      table.put(
          new Put("some-key".getBytes())
              .addColumn(FAMILY.getBytes(), "q".getBytes(), "value".getBytes())
      );

      System.out.println("MutateRows (put(List))");
      table.put(
          List.of(
              new Put("some-key".getBytes())
                  .addColumn(FAMILY.getBytes(), "q".getBytes(), "value".getBytes()),
              new Put("some-other-key".getBytes())
                  .addColumn(FAMILY.getBytes(), "q".getBytes(), "value".getBytes())
          )
      );

      System.out.println("CheckAndMutate");
      table.checkAndMutate(
          "some-row".getBytes(),
          FAMILY.getBytes(),
          "q".getBytes(),
          CompareOperator.EQUAL,
          "some-value".getBytes(),
          RowMutations.of(
              List.of(
                new Put("some-row".getBytes()).addColumn(FAMILY.getBytes(), "q2".getBytes(), "value".getBytes())
              )
          )
      );

      System.out.println("ReadModifyWrite (increment)");
      table.increment(new Increment("some-row".getBytes()).addColumn(FAMILY.getBytes(), "counter".getBytes(), 1));
    }
  }
}

This is a simple example of how bigtable-hbase can be used.

```sh
mvn compile exec:java \
    -Dexec.mainClass=Main \
    -Dbigtable.project=<YOUR_PROJECT_ID> \
    -Dbigtable.instance=<YOUR_INSTANCE_ID> \
    -Dbigtable.app_profile=<YOUR_APP_PROFILE_ID> \
    -Dbigtable.table=<YOUR_TABLE_ID> \
    -Dbigtable.family=<YOUR_FAMILY>
```

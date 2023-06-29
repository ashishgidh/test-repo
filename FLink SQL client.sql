
================================================================================
USE CASE-3: ATM Transaction data based fraud Analytics - USING SQL CLIENT
# Generate Simulated Data using python application to Kafka. 
# Flink, Query, Apply Windowing (Tumbling/Hopping) and create alerts based upon conditions.
# Read from Kafka-> Process in Flink Tables -> Produce back to Kafka Alert Topic. 
================================================================================

Check for # TX on a card in last 2 minutes.

1. Generate data to a topic -> using python program

2. Create a flink table with watermarking(for event based windowing later) from kafka rt_test1 topic.
    * SQL-client
    - Using Watermark for handle completeness of data, using event time.
        CREATE TABLE transactions (
        `account_id` STRING,
        `atm` STRING,
        `location` String,
        `amount` INTEGER,
        `transaction_id` STRING,
        `timestamp` Timestamp(3) ,
        `ts` TIMESTAMP(3) METADATA FROM 'timestamp',
        WATERMARK FOR `timestamp` AS `timestamp` - INTERVAL '30' SECONDS
        ) WITH (
        'connector' = 'kafka',
        'topic' = 'rt_test1',
        'properties.bootstrap.servers' = 'shared-kafka-na-sit.chubbdigital.com:9092',
        'properties.group.id' = 'group.rt.readings',
        'properties.ssl.truststore.password'='123456',
        'properties.ssl.truststore.location'='/opt/apps/chubbio/ajbisht/kafka.client.truststore.jks',
        'properties.sasl.mechanism'='PLAIN',
        'properties.security.protocol'='SASL_SSL',
        'properties.sasl.jaas.config'='org.apache.kafka.common.security.plain.PlainLoginModule required username="flinkuser" password="NDEtbGltaXRpbmctcmlmbGVzLUFycmFuZ2U=";',
        'scan.startup.mode' = 'earliest-offset',
        'format' = 'json'
        );
3. Create a temp view with tumblilng window.
    * SQL-client
    - Applying 2 minute Tumble over kafka backed table.
        create view tempTumble as select window_start,window_end,account_id,sum(amount) as amount,count(transaction_id) as numberoftx,
        listagg(atm) as atms from TABLE(TUMBLE(TABLE transactions, DESCRIPTOR(`timestamp`),
        INTERVAL '2' MINUTES))
        group by account_id,window_start,window_end;

4. Create a temp view with Hopping window.
    * SQL-client
    - Applying Hoppping window of 2 minutes with 30 seconds-Overlapping
        create view tempHop as select window_start,window_end,account_id,sum(amount) as amount,count(transaction_id) as numberoftx,
        listagg(atm) as atms from TABLE(HOP(TABLE transactions, DESCRIPTOR(`timestamp`),INTERVAL '30' SECONDS, INTERVAL '2' MINUTES))
        group by account_id,window_start,window_end;


5. Create an alert fiilter based on #transactions and produce to kafka.
    * SQL-client
    - Create a table in flink backed by Kafka topic and inserting filtered Tx. 
        create table tempTumbleKafka with (
        'connector' = 'kafka',
        'topic' = 'rt_test_TumbleKafka_C',
        'properties.bootstrap.servers' = 'shared-kafka-na-sit.chubbdigital.com:9092',
        'properties.ssl.truststore.password'='123456',
        'properties.ssl.truststore.location'='/opt/apps/chubbio/ajbisht/kafka.client.truststore.jks',
        'properties.sasl.mechanism'='PLAIN',
        'properties.security.protocol'='SASL_SSL',
        'properties.sasl.jaas.config'='org.apache.kafka.common.security.plain.PlainLoginModule required username="flinkuser" password="NDEtbGltaXRpbmctcmlmbGVzLUFycmFuZ2U=";',
        'scan.startup.mode' = 'earliest-offset',
        'format' = 'json',
        'key.format' = 'raw',
         'key.fields' = 'account_id'
         ) as  select * from tempTumble where numberoftx > 2 ;

6. Observe the result on kafka.

    * AKHQ
        - Observe the topic rt_test_TumbleKafka_C, this is a compacted topic with key field as well. We always see updated value for any given account.
    
7. Enrich data using MSSQL.
    * SQL-client
    - Bring MSSQL table to flink to enrich the data
        CREATE TABLE users (
        account_id String,
        name String
        )with (  'connector' = 'jdbc',
        'url' = 'jdbc:sqlserver://SQLV2006SN1.aceins.com:1433;databaseName=EXPMOD;encrypt=true;trustServerCertificate=true',
        'table-name' = 'users',
        'username'='iccdbb',
        'password'='eiicc0111@'
    );

7. Enriched data    
    * SQL-client
    - Observe the Enriched data;
        select t2.name,t1.account_id,t1.amount,t1.numberoftx from tempTumble as t1 join users as t2 on t1.account_id=t2.account_id;


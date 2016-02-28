from pyspark import SparkContext

from pyspark import SparkConf

conf = SparkConf()
conf.setAppName('forecast manager')
sc = SparkContext(conf=conf)


from pyspark.sql import HiveContext


sqlContext = HiveContext(sc)
df = sqlContext.sql("select * from health_table")
df.select("year", "state", "category").write.parquet("/tmp/yearstatecat.parquet")
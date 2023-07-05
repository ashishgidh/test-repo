test-repo
=========

Test Repository


kamel run --name camelsample10 ./Sample1.java ./Sample2.java ./Sample3.java ./Sample4.java ./Sample5.java ./Sample6.java ./Sample7.java ./Sample8.java ./Sample9.java ./Sample10.java  -n camelk --profile Kubernetes


mvn archetype:generate -DarchetypeGroupId=org.apache.camel.archetypes -DarchetypeArtifactId=camel-archetype-java -DarchetypeVersion=3.21.0


kamel run --name camelkjar -d ./target/camelkjar-1.0-SNAPSHOT.jar   -n camelk --profile Kubernetes

kamel run  -d ./target/camelkjar-1.0-SNAPSHOT.jar   -n camelk --profile Kubernetes

kamel run -d file:./target/camelkjar-1.0-SNAPSHOT.jar ./Sample1.java --name camelkjar  -n camelk --profile Kubernetes
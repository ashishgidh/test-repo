 

import org.apache.camel.builder.RouteBuilder;

public class Sample1 extends RouteBuilder {
  @Override
  public void configure() throws Exception {
	  from("timer:tick?period=3000")
        .log("Hello Camel K! sample1");
  }
}
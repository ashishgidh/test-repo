 

import org.apache.camel.builder.RouteBuilder;

public class Sample2 extends RouteBuilder {
  @Override
  public void configure() throws Exception {
	  from("timer:tick?period=3000")
        .log("Hello Camel K! Sample2");
  }
}
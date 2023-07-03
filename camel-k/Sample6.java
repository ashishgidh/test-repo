 

import org.apache.camel.builder.RouteBuilder;

public class Sample6 extends RouteBuilder {
  @Override
  public void configure() throws Exception {
	  from("timer:tick?period=3000")
        .log("Hello Camel K! Sample6");
  }
}
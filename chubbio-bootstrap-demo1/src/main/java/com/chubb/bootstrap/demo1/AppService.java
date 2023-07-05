package com.chubb.bootstrap.demo1;

import org.apache.camel.Exchange;

public class AppService {

    public void setBody(Exchange exchange)  {
        try {
            exchange.getIn().setBody("hello ashish Gidh Ash");
     
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

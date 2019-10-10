package com.example.demo;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class WebServerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

  @Override
  public void customize(TomcatServletWebServerFactory factory) {
    factory.addContextCustomizers(context -> {
      SecurityConstraint securityConstraint = new SecurityConstraint();
      securityConstraint.setUserConstraint("CONFIDENTIAL");
      SecurityCollection collection = new SecurityCollection();
      collection.addPattern("/*");
      securityConstraint.addCollection(collection);
      context.addConstraint(securityConstraint);
    });
    factory.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());
  }

  private Connector httpToHttpsRedirectConnector() {
    Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
    connector.setScheme("http");
    connector.setPort(8080);
    connector.setSecure(false);
    connector.setRedirectPort(8443);
    return connector;
  }

}

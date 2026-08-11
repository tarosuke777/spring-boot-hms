package com.tarosuke777.hms.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("appProperties")
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  private String baseDomain;
  private Map<String, String> services = new HashMap<>();

  public String getBaseDomain() {
    return baseDomain;
  }

  public void setBaseDomain(String baseDomain) {
    this.baseDomain = baseDomain;
  }

  public Map<String, String> getServices() {
    return services;
  }

  public void setServices(Map<String, String> services) {
    this.services = services;
  }
}

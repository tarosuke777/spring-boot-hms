package arpa.home.hms.config;

import java.util.Map;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AppModelAdvice {

  private final AppProperties appProperties;

  public AppModelAdvice(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  @ModelAttribute("appServices")
  public Map<String, String> appServices() {
    return appProperties.getServices();
  }
}

package ch.tbmelabs.tv.core.configserver;

import ch.tbmelabs.tv.shared.constants.spring.SpringApplicationProfile;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.Environment;

@EnableConfigServer
@SpringCloudApplication
public class Application {

  private Environment environment;

  public Application(Environment environment) {
    this.environment = environment;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @PostConstruct
  public void initBean() {
    if (environment.acceptsProfiles(SpringApplicationProfile.PROD)
        && environment.acceptsProfiles(SpringApplicationProfile.DEV)) {
      throw new IllegalArgumentException(
          "Do not attempt to run an application in productive and development environment at the same time!");
    }
  }
}
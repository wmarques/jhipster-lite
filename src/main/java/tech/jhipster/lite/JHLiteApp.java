package tech.jhipster.lite;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class JHLiteApp {

  private static final Logger log = LoggerFactory.getLogger(JHLiteApp.class);

  private static final String SEPARATOR = "----------------------------------------------------------";

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(JHLiteApp.class);
    Environment env = app.run(args).getEnvironment();
    logApplicationStartup(env);
  }

  private static void logApplicationStartup(Environment env) {
    String protocol = getProtocol(env.getProperty("server.ssl.key-store"));
    String serverPort = env.getProperty("server.port");
    String contextPath = getContextPath(env.getProperty("server.servlet.context-path"));
    String hostAddress = getHostAddress();

    String welcomeMessage = new StringBuilder()
      .append("\n")
      .append(SEPARATOR)
      .append("\n")
      .append(applicationRunning(env.getProperty("spring.application.name")))
      .append(accessUrlLocal(protocol, serverPort, contextPath))
      .append(accessUrlExternal(protocol, hostAddress, serverPort, contextPath))
      .append(profile(Arrays.toString(env.getActiveProfiles())))
      .append(SEPARATOR)
      .append(configServer(env.getProperty("configserver.status")))
      .toString();

    log.info(welcomeMessage);
  }

  public static String applicationRunning(String value) {
    return String.format("  Application '%s' is running!", value) + "\n";
  }

  public static String accessUrlLocal(String protocol, String serverPort, String contextPath) {
    if (StringUtils.isBlank(serverPort)) {
      return "";
    }
    return String.format("  Local: \t%s://localhost:%s%sswagger-ui.html", protocol, serverPort, contextPath) + "\n";
  }

  public static String accessUrlExternal(String protocol, String hostAddress, String serverPort, String contextPath) {
    if (StringUtils.isBlank(serverPort)) {
      return "";
    }
    return String.format("  External: \t%s://%s:%s%sswagger-ui.html", protocol, hostAddress, serverPort, contextPath) + "\n";
  }

  public static String profile(String profiles) {
    return String.format("  Profile(s): \t%s", profiles) + "\n";
  }

  public static String configServer(String configServerStatus) {
    if (StringUtils.isBlank(configServerStatus)) {
      return "";
    }
    return "\n" + String.format("  Config Server: %s", configServerStatus) + "\n" + SEPARATOR + "\n";
  }

  public static String getProtocol(String value) {
    return Optional.ofNullable(value).map(key -> "https").orElse("http");
  }

  public static String getContextPath(String value) {
    return Optional.ofNullable(value).filter(StringUtils::isNotBlank).orElse("/");
  }

  public static String getHostAddress() {
    String hostAddress = "localhost";
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.warn("The host name could not be determined, using `localhost` as fallback");
    }
    return hostAddress;
  }
}

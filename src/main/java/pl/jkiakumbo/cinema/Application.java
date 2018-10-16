package pl.jkiakumbo.cinema;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import pl.jkiakumbo.cinema.embedded.EmbeddedJetty;

public class Application {

    private static final String SPRING_PROFILES_ACTIVE = "Spring.profiles.active";
    private static final String SPRING_DEFAULT_PROFILE = "dev";
    private static final String APPLICATION_PORT = "PORT";
    private static final String SPRING_DEFAULT_PORT = "8080";

    public static void main(String[] args) throws Exception {

        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.baziaka.config");

        String webPort = System.getenv(APPLICATION_PORT);
        if (webPort == null || webPort.isEmpty()) {
            webPort = SPRING_DEFAULT_PORT;
        }

        String profile = System.getProperty(SPRING_PROFILES_ACTIVE, SPRING_DEFAULT_PROFILE);

        new EmbeddedJetty(context, Integer.valueOf(webPort), profile);
    }
}

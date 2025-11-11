package takehomechallenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class SwaggerAutoOpenConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @EventListener(ApplicationReadyEvent.class)
    public void openSwaggerInBrowser() {
        String url = "http://localhost:" + serverPort + "/swagger-ui.html";

        System.out.println("\n" + "=".repeat(70));
        System.out.println("🚀 Application started successfully!");
        System.out.println("📚 Swagger UI: " + url);
        System.out.println("=".repeat(70) + "\n");

        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                System.out.println("✅ Opening Swagger in browser...\n");
            } else if (os.contains("mac")) {
                // macOS
                Runtime.getRuntime().exec("open " + url);
                System.out.println("✅ Opening Swagger in browser...\n");
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                Runtime.getRuntime().exec("xdg-open " + url);
                System.out.println("✅ Opening Swagger in browser...\n");
            } else {
                // Fallback to Desktop API
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(URI.create(url));
                    System.out.println("✅ Opening Swagger in browser...\n");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️  Could not open browser automatically");
            System.out.println("👉 Open manually: " + url + "\n");
        }
    }
}

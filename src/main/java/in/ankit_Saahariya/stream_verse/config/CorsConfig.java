package in.ankit_Saahariya.stream_verse.config;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class CorsConfig  implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origin:http://localhost:4200")
    private String[] allowedOrigins;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET","PUT","POST","DELETE","OPTIONS,PATCH")
                .allowedHeaders("*")
                .allowedHeaders("Location","Content-Disposition")
                .allowCredentials(false)
                .maxAge(3600);
    }
}

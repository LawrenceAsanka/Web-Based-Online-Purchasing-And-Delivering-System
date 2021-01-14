package lk.bit.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@SpringBootApplication
public class AppInitializer implements WebMvcConfigurer {

    @Value("${static.path}")
    private String staticPath;

    public static void main(String[] args) {
        SpringApplication.run(AppInitializer.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///" + staticPath + "/");
    }

}

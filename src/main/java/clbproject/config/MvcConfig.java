package clbproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Value("${download.path}")
    private String downloadloadPath;

    @Value("${manupload.path}")
    private String manUpPath;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }


    @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

            registry.addResourceHandler("/reports/**")
                    .addResourceLocations("file:/" + downloadloadPath + "/");

        registry.addResourceHandler("/docs/**")
                .addResourceLocations("file:/" + manUpPath + "/");

            registry.addResourceHandler("/img/**")
                    .addResourceLocations("classpath:/img/");

            registry.addResourceHandler("/js/**")
                    .addResourceLocations("classpath:/js/");

    }
}
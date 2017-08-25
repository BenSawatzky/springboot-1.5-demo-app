package answer.king.config;

import com.google.common.base.Predicate;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@EnableTransactionManagement
public class AnswerKingConfig {

  @Bean
  public ServletRegistrationBean h2ServletRegistration() {
    return new ServletRegistrationBean(new WebServlet(), "/h2/*");
  }

  @Bean
  public Docket swaggerSpringMvcPlugin() {
    Predicate<String> apiPaths = or(regex("/order.*"), regex("/item.*"), regex("/receipt.*"));
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .paths(apiPaths)
        .build();
  }
}

package com.binchencoder.oauth2.sso;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

public class ServletInitializer extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(SsoApplication.class);
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    super.onStartup(servletContext);
    FilterRegistration.Dynamic encodingFilter = servletContext
        .addFilter("encoding-filter", CharacterEncodingFilter.class);
    encodingFilter.setInitParameter("encoding", "UTF-8");
    encodingFilter.setInitParameter("forceEncoding", "true");
    encodingFilter.setAsyncSupported(true);
    encodingFilter.addMappingForUrlPatterns(null, false, "/*");
    ServletRegistration.Dynamic kaptchaServlet = servletContext
        .addServlet("kaptcha-servlet", KaptchaServlet.class);
    kaptchaServlet.addMapping("/except/kaptcha");
  }
}

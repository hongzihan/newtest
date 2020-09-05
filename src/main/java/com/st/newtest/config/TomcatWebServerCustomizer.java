package com.st.newtest.config;


import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * 请求中带有json串，出现异常解决方案
 *
 * @author hcmony
 * @since V1.0.0, 2019/03/22 16:22
 */
@Configuration
public class TomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    /**
     * 解决请求url中参数 带有特殊字符无法提交导致java.lang.IllegalArgumentException的问题
     *
     * @param factory the web server factory to customize
     */
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "{}[]|"));
    }
}
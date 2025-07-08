package org.baseball.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.baseball.interceptor.AdminCheckInterceptor;
import org.baseball.interceptor.LoginCheckInterceptor;
import org.baseball.interceptor.MypageCheckInterceptor;
import org.baseball.interceptor.NeedsLogin;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:db.properties", "classpath:iamport.properties", "classpath:application.properties"})
@EnableWebMvc
@EnableScheduling
@ComponentScan("org.baseball")
@MapperScan(basePackages = "org.baseball", annotationClass = Mapper.class)
@EnableTransactionManagement //트랜잭션 활성화
public class MvcConfig implements WebMvcConfigurer {
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    // ViewResolver 설정(JSP 경로)
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    // 정적페이지 처리(컨트롤러가 아니라 톰캣에서 처리하기 위해)
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // HikariCP
    // HikariCP
    @Bean
    public DataSource datasource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    //MyBatis
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean ssf = new SqlSessionFactoryBean();
        ssf.setDataSource(datasource());//의존성 주입

        // mapper 경로설정
        org.springframework.core.io.Resource[] res =
                new org.springframework.core.io.support.PathMatchingResourcePatternResolver()
                        .getResources("classpath:mappers/**/*.xml");
        ssf.setMapperLocations(res);
        
        return ssf.getObject();
    }
    //DAO에 도입될 객체
    @Bean //sqlsessiontemplate 이름 암기하자!
    public SqlSessionTemplate sst() throws Exception{
        return new SqlSessionTemplate(sqlSessionFactory()); //의존성 주입(생성자 방식)
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("/assets/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    //트랜잭션 매니저 빈 등록
    @Bean
    public TransactionManager transactionManager() {
        TransactionManager tm = new DataSourceTransactionManager(datasource());
        return tm;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminCheckInterceptor())
                .addPathPatterns("/admin/**");

        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/reservation/**")
                .excludePathPatterns(
                        "/reservation/errors/needLogin"  // 무한 리다이렉트 방지
                );

        registry.addInterceptor(new MypageCheckInterceptor())
                .addPathPatterns("/user/mypage/**");

        registry.addInterceptor(new NeedsLogin())
                .addPathPatterns("/community/post/write");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

package com.cinder.filefragment.config;

import com.cinder.filefragment.services.FileFragmentService;
import com.cinder.filefragment.services.Impl.DefaultFileFragmentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cinder
 */
@Configuration
//@ConditionalOnClass(KiteService.class) ：
//只有在 classpath 中找到 KiteService 类的情况下，才会解析此自动配置类，否则不解析。
@ConditionalOnClass(FileFragmentService.class)
//@EnableConfigurationProperties(KiteProperties.class)：
//启用配置类。
//@EnableConfigurationProperties(FilePathProperties.class)
@Slf4j
public class FileFragmentConfig {


  @Bean
  //@ConditionalOnMissingBean(KiteService.class)：
  //与 @Bean 配合使用，只有在当前上下文中不存在某个 bean 的情况下才会执行所注解的代码块，也就是当前上下文还没有 KiteService 的 bean 实例的情况下，才会执行 kiteService() 方法，从而实例化一个 bean 实例出来。
  @ConditionalOnMissingBean(FileFragmentService.class)
  //@ConditionalOnProperty：
  //当应用配置文件中有相关的配置才会执行其所注解的代码块。
  @ConditionalOnProperty(prefix = "file.fragment",value = "enabled", havingValue = "true")
  FileFragmentService kiteService(){
    return new DefaultFileFragmentServiceImpl();
  }

}

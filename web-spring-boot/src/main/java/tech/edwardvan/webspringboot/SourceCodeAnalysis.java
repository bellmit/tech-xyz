package tech.edwardvan.webspringboot;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.*;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.*;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * 源码解析集合类
 *
 * @author EdwardVan
 */
public interface SourceCodeAnalysis {

    /**
     * SpringBoot启动过程
     * <p>
     * 创建SpringApplication:{@link SpringApplication#SpringApplication(ResourceLoader, Class[])}
     * 加载RunListener:{@link SpringApplication#getRunListeners(String[])}
     * RunListeners.starting 执行:{@link SpringApplicationRunListeners#starting()}
     * 准备环境入口:{@link SpringApplication#prepareEnvironment(org.springframework.boot.SpringApplicationRunListeners, ApplicationArguments)}
     * 创建环境:{@link SpringApplication#getOrCreateEnvironment()}
     * 配置环境:{@link SpringApplication#configureEnvironment(ConfigurableEnvironment, String[])}
     * RunListeners.environmentPrepared 执行:{@link SpringApplicationRunListeners#environmentPrepared(ConfigurableEnvironment)}
     * 输出Banner:{@link SpringApplication#printBanner(ConfigurableEnvironment)}
     * 创建应用上下文:{@link SpringApplication#createApplicationContext()}
     * 准备应用上下文入口:{@link SpringApplication#prepareContext(ConfigurableApplicationContext, ConfigurableEnvironment, org.springframework.boot.SpringApplicationRunListeners, ApplicationArguments, Banner)}
     * 执行系统初始化器:{@link SpringApplication#applyInitializers(ConfigurableApplicationContext)}
     * RunListeners.contextPrepared 执行:{@link SpringApplicationRunListeners#contextPrepared(ConfigurableApplicationContext)}
     * 启动类注册到BeanDefinitionMap:{@link SpringApplication#load(ApplicationContext, Object[])}
     * RunListeners.contextLoaded 执行:{@link SpringApplicationRunListeners#contextLoaded(ConfigurableApplicationContext)}
     * 刷新应用上下文入口:{@link AbstractApplicationContext#refresh()}
     * 获取BeanFactory:{@link AbstractApplicationContext#obtainFreshBeanFactory()}
     * 准备BeanFactory:{@link AbstractApplicationContext#prepareBeanFactory(ConfigurableListableBeanFactory)}
     * 执行BeanFactoryPostProcessor:{@link AbstractApplicationContext#invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory)}
     * 注册BeanPostProcessor:{@link AbstractApplicationContext#registerBeanPostProcessors(ConfigurableListableBeanFactory)}
     * 初始化MessageSource:{@link AbstractApplicationContext#initMessageSource()}
     * 初始化事件广播器:{@link AbstractApplicationContext#initApplicationEventMulticaster()}
     * 创建web容器:{@link ServletWebServerApplicationContext#createWebServer()}
     * 注册监听器:{@link AbstractApplicationContext#registerListeners()}
     * 创建所有非懒加载的单例类:{@link AbstractApplicationContext#finishBeanFactoryInitialization(ConfigurableListableBeanFactory)}
     * 刷新完成:{@link AbstractApplicationContext#finishRefresh()}
     * RunListeners.started 执行:{@link SpringApplicationRunListeners#started(ConfigurableApplicationContext)}
     * 执行启动加载器:{@link SpringApplication#callRunners(ApplicationContext, ApplicationArguments)}
     * RunListeners.running 执行:{@link SpringApplicationRunListeners#running(ConfigurableApplicationContext)}
     */
    void SpringBoot启动过程();

    /**
     * Bean实例化过程
     * <p>
     * 实例化剩余所有对象:{@link DefaultListableBeanFactory#preInstantiateSingletons()}
     * 获取实例对象:{@link AbstractBeanFactory#doGetBean(String, Class, Object[], boolean)}
     * 实例化对象入口:{@link AbstractAutowireCapableBeanFactory#doCreateBean(String, RootBeanDefinition, Object[])}
     * 实例化对象:{@link AbstractAutowireCapableBeanFactory#createBeanInstance(String, RootBeanDefinition, Object[])}
     * Autowired的依赖注入:{@link AbstractAutowireCapableBeanFactory#populateBean(String, RootBeanDefinition, BeanWrapper)}
     * 检查Aware相关方法:{@link AbstractAutowireCapableBeanFactory#invokeAwareMethods(String, Object)}
     * BeanPostProcessor前置处理:{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization(Object, String)}
     * 自定义初始化:{@link AbstractAutowireCapableBeanFactory#invokeInitMethods(String, Object, RootBeanDefinition)}
     * BeanPostProcessor后置处理:{@link AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization(Object, String)}
     */
    void Bean实例化过程();

    /**
     * 配置优先级
     * <p>
     * 环境变量准备原理:{@link SpringApplication#prepareEnvironment(SpringApplicationRunListeners, ApplicationArguments)}
     * 加载配置文件原理:{@link ConfigFileApplicationListener.Loader#load()}
     * <p>
     * 按照顺序由高到低,数字越小优先级越高
     * 1.在命令行中传入的参数.类似于java -jar -Dspring.profiles.active等.
     * 2.SPRING_APPLICATION_JSON属性,该属性以JSON形式存储在系统环境变量中.
     * 3.java:comp/env中JNDI属性.
     * 4.Java的系统的属性,可通过System.getProperties()获得相关内容.
     * 5.操作系统中的环境变量.
     * 6.通过random.*配置的随机属性.
     * 7.位于当前应用jar包外,针对不同{profile}环境的配置文件内容.
     * 8.位于当前应用jar包内,针对不同{profile}环境的配置文件内容.
     * 9.位于当前应用jar包外的application.properties或application.yml配置内容.
     * 10.位于当前应用jar包内的application.properties或application.yml配置内容.
     * 11.在@Configuration注解修改的类中,通过@PropertySource注解定义的属性.
     * 12.应用默认属性,使用SpringApplication.setDefaultProperties定义的属性内容.
     */
    void 配置优先级();

    /**
     * SpringMVC处理请求步骤
     * <p>
     * 处理器映射器执行:{@link DispatcherServlet#getHandler(HttpServletRequest)}
     * 获取处理器适配器:{@link DispatcherServlet#getHandlerAdapter(Object)}
     * 执行适配入口:{@link RequestMappingHandlerAdapter#handleInternal(HttpServletRequest, HttpServletResponse, HandlerMethod)}
     * 解析方法参数入口:{@link InvocableHandlerMethod#getMethodArgumentValues(NativeWebRequest, ModelAndViewContainer, Object...)}
     * 寻找参数解析器:{@link HandlerMethodArgumentResolverComposite#getArgumentResolver(MethodParameter)}
     * 执行参数解析器:{@link HandlerMethodArgumentResolver#resolveArgument(MethodParameter, ModelAndViewContainer, NativeWebRequest, WebDataBinderFactory)}
     * 执行方法且返回结果:{@link InvocableHandlerMethod#doInvoke(Object...)}
     * 解析方法返回值入口:{@link HandlerMethodReturnValueHandlerComposite#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)}
     * 寻找返回值处理器:{@link HandlerMethodReturnValueHandlerComposite#selectHandler(Object, MethodParameter)}
     * 执行返回值处理器:{@link HandlerMethodReturnValueHandler#handleReturnValue(Object, MethodParameter, ModelAndViewContainer, NativeWebRequest)}
     * 视图内容协商器解析得到View:{@link ContentNegotiatingViewResolver#resolveViewName(String, Locale)}
     * 输出结果:{@link View#render(Map, HttpServletRequest, HttpServletResponse)}
     */
    void SpringMVC处理请求步骤();
}

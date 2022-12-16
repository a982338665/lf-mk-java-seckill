package pers.lish.girl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * create by lishengbo on 2017-12-20 10:39
 */
@Aspect
@Component
public class HttpAspect {
    /**
     * 方法执行之前执行
     */
/*//    @Before("execution(public * pers.lish.girl.controller.GirlController.girlList(..))")
    @Before("execution(public * pers.lish.girl.controller.GirlController.*(..))")
    public void log(){
        System.err.print("调用开始-----------------");
    }
    @After("execution(public * pers.lish.girl.controller.GirlController.*(..))")
    public void after(){
        System.err.print("调用结束-----------------");
    }*/
    /********以上代码等同于以下******************************************************/
    /**
     * 日志的打印
     */
    private final static Logger log = LoggerFactory.getLogger(HttpAspect.class);

    //方法修饰符+任意返回值（*）+包名及子包+类+任意方法（*）+（..）（任意参数:无参则为(),第一个参数为Long的(Long)）+只拦截抛出异常的方法
//    @Pointcut("execution(public * com.imooc.service..*Service.*(..)) throws Exception")
//    @Pointcut("execution(public String com.imooc.service..*Service.*(..))")
//    @Pointcut("execution(public void com.imooc.service..*Service.*(..))")
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void log() {
    }

    //    @Pointcut("execution(public * pers.lish.girl.controller.GirlController.*(..))")
//    public void log(){
//    }
    @Before("log()")
    public void before(JoinPoint joinPoint) {
        log.info("log调用开始-----------------");
        //url获取
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        log.info("url={}", request.getRequestURI());
        log.info("method={}", request.getMethod());
        log.info("ip={}", request.getRemoteAddr());
        log.info("remote={}-{}-{}", request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort());
        log.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("args={}", joinPoint.getArgs());

    }

//    @After("log()")
//    public void after() {
//        log.info("log调用结束-----------------");
////        System.err.print("调用结束-----------------");
//    }


    @Around("log()")
    public Object printTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = proceedingJoinPoint.proceed();
        stopWatch.stop();
        log.info("接口耗时--------------秒：{}/换算毫秒：{}", stopWatch.getTotalTimeSeconds(), stopWatch.getTotalTimeMillis());
        return proceed;
    }

    /**
     * 接口响应内容获取
     *
     * @param o
     */
    @AfterReturning(returning = "o", pointcut = "log()")
    public void returning(Object o) {
        log.info("response={}", o.toString());
//        log.info("response={}", JSON.toJSONString(o));
    }

/**
 * 万能注解
 * 以注解形式定义与方法级别上
 * 所有带有注解@AdmainOnly的方法都要经过此拦截器---此注解写全类名
 */
//    @Around("log()")
//    public Object  around(ProceedingJoinPoint proceedingJoinPoint){
//        log.error("before={}",proceedingJoinPoint.getSignature().getName());
//        Object result=null;
//        try {
//            result=proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
//            log.error("afterReturning={}",result);
//        } catch (Throwable throwable) {
//            log.error("afterException={}",throwable);
//            throwable.printStackTrace();
//        }finally {
//            log.error("afterresponse={}","-----------");
//        }
//        return  result;
//    }
/*********************以注解形式定义与方法级别上**************************************************************/
/**
 * 所有带有注解@AdmainOnly的方法都要经过此拦截器---此注解写全类名
 */
    /*@Pointcut("@annotation(pers.lish.girl.security.AdmainOnly)")
    public void admainOnly(){

    }
    @Before("admainOnly()")
    public void after2(){
        log.error("注解形式拦截器定义-----------------");
    }*/
}

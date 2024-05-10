package pers.lish.girl.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * create by lishengbo on 2017-12-20 10:39
 */
@Aspect
@Component
public class HttpAspect2 {

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 日志的打印
	 */
	private final static Logger log = LoggerFactory.getLogger(HttpAspect.class);

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void log() {
	}

	@Before("log()")
	public void before(JoinPoint joinPoint) {
		log.info("log调用开始-------------------------");
		//url获取
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		log.info("url={}", request.getRequestURI());
		log.info("method={}", request.getMethod());
		log.info("ip={}", request.getRemoteAddr());
		log.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		try {
			// 获取GET请求的query参数
			Map<String, String[]> parameterMap = request.getParameterMap();
			// 打印或处理query参数
			StringBuilder sv = new StringBuilder();
			parameterMap.forEach((key, value) -> {
				if (value.length > 0) {
					for (String s : value) {
						sv.append(key).append("=").append(s).append("&");
					}
				} else {
					sv.append(key).append("=&");
				}
			});
			String svc = sv.substring(0, sv.length() - 1);
			log.info("args-query={}", svc);
		} catch (Exception e) {

		}
		Object[] args = joinPoint.getArgs();
		// 将参数转换为JSON字符串
		try {
			String jsonArgs = objectMapper.writeValueAsString(args);
			int i = jsonArgs.indexOf("[");
			int i1 = jsonArgs.lastIndexOf("]");
			jsonArgs = jsonArgs.substring(i + 1, i1);
			log.info("args-body：：：{}", jsonArgs);
		} catch (JsonProcessingException e) {
			log.info("args-body：：：{}", args);
		}

	}


	/**
	 * 万能注解
	 */
	@Around("log()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) {
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			try {
				String jsonString = JSON.toJSONString(result, SerializerFeature.WriteMapNullValue);
				log.error("afterReturning={}", jsonString);
			} catch (Exception e) {
				log.error("afterReturning={}", result);
			}
		} catch (Throwable throwable) {
			log.error("afterException={}", throwable.getMessage());
			throw new RuntimeException(throwable.getMessage());
		} finally {
			log.info("log调用结束----------------------------------------");
		}
		return result;
	}

}

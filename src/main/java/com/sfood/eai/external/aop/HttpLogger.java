package com.sfood.eai.external.aop;

import com.sfood.eai.external.aop.annotation.HttpLog;
import com.sfood.eai.external.config.MallConfiguration;
import com.sfood.eai.external.dto.ApiCallLogReq;
import com.sfood.eai.external.dto.ApiCallLogRes;
import com.sfood.eai.external.service.MarketMallService;
import com.sfood.eai.external.type.InterfaceType;
import com.sfood.eai.external.util.CustomMapUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class HttpLogger {
    private final MarketMallService mallService;
    private ThreadLocal<String> logId = new ThreadLocal<>();

    @Before("@annotation(com.sfood.eai.external.aop.annotation.HttpLog)")
    public void saveRequestLog(JoinPoint joinPoint) throws Throwable {
        logId.remove();
        logId.set(UUID.randomUUID().toString());

        Map<String, Object> params = CustomMapUtils.extractFields(getRequestObject(joinPoint));

        ApiCallLogReq apiCallLogReq = ApiCallLogReq.builder()
                .id(logId.get())
                .type(getInterfaceType(joinPoint))
                .data(params)
                .clientIp(getClientIp())
                .build();

        mallService.saveRequestLog(apiCallLogReq);
    }

    @AfterReturning(value = "@annotation(com.sfood.eai.external.aop.annotation.HttpLog)", returning = "response")
    public void saveResponseLog(JoinPoint joinPoint, ResponseEntity response) throws Throwable {
        ApiCallLogRes apiCallLogRes = ApiCallLogRes.builder()
                .logId(logId.get())
                .status(response.getStatusCode().value())
                .message(response.getStatusCode().toString())
                .data(response.getBody())
                .build();

        mallService.saveResponseLog(apiCallLogRes);
    }

    private InterfaceType getInterfaceType(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        HttpLog httpLog = method.getAnnotation(HttpLog.class);
        return httpLog.value();
    }

    private Object getRequestObject(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (ObjectUtils.isNotEmpty(arg)  && arg.getClass().equals(MallConfiguration.class)) {
                return ((MallConfiguration)arg).getCondition();
            }
        }
        return null;
    }

    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "UNKNOWN";
        }

        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("X-Forwarded-For");

        if (StringUtils.hasText(ip) || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.hasText(ip) || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.hasText(ip) || "UNKNOWN".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}

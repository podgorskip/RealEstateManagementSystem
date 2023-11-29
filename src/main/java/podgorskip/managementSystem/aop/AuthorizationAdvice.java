package podgorskip.managementSystem.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import podgorskip.managementSystem.annotations.RequiredPrivilege;
import podgorskip.managementSystem.exceptions.UnauthorizedException;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.security.DatabaseUserDetailsService;
import podgorskip.managementSystem.utils.Privileges;
import java.util.Objects;

@Component
@Aspect
public class AuthorizationAdvice {

    @Autowired
    private DatabaseUserDetailsService databaseUserDetailsService;

    private static final Logger log = LogManager.getLogger(AuthorizationAdvice.class);


    @Before("execution(public * podgorskip.managementSystem.controllers.*.*(..))")
    public void authorize(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof CustomUserDetails userDetails) {

                if (isUserUnauthorized(userDetails, extractPrivilege(joinPoint))) {
                    throw new UnauthorizedException("User lacked privilege to perform the action");
                }

            }
        }
    }

    private Boolean isUserUnauthorized(CustomUserDetails userDetails, Privileges requiredAuthority) {

        if (Objects.isNull(userDetails) || userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredAuthority.name()))) {
            log.warn("Authenticated user lacked privilege {} to perform the request", requiredAuthority);
            return true;
        }

        return false;
    }

    private Privileges extractPrivilege(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RequiredPrivilege privilege = methodSignature.getMethod().getAnnotation(RequiredPrivilege.class);

        if (privilege != null) {
            return privilege.value();

        } else {
            throw new RuntimeException("No required privilege provided");
        }
    }
}

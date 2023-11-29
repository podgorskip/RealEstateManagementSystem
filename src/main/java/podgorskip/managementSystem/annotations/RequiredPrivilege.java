package podgorskip.managementSystem.annotations;

import podgorskip.managementSystem.utils.Privileges;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface RequiredPrivilege {
    Privileges value();
}
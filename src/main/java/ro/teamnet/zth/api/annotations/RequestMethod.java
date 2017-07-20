package ro.teamnet.zth.api.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Ramona.Arsene on 7/20/2017.
 */

@Target({ElementType.METHOD})
@Retention(RUNTIME)
@Inherited
@Documented
public @interface RequestMethod {
    String urlPath() default "";
    String methodType() default "";

}

package ro.teamnet.zth.api.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Ramona.Arsene on 7/20/2017.
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Documented
public @interface Controller {
    String urlPath() default"";

}

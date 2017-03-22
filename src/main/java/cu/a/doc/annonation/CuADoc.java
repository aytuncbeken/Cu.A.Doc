package cu.a.doc.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Aytunc BEKEN on 10.3.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CuADoc {
    String purpose() default "";
    String[] params() default "";
    String[] escapes() default "";
}

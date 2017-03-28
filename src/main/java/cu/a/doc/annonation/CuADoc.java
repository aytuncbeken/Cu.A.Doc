package cu.a.doc.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Aytunc BEKEN on 10.3.2017.
 * Annonation Class For Cu.A.Doc
 * This annonation is used for generating Cu.A.Doc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CuADoc {
    /**
     * Purpose of the gherkin
     * @return empty string if not annotated
     */
    String purpose() default "";

    /**
     * Explanations for all parameters which is used by gherkin
     * @return empty string if not annotated
     */
    String[] params() default "";

    /**
     * Escape string values for replacing with space value.
     * This is used for making gherkin more readable
     * @return empty string if not annotated
     */
    String[] escapes() default "";
}

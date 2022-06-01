package com.xfoss.Annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation
{
    String Developer() default "Lenny";
    String Expirydate();
}

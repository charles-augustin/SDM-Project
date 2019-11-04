package com.sdmproject.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseField {

    String columnName() default "";
    boolean unique() default false;
    boolean primary() default false;
    boolean id() default false;
    boolean ai() default false;
    int length() default -1;
    boolean otherClassReference() default false;
    
    String dateFormat() default "";

}

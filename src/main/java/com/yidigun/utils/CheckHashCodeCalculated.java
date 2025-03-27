package com.yidigun.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("all")
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface CheckHashCodeCalculated {
}

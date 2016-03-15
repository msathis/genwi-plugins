package com.genwi.plugin.plugins.annotation;

import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: sathis
 * Last Updated: 08/03/16.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public  @interface  Subscribe {
    @NonNull String action();
}

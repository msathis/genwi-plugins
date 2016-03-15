package com.genwi.plugin.plugins.annotation;

import android.support.annotation.NonNull;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: sathis
 * Last Updated: 09/03/16.
 */

@IndexAnnotated
@Retention(RetentionPolicy.RUNTIME)
public @interface Plugin {
    @NonNull String type();
}

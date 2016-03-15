package com.genwi.plugin.plugins.annotation;

import android.support.annotation.NonNull;

import org.atteo.classindex.IndexAnnotated;

/**
 * Author: sathis
 * Last Updated: 09/03/16.
 */

@IndexAnnotated
public @interface Plugin {
    @NonNull String type();
}

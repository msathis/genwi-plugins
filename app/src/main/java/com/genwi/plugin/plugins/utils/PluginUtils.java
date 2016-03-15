package com.genwi.plugin.plugins.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.genwi.plugin.plugins.model.ActionModel;

/**
 * Author: sathis
 * Last Updated: 10/03/16.
 */
public class PluginUtils {

    public static final int CUSTOM_ACTION_REQUEST_CODE = 5604;

    public static final String DATA = "data";

    public static ActionModel curModel;

    public static void startActivity(Context context, ActionModel model, Intent intent) {
        curModel = model;
        ((Activity) context).startActivityForResult(intent, CUSTOM_ACTION_REQUEST_CODE);
    }

    public static ActionModel getCurModel() {
        return curModel;
    }
}

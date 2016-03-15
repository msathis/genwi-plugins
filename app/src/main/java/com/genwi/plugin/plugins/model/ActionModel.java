package com.genwi.plugin.plugins.model;

import android.webkit.WebView;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Author: sathis
 * Last Updated: 08/03/16.
 */
public class ActionModel {

    public String type;
    public String action;
    public String callback;
    public String data;
    public boolean success = true;

    /** You can set to true if you want to open custom activity **/
    public boolean resultPending = false;

    public int handledCount = 0;
    public JSONObject result;

    public static ActionModel from(Map<String, String> params) {
        ActionModel model = new ActionModel();
        model.action = params.get("action");
        model.callback = params.get("callback");
        model.type = params.get("type");
        model.data = params.get("data");
        return model;
    }
}

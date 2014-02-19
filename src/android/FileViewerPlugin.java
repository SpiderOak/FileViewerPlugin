package com.spideroak.fileviewerplugin;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;

import java.io.File;

import org.apache.cordova.DroidGap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.text.Html;
import android.webkit.MimeTypeMap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

/**
 * This plugin is basically a minimal version of Boris Smus's WebIntents plugin,
 * details below:
 *
 * WebIntent is a PhoneGap plugin that bridges Android intents and web
 * applications:
 * 
 * 1. web apps can spawn intents that call native Android applications.
 * 
 * @author boris@borismus.com
 * 
 */
public class FileViewerPlugin extends CordovaPlugin {

  private static final String LOG_TAG = "FileViewerPlugin";

  private String onNewIntentCallback = null;

  /**
   * Executes the request and returns PluginResult.
   * 
   * @param action
   *            The action to execute.
   * @param args
   *            JSONArray of arguments for the plugin.
   * @param callbackId
   *            The callback id used when calling back into JavaScript.
   * @return A PluginResult object with a status and message.
   */
  public boolean
      execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    try {
      if (action.equals("view")) {
        if (args.length() != 1) {
          callbackContext.error("Expected one non-empty string argument.");
          return false;
        }

        // Parse the arguments
        JSONObject obj = args.getJSONObject(0);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Uri uri = obj.has("url") ? Uri.parse(obj.getString("url")) : null;
        // File file = new File(uri.getEncodedPath());
        // Log.d(LOG_TAG, Uri.encode(uri.toString()));
        // String ext=mime.getFileExtensionFromUrl(uri.toString());
        String ext = "";
        int x = uri.toString().lastIndexOf('.');
        if (x > 0) {
            ext = uri.toString().substring(x+1);
        }
        // Log.d(LOG_TAG, ext);
        String type = obj.has("type") ? obj.getString("type") : "";
        if (type == "") {
            Log.e(LOG_TAG, "trying getContentResolver...");
            Context app = this.cordova.getActivity().getApplicationContext();
            type = app.getContentResolver().getType(uri);
            Log.e(LOG_TAG, "after getContentResolver, type == " + type);
        }

        JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
        Map<String, String> extrasMap = new HashMap<String, String>();

        // Populate the extras if any exist
        if (extras != null) {
          JSONArray extraNames = extras.names();
          for (int i = 0; i < extraNames.length(); i++) {
            String key = extraNames.getString(i);
            String value = extras.getString(key);
            extrasMap.put(key, value);
          }
        }

        Log.e(LOG_TAG, "type=" + type + ", uri=" + uri);

        view(obj.getString("action"), uri, type, extrasMap, callbackContext);
        callbackContext.success();
        return true;
      }
      if (action.equals("share")) {
        if (args.length() != 1) {
          callbackContext.error("Expected one non-empty string argument.");
          return false;
        }
        // Parse the arguments
        JSONObject obj = args.getJSONObject(0);
        String type = obj.has("type") ? obj.getString("type") : "*/*";
        JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
        Map<String, String> extrasMap = new HashMap<String, String>();

        // Populate the extras if any exist
        if (extras != null) {
          JSONArray extraNames = extras.names();
          for (int i = 0; i < extraNames.length(); i++) {
            String key = extraNames.getString(i);
            String value = extras.getString(key);
            extrasMap.put(key, value);
          }
        }
        share(obj.getString("action"), type, extrasMap, callbackContext);
        return true;
      }
      callbackContext.success();
      return true;
    } catch (JSONException e) {
      e.printStackTrace();
      callbackContext.error("Error.");
      return false;
    }
  }

  void view(String action, Uri uri, String type, 
                                      Map<String, String> extras, 
                                      CallbackContext callbackContext) {
    try {
      Intent i = (uri != null ? new Intent(action, uri) : new Intent(action));
      
      if (type != null && uri != null) {
        i.setDataAndType(uri, type); //Fix the crash problem with android 2.3.6
      } else {
        if (type != null) {
          i.setType(type);
        }
      }
      
      for (String key : extras.keySet()) {
        String value = extras.get(key);
        // If type is text html, the extra text must sent as HTML
        if (key.equals(Intent.EXTRA_TEXT)) {
          if (type.equals("text/html")) {
            i.putExtra(key, Html.fromHtml(value));
          } else {
            i.putExtra(key, value);
          }
        } else if (key.equals(Intent.EXTRA_STREAM)) {
          // allowes sharing of images as attachments.
          // value in this case should be a URI of a file
          i.putExtra(key, Uri.parse(value));
        } else if (key.equals(Intent.EXTRA_EMAIL)) {
          // allows to add the email address of the receiver
          i.putExtra(Intent.EXTRA_EMAIL, new String[] { value });
        } else {
          i.putExtra(key, value);
        }
      }
      ((DroidGap)this.cordova.getActivity()).startActivity(i);
    } catch (Exception ex) {
      ex.printStackTrace();
      callbackContext.error("Error. No Activity found to handle Intent.");
    }
  }

  void share(String action, String type, Map<String, String> extras, 
                                          CallbackContext callbackContext) {
    try {
      Intent i = new Intent(action);
      MimeTypeMap mime = MimeTypeMap.getSingleton();
      
      i.setType(type);
      
      for (String key : extras.keySet()) {
        String value = extras.get(key);
        // If type is text html, the extra text must sent as HTML
        if (key.equals(Intent.EXTRA_TEXT)) {
          if (type.equals("text/html")) {
            i.putExtra(key, Html.fromHtml(value));
          } else {
            i.putExtra(key, value);
          }
        } else if (key.equals(Intent.EXTRA_STREAM)) {
          // allowes sharing of images as attachments.
          // value in this case should be a URI of a file
          Uri uri = Uri.parse(value);
          i.putExtra(key, uri);

          String ext = "";
          int x = uri.toString().lastIndexOf('.');
          if (x > 0) {
              ext = uri.toString().substring(x+1);
          }
          // Log.d(LOG_TAG, ext);
          String calculatedType = mime.getMimeTypeFromExtension(ext);
          i.setType(calculatedType);
        } else if (key.equals(Intent.EXTRA_EMAIL)) {
          // allows to add the email address of the receiver
          i.putExtra(Intent.EXTRA_EMAIL, new String[] { value });
        } else {
          i.putExtra(key, value);
        }
      }
      ((DroidGap)this.cordova.getActivity()).startActivity(i);
    } catch (Exception ex) {
      ex.printStackTrace();
      callbackContext.error("Error. No Activity found to handle Intent.");
    }
  }
}

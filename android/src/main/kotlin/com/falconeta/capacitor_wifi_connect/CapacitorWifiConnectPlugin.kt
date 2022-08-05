package com.falconeta.capacitor_wifi_connect

import android.Manifest
import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback


private const val PERMISSION_ACCESS_FINE_LOCATION = "access-fine-location";
private const val PERMISSION_ACCESS_COARSE_LOCATION = "access-coarse-location";
private const val PERMISSION_ACCESS_WIFI_STATE = "access-wifi-state";
private const val PERMISSION_CHANGE_WIFI_STATE = "change-wifi-state";
private const val PERMISSION_CHANGE_NETWORK_STATE = "change-network-state";


@CapacitorPlugin(
  name = "CapacitorWifiConnect",
  permissions = [
    Permission(
      alias = PERMISSION_ACCESS_FINE_LOCATION,
      strings = [Manifest.permission.ACCESS_FINE_LOCATION]
    ),
    Permission(
      alias = PERMISSION_ACCESS_COARSE_LOCATION,
      strings = [Manifest.permission.ACCESS_COARSE_LOCATION]
    ),
    Permission(
      alias = PERMISSION_ACCESS_WIFI_STATE,
      strings = [Manifest.permission.ACCESS_WIFI_STATE]
    ),
    Permission(
      alias = PERMISSION_CHANGE_WIFI_STATE,
      strings = [Manifest.permission.CHANGE_WIFI_STATE]
    ),
    Permission(
      alias = PERMISSION_CHANGE_NETWORK_STATE,
      strings = [Manifest.permission.CHANGE_NETWORK_STATE]
    )
  ]
)
class CapacitorWifiConnectPlugin : Plugin() {
  private lateinit var implementation: CapacitorWifiConnect;

  override fun load() {
    super.load();
    implementation = CapacitorWifiConnect(context);
  }

  @PluginMethod
  fun disconnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "disconnectCallback");
      return;
    }
    val ret = JSObject()
    implementation.disconnect(call)
  }

  @PermissionCallback
  private fun disconnectCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      disconnect(call);
    } else {
      call.reject("Permission is required")
    }
  }

  @PluginMethod
  fun getSSID(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "getSSIDCallback");
      return;
    }
    val ret = JSObject()
    ret.put("value", implementation.getSSID())
    call.resolve(ret)
  }

  @PermissionCallback
  private fun getSSIDCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      getSSID(call);
    } else {
      call.reject("Permission is required")
    }
  }

  @PluginMethod
  fun connect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "connectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")

    if (ssid != null) {
      implementation.connect(ssid, call);
    } else {
      call.reject("SSID is mandatory")
    }
  }

  @PermissionCallback
  private fun connectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      connect(call);
    } else {
      call.reject("Permission is required")
    }
  }

  @PluginMethod
  fun prefixConnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "prefixConnectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")
    if (ssid != null) {
      implementation.prefixConnect(ssid!!, call);
    } else {
      call.reject("SSID is mandatory")
    }
  }

  @PermissionCallback
  private fun prefixConnectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      prefixConnect(call);
    } else {
      call.reject("Permission is required")
    }
  }

  @PluginMethod
  fun secureConnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "secureConnectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")
    val password = call.getString("password")
    val isWep = call.getBoolean("isWep") ?: false;
    if (ssid != null && password != null) {
      implementation.secureConnect(ssid!!, password!!, isWep, call);
    } else {
      call.reject("SSID and password are mandatory")
    }
  }

  @PermissionCallback
  private fun secureConnectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      secureConnect(call);
    } else {
      call.reject("Permission is required")
    }
  }

  fun securePrefixConnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "securePrefixConnectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")
    val password = call.getString("password")
    val isWep = call.getBoolean("isWep") ?: false;
    if (ssid != null && password != null) {
      implementation.secureConnect(ssid!!, password!!, isWep, call);
    } else {
      call.reject("SSID and password are mandatory")
    }
  }

  @PermissionCallback
  private fun securePrefixConnectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      securePrefixConnect(call);
    } else {
      call.reject("Permission is required")
    }
  }


  private fun checkPermission(call: PluginCall, callbackName: String) {
    if (getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_FINE_LOCATION,
        call,
        callbackName
      );
    }

    if (getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_COARSE_LOCATION,
        call,
        callbackName
      );
    }

    if (getPermissionState(PERMISSION_ACCESS_WIFI_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_WIFI_STATE,
        call,
        callbackName
      );
    }

    if (getPermissionState(PERMISSION_CHANGE_WIFI_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_CHANGE_WIFI_STATE,
        call,
        callbackName
      );
    }

    if (getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_CHANGE_NETWORK_STATE,
        call,
        callbackName
      );
    }

  }

  private fun isPermissionGranted(): Boolean {
    return getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) == PermissionState.GRANTED;
  }

}
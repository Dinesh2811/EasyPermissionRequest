package com.dinesh.permission

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

private const val TAG = "log_PermissionRequest"

class PermissionRequest internal constructor(private val activity: ComponentActivity) {
    internal var requestMultiplePermissionsLauncher: ActivityResultLauncher<Array<String>>? = null
    internal var permissionsToRequest: Array<String> = arrayOf()
    internal var isRepeatRequestPermissionAllowed = true
    internal var isLoggingEnabled = true
    internal var permissionCallbackExperimental: PermissionCallbackExperimental? = null
    internal var permissionCallback: PermissionCallback? = null
    private var hasPreviouslyGrantedOrDenied = false
    private var hasPreviouslyPermanentlyDenied = false
    private val sharedPreferences = activity.getSharedPreferences("${activity.packageName}_permission_sharedPreferences", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    init {
        requestMultiplePermissionsLauncher?.unregister()
        if (requestMultiplePermissionsLauncher == null) {
            requestMultiplePermissionsLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionResult ->
                handlePermissionResult(permissionResult)
            }
        }
    }

    private fun handlePermissionResult(permissionResult: Map<String, Boolean>) {
        val permissionStates = mutableListOf<PermissionState>()

        permissionResult.forEach { (permission, isPermissionGranted) ->
            hasPreviouslyGrantedOrDenied = sharedPreferences.getBoolean("${permission}hasPreviouslyGrantedOrDenied", false)
            hasPreviouslyPermanentlyDenied = sharedPreferences.getBoolean("${permission}hasPreviouslyPermanentlyDenied", false)

            val state = when {
                isPermissionGranted -> {
                    editor.putBoolean("${permission}hasPreviouslyGrantedOrDenied", true).apply()
                    hasPreviouslyGrantedOrDenied = sharedPreferences.getBoolean("${permission}hasPreviouslyGrantedOrDenied", true)
                    if (isLoggingEnabled) {
                        Log.d(TAG, "Permission granted: $permission")
                    }
                    PermissionState.Granted(permission)
                }

                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                    editor.putBoolean("${permission}hasPreviouslyGrantedOrDenied", true).apply()
                    hasPreviouslyGrantedOrDenied = sharedPreferences.getBoolean("${permission}hasPreviouslyGrantedOrDenied", true)
                    if (isLoggingEnabled) {
                        Log.w(TAG, "Permission denied: $permission")
                    }
                    PermissionState.Denied(permission)
                }

                else -> {
                    if (hasPreviouslyGrantedOrDenied) {
                        editor.putBoolean("${permission}hasPreviouslyPermanentlyDenied", true).apply()
                        if (isLoggingEnabled) {
                            Log.e(TAG, "Permission permanently denied: $permission")
                        }
                        PermissionState.PermanentlyDenied(permission)
                    } else {
                        editor.putBoolean("${permission}hasPreviouslyPermanentlyDenied", false).apply()
                        if (isLoggingEnabled) {
                            Log.i(TAG, "Permission cancelled: $permission")
                        }
                        PermissionState.Cancelled(permission)
                    }
                }
            }
            permissionStates.add(state)
        }

        onPermissionStatesResult(permissionStates, isRepeatRequestPermissionAllowed)
    }

    private fun onPermissionStatesResult(permissionStates: List<PermissionState>, isRepeatRequestPermissionAllowed: Boolean) {
        val permissionGrantedList = mutableListOf<String>()
        val permissionDeniedList = mutableListOf<String>()
        val permissionPermanentlyDeniedList = mutableListOf<String>()
        val permissionCancelledList = mutableListOf<String>()

        permissionStates.forEach { permissionState ->
            when (permissionState) {
                is PermissionState.Granted -> permissionGrantedList.add(permissionState.permission)
                is PermissionState.Denied -> permissionDeniedList.add(permissionState.permission)
                is PermissionState.Cancelled -> permissionCancelledList.add(permissionState.permission)
                is PermissionState.PermanentlyDenied -> {
                    permissionDeniedList.remove(permissionState.permission)
                    permissionPermanentlyDeniedList.add(permissionState.permission)
                }
            }
        }

        permissionCallback?.onPermissionCallback(granted = permissionGrantedList, denied = permissionDeniedList + permissionPermanentlyDeniedList + permissionCancelledList)
        permissionCallbackExperimental?.onPermissionGranted(granted = permissionGrantedList)
        permissionCallbackExperimental?.onPermissionDenied(denied = permissionDeniedList, permanentlyDenied = permissionPermanentlyDeniedList, cancelled = permissionCancelledList)

        if (isRepeatRequestPermissionAllowed) {
            repeatRequestPermission(permissionDeniedList)
        }
    }

    private fun repeatRequestPermission(permissionsDenied: List<String>) {
        if (permissionsDenied.isNotEmpty()) {
            permissionsToRequest = permissionsDenied.toTypedArray()
            requestMultiplePermissionsLauncher?.launch(permissionsToRequest)
        }
    }
}

class EasyPermissionRequestBuilder(activity: ComponentActivity) {
    private val permissionRequest = PermissionRequest(activity)

    fun repeatRequestPermission(isRepeatRequestPermissionAllowed: Boolean): EasyPermissionRequestBuilder {
        permissionRequest.isRepeatRequestPermissionAllowed = isRepeatRequestPermissionAllowed
        return this
    }

    fun setLogging(isLoggingEnabled: Boolean): EasyPermissionRequestBuilder {
        permissionRequest.isLoggingEnabled = isLoggingEnabled
        return this
    }

    fun setPermissionCallback(callback: PermissionCallbackExperimental): EasyPermissionRequestBuilder {
        permissionRequest.permissionCallbackExperimental = callback
        return this
    }

    fun setPermissionCallback(callback: PermissionCallback): EasyPermissionRequestBuilder {
        permissionRequest.permissionCallback = callback
        return this
    }

    fun requestPermission(permissionsToRequest: Array<String>): PermissionRequest {
        permissionRequest.permissionsToRequest = permissionsToRequest
        permissionRequest.requestMultiplePermissionsLauncher?.launch(permissionsToRequest)
        return permissionRequest
    }
}


internal sealed class PermissionState {
    data class Granted(val permission: String) : PermissionState()
    data class Denied(val permission: String) : PermissionState()
    data class PermanentlyDenied(val permission: String) : PermissionState()
    data class Cancelled(val permission: String) : PermissionState()
}

interface PermissionCallbackExperimental {
    fun onPermissionGranted(granted: List<String>)
    fun onPermissionDenied(denied: List<String>, permanentlyDenied: List<String>, cancelled: List<String>)
}

interface PermissionCallback {
    fun onPermissionCallback(granted: List<String>, denied: List<String>)
}





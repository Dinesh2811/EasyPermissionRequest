package com.dinesh.library

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dinesh.permission.EasyPermissionRequestBuilder
import com.dinesh.permission.PermissionCallback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionRequestBuilder = EasyPermissionRequestBuilder(this)
        val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA)

        permissionRequestBuilder.setPermissionCallback(object : PermissionCallback {
            override fun onPermissionCallback(granted: List<String>, denied: List<String>) {
                //  TODO("Implement your code here")
            }
        }).requestPermission(permissionsToRequest)

    }
}
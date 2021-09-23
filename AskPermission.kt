
package com.app.utility

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Created by Chandresh Patel on 23/09/2021.
 * Ask permission run time
 * call [AskPermission.requestPermission] for asking permission
 * override [Activity.onRequestPermissionsResult] method in Activity and call this method [AskPermission.onRequestPermissionsResult] in this.
 * you are getting permission callback.[AskPermission.PermissionCallback]
 * [AskPermission.PermissionCallback.onGranted]  callback for granted all permission by user or already granted
 * [AskPermission.PermissionCallback.onDenied] callback for denied permission by the user
 */
const val PERMISSION_REQUEST = 100

class AskPermission(private val mActivity: Activity) {

    lateinit var permissionCallback: PermissionCallback


    /**
     * TODO request for permission & set callback
     *
     * @param arrPermissionName List of permissions
     * @param permissionCallback provided callback to update about permissions
     */
    fun requestPermission(arrPermissionName: List<String>, permissionCallback: PermissionCallback) {
        this.permissionCallback = permissionCallback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkAllPermissionGranted(arrPermissionName)) {
                mActivity.requestPermissions(arrPermissionName.toTypedArray(), PERMISSION_REQUEST)
            } else {
                permissionCallback.onGranted()
            }
        } else {
            permissionCallback.onGranted()
        }
    }
    /**
     *
     * @param arrPermissionName array of permissions to check if granted or not
     */
    private fun checkAllPermissionGranted(arrPermissionName: List<String>): Boolean {
        for (i in arrPermissionName.indices) {
            if (ContextCompat.checkSelfPermission(mActivity, arrPermissionName[i]) !== PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * TODO requested permission result passing through activity filter permitted permissions & set to callback
     *
     * @param grantResults list of allowed & not granted permissions
     */
    fun onRequestPermissionsResult(grantResults: IntArray) {
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED && null != permissionCallback) {
                permissionCallback.onGranted()

            } else {
                permissionCallback.onDenied()
                break
            }
        }
    }

    interface PermissionCallback {
        /**
         * granted all permission by the use or already granted
         */
        fun onGranted()

        /**
         * denied permission by the user
         */
        fun onDenied()
    }

}
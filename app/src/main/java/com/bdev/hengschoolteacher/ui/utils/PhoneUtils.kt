package com.bdev.hengschoolteacher.ui.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.bdev.hengschoolteacher.ui.activities.BaseActivity

class PhoneUtils {
    companion object {
        fun call(activity: BaseActivity, phone: String) {
            val hasCallPermission = checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED

            if (hasCallPermission) {
                requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), 0)
            } else {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))

                activity.startActivity(intent)
            }
        }
    }
}

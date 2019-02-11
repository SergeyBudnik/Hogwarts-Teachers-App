package com.bdev.hengschoolteacher.ui.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import com.bdev.hengschoolteacher.ui.activities.BaseActivity

class PhoneUtils {
    companion object {
        fun call(activity: BaseActivity, phone: String) {
            val hasCallPermission = checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED

            if (hasCallPermission) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), 0)
            } else {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))

                activity.startActivity(intent)
            }
        }
    }
}

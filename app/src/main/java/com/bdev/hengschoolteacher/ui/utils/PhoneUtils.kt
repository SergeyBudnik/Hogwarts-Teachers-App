package com.bdev.hengschoolteacher.ui.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment

class PhoneUtils {
    companion object {
//        fun call(pageFragment: BasePageFragment, phone: String) {
//            val hasCallPermission = checkSelfPermission(pageFragment, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
//
//            if (hasCallPermission) {
//                requestPermissions(pageFragment, arrayOf(Manifest.permission.CALL_PHONE), 0)
//            } else {
//                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
//
//                pageFragment.startActivity(intent)
//            }
//        }
    }
}

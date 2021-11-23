package com.liyaan.premission.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings


class VIVOStartSetting:MenuStart {
    override fun startActivity(context: Context): Intent {
        val appIntent =
            context.packageManager.getLaunchIntentForPackage("coom.iqoo.secure")

        if (appIntent != null && Build.VERSION.SDK_INT < 23) {
            context.startActivity(appIntent)
        }

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Settings.ACTION_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }
}
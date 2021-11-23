package com.liyaan.premission.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

class DefaultStartSetting:MenuStart  {
    override fun startActivity(context: Context): Intent {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.getPackageName(), null)
        return intent
    }

}
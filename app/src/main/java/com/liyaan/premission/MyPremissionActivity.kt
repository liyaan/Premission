package com.liyaan.premission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.liyaan.premission.callback.IPermission
import com.liyaan.premission.utils.PermissionContant
import com.liyaan.premission.utils.PermissionUtils


class MyPremissionActivity:AppCompatActivity() {
    private var permission: Array<String>? = null
    private var requestCode:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_permission)
        permission = intent.getStringArrayExtra(PermissionContant.PERMISSION_LIST)
        requestCode = intent.getIntExtra(
            PermissionContant.PERMISSION_LIST_CODE,PermissionContant.REQUEST_CODE_DEFALUT)
        permission?.forEach {
            Log.i("aaaaaa",it)
        }
        if (permission == null && requestCode<0 && permisssionListener ==null){
            this.finish()
            return
        }
        val permissionRequest =
            PermissionUtils.hasPermissionRequest(this, *permission!!)
        if (permissionRequest){
            permisssionListener?.success()
            this.finish()
            return
        }
        ActivityCompat.requestPermissions(this,
            permission as Array<out String>,requestCode)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //申请成功
        if (PermissionUtils.requestPermissionSuccess(*grantResults)){
            permisssionListener?.success()
        }else if (PermissionUtils.shouldShowRequestPermissionRationale(this,*permissions)){
            permisssionListener?.denied()

        }else{
            permisssionListener?.cancel()
        }
        this.finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0,0)
    }
    companion object{
        private var permisssionListener: IPermission? = null
        fun permission(context:Context,
                       permissions:Array<String>,requestCode:Int,listener:IPermission){
            permissions.forEach {
                Log.i("aaaa","cccc   $it")
            }
             permisssionListener = listener
            val intent = Intent(context, MyPremissionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(PermissionContant.PERMISSION_LIST, permissions)
            intent.putExtra(PermissionContant.PERMISSION_LIST_CODE, requestCode)
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(0, 0)
            }
        }

    }
}
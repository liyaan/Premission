package com.liyaan.premission

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.liyaan.premission.annotation.Permission
import com.liyaan.premission.annotation.PermissionCancel
import com.liyaan.premission.annotation.PermissionDenied

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    @Permission(permissions = [Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE],requestCode = 100)
    fun requestPermission(view: View){
        Toast.makeText(this, "权限请求成功", Toast.LENGTH_LONG).show();
    }

    @PermissionCancel
    fun requestPermissionCancel() {
        Toast.makeText(this, "权限请求取消", Toast.LENGTH_LONG).show();
    }

    @PermissionDenied
    fun requestPermissionDenied() {
        Toast.makeText(this, "权限请求被拒绝", Toast.LENGTH_LONG).show();
    }
}
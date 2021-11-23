package com.liyaan.premission.aspectj

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.liyaan.premission.MyPremissionActivity
import com.liyaan.premission.annotation.Permission
import com.liyaan.premission.annotation.PermissionCancel
import com.liyaan.premission.annotation.PermissionDenied
import com.liyaan.premission.callback.IPermission
import com.liyaan.premission.utils.PermissionUtils.invokeAnnotation
import com.liyaan.premission.utils.PermissionUtils.startAndroidSettings
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut


@Aspect
class PermissionAspect {

    @Pointcut("execution(@com.liyaan.premission.annotation.Permission * *(..)) && @annotation(permission)")
    fun permission(permission: Permission){

    }
    @Around("permission(permission)")
    @Throws(Throwable::class)
    fun approachMethod(permission: Permission,point: ProceedingJoinPoint){
        val context: Context?
        val `object` = point.getThis()
        context = if (`object` is Context) {
            `object`
        } else if (`object` is Fragment) {
            `object`.context
        } else {
            point.proceed()
            return
        }
        Log.i("aaaa","bbbbbbbb")
        MyPremissionActivity.permission(context!!,
            permission.permissions,permission.requestCode,object:IPermission{
                override fun success() {
                    try {
                        point.proceed()
                    } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                    }
                }

                override fun cancel() {
                    invokeAnnotation(`object`, PermissionCancel::class.java)
                }

                override fun denied() {
                    invokeAnnotation(`object`, PermissionDenied::class.java)
                    startAndroidSettings(context)
                }

            })
    }
}
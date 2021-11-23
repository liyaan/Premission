package com.liyaan.premission.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.collection.SimpleArrayMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.liyaan.premission.menu.DefaultStartSetting
import com.liyaan.premission.menu.MenuStart
import com.liyaan.premission.menu.OPPOStartSetting
import com.liyaan.premission.menu.VIVOStartSetting

import java.lang.reflect.InvocationTargetException
import java.util.*


object PermissionUtils {
    private val TAG = PermissionUtils::class.java.simpleName

    // 定义八种权限
    private var MIN_SDK_PERMISSIONS: SimpleArrayMap<String, Int>? = null
    private val permissionMenu: HashMap<String, Class<out MenuStart?>> =
        HashMap<String, Class<out MenuStart?>>()
    private const val MANUFACTURER_DEFAULT = "Default" //默认
    const val MANUFACTURER_HUAWEI = "huawei" //华为
    const val MANUFACTURER_MEIZU = "meizu" //魅族
    const val MANUFACTURER_XIAOMI = "xiaomi" //小米
    const val MANUFACTURER_SONY = "sony" //索尼
    const val MANUFACTURER_OPPO = "oppo"
    const val MANUFACTURER_LG = "lg"
    const val MANUFACTURER_VIVO = "vivo"
    const val MANUFACTURER_SAMSUNG = "samsung" //三星
    const val MANUFACTURER_LETV = "letv" //乐视
    const val MANUFACTURER_ZTE = "zte" //中兴
    const val MANUFACTURER_YULONG = "yulong" //酷派
    const val MANUFACTURER_LENOVO = "lenovo" //联想

    /**
     * TODO 检查是否需要去请求权限，此方法目的：就是检查 是否已经授权了
     *
     * @param context
     * @param permissions
     * @return 返回false代表需要请求权限，  返回true代表不需要请求权限 就可以结束MyPermisisonActivity了
     */
    fun hasPermissionRequest(
        context: Context,
        vararg permissions: String
    ): Boolean {
        for (permission in permissions) {
            if (permissionExists(
                    permission
                ) && !isPermissionReqeust(
                    context,
                    permission
                )
            ) {
                return false
            }
        }
        return true
    }

    /**
     * 检查当前SDK 权限是否存在 如果存在就return true
     *
     * @param permission
     * @return
     */
    private fun permissionExists(permission: String): Boolean {
        val minVersion = MIN_SDK_PERMISSIONS!![permission]
        return minVersion == null || minVersion <= Build.VERSION.SDK_INT
    }

    /**
     * 判断参数中传递进去的权限是否已经被授权了
     *
     * @param context
     * @param permission
     * @return
     */
    private fun isPermissionReqeust(
        context: Context,
        permission: String
    ): Boolean {
        return try {
            val checkSelfPermission = ContextCompat.checkSelfPermission(context, permission)
            checkSelfPermission == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    // TODO 最后判断下 是否真正的成功
    fun requestPermissionSuccess(vararg gantedResult: Int): Boolean {
        if (gantedResult == null || gantedResult.isEmpty()) {
            return false
        }
        for (permissionValue in gantedResult) {
            if (permissionValue != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // TODO 说白了：就是用户被拒绝过一次，然后又弹出这个框，【需要给用户一个解释，为什么要授权，就需要执行此方法判断】
    // 当用户点击了不再提示，这种情况要考虑到才行
    fun shouldShowRequestPermissionRationale(
        activity: Activity?,
        vararg permissions: String?
    ): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission!!)) {
                return true
            }
        }
        return false
    }

    // TODO 专门去 callback invoke ---》 MainActivity  被注解的方法
    fun invokeAnnotation(`object`: Any, annotationClass: Class<*>?) {
        // 获取 object 的 Class对象
        val objectClass: Class<*> = `object`.javaClass

        // 遍历 所有的方法
        val methods = objectClass.declaredMethods
        for (method in methods) {
            method.isAccessible = true // 让虚拟机，不要去检测 private

            // 判断方法 是否有被 annotationClass注解的方法
            val annotationPresent = method.isAnnotationPresent(annotationClass as Class<out Annotation>)
            if (annotationPresent) {
                // 当前方法 代表包含了 annotationClass注解的
                try {
                    method.invoke(`object`)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
            }
        }
    }

    // TODO 专门去 跳转到 设置界面
    fun startAndroidSettings(context: Context) {
        // 拿到当前手机品牌制造商，来获取 具体细节
        var aClass: Class<*>? =
            permissionMenu[Build.MANUFACTURER.toLowerCase()]
        if (aClass == null) {
            aClass = permissionMenu[MANUFACTURER_DEFAULT]
        }
        try {
            val newInstance = aClass!!.newInstance() // new OPPOStartSettings()
            val iMenu: MenuStart = newInstance as MenuStart // IMenu iMenu = (IMenu) oPPOStartSettings;
            // 高层 面向抽象，而不是具体细节
            val startActivityIntent: Intent = iMenu.startActivity(context)
            if (startActivityIntent != null) {
                context.startActivity(startActivityIntent)
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
    }

    init {
        MIN_SDK_PERMISSIONS = SimpleArrayMap(8)
        MIN_SDK_PERMISSIONS!!.put(
            "com.android.voicemail.permission.ADD_VOICEMAIL",
            14
        )
        MIN_SDK_PERMISSIONS!!.put("android.permission.BODY_SENSORS", 20)
        MIN_SDK_PERMISSIONS!!.put("android.permission.READ_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS!!.put("android.permission.READ_EXTERNAL_STORAGE", 16)
        MIN_SDK_PERMISSIONS!!.put("android.permission.USE_SIP", 9)
        MIN_SDK_PERMISSIONS!!.put("android.permission.WRITE_CALL_LOG", 16)
        MIN_SDK_PERMISSIONS!!.put("android.permission.SYSTEM_ALERT_WINDOW", 23)
        MIN_SDK_PERMISSIONS!!.put("android.permission.WRITE_SETTINGS", 23)
    }

    init {
        permissionMenu[MANUFACTURER_DEFAULT] = DefaultStartSetting::class.java
        permissionMenu[MANUFACTURER_OPPO] = OPPOStartSetting::class.java
        permissionMenu[MANUFACTURER_VIVO] = VIVOStartSetting::class.java
    }
}
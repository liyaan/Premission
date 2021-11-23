package com.liyaan.premission.annotation

import com.liyaan.premission.utils.PermissionContant


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission(val permissions: Array<String>,
                            val  requestCode:Int=PermissionContant.REQUEST_CODE_DEFALUT) {
}
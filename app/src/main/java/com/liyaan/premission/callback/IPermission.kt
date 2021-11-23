package com.liyaan.premission.callback

interface IPermission {
    fun success()

    fun cancel()

    fun denied()
}
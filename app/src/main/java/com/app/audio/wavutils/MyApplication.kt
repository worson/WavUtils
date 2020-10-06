package com.app.audio.wavutils

import android.app.Application
import com.langogo.lib.log.internal.Platform
import com.worson.lib.log.L
import com.worson.lib.log.LogConfiguration
import com.worson.lib.log.LogLevel

/**
 * 说明:
 * @author worson  09.30 2020
 */
class MyApplication :Application(){
    val  TAG = "MyApplication"

    override fun onCreate() {
        super.onCreate()
        initConsoleLog(true)
        L.i(TAG, "onCreate: ")
    }

    fun initConsoleLog(debug:Boolean=true) {
        L.init(
                LogConfiguration.Builder()
                        .tag("WavUtils")
                        .logLevel(if (debug) LogLevel.ALL else LogLevel.DEBUG)
                        .threadInfo(debug)
                        .traceInfo(debug, 7)
                        .addPrinter(Platform.get().defaultPrinter())
                        .build()
        )
    }
}
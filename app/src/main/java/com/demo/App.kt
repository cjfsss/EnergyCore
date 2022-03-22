package com.demo

import android.app.Application
import hos.core.ActivityManager
import hos.core.AppCompat

/**
 * <p>Title: App </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @date : 2022/3/22 21:16
 * @version : 1.0
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompat.getInstance().init(this)
        ActivityManager.getInstance().init(this)
        ActivityManager.getInstance().addFrontBackCallback { activity, front ->

        }
    }

}
package com.demo

import android.app.Application
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.UriUtils
import com.blankj.utilcode.util.Utils
import hos.core.ActivityManager
import hos.core.AppCompat
import hos.core.UriFileConvert

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
        Utils.init(this)
        AppCompat.init(this, UriFileConvert { context, file ->
            UriUtils.file2Uri(FileUtils.getFileByPath(file))
        })
        ActivityManager.getInstance().addFrontBackCallback { activity, front ->

        }
    }

}
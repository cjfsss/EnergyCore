package com.demo

import hos.core.singleton.ISingletonWrapper
import hos.core.singleton.SingletonManager

/**
 * <p>Title: TaskSingleManager </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @date : 2021/9/7 11:39
 * @version : 1.0
 */
class TaskSingleManager : ISingletonWrapper {
    companion object {
        /**
         * 单例
         *
         * @return 当前对象
         */
        @JvmStatic
        fun getInstance(): TaskSingleManager {
            return SingletonManager.get().getInstance(
                    TaskSingleManager::class.java
            ) { TaskSingleManager() }
        }
    }
}

<p align="center"><strong>核心框架</strong></p>



* ##### Application 必须继承AppCompatApplication  相关 -> [AppCompatApplication.java]
```kotlin
class CommonApp : AppCompatApplication() {
    
}
```

* ##### AnimCompat  相关 -> [AnimCompat.java]
```kotlin
var anim:Animation = AnimCompat.loadAnimation(context,R.anim.window_bottom_in)
var animator:Animator = AnimCompat.loadAnimator(context,R.anim.window_bottom_in)
```
* ##### LifecycleHandler  相关 -> [LifecycleHandler.java]
```kotlin
// 创建包含生命周期的Handler
LifecycleHandler(lifecycleOwner) 
// 创建包含生命周期的Handler
LifecycleHandler(lifecycleOwner,looper)
```
* ##### SingletonManager  相关 -> [SingletonManager.java]
```kotlin
class TaskSingleManager : ISingletonWrapper {
    companion object {
        /**
         * 单例
         * @return 当前对象
         */
        @JvmStatic
        fun getInstance(): TaskSingleManager {
            return SingletonManager.get().getInstance(
                    TaskSingleManager::class.java
            ) { TaskSingleManager() }
        }
        /**
         * 单例 随时可能被清理
         * @return 当前对象
         */
        @JvmStatic
        fun getInstance(): TaskSingleManager {
            return SingletonWeakManager.get().getInstance(
                    TaskSingleManager::class.java
            ) { TaskSingleManager() }
        }
    }
}
```
<br>

在项目根目录的 build.gradle 添加仓库

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

在 module 的 build.gradle 添加依赖

```groovy
// 协程库(版本自定)
implementation 'com.github.cjfsss:EnergyCore:0.0.1'
implementation 'androidx.annotation:annotation:1.2.0'
implementation 'androidx.lifecycle:lifecycle-livedata-core:2.3.1'
implementation 'androidx.lifecycle:lifecycle-common:2.3.1'
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.3.1'
implementation 'androidx.lifecycle:lifecycle-runtime:2.3.1'
implementation 'androidx.collection:collection:1.1.0'
```

<br>


## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

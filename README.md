
<p align="center"><strong>核心框架</strong></p>



```
必须调用
```java
    AppCompat.init(this, new UriFileConvert() {
            @Override
            public Uri convertUri(Context context, String file) {
                return UriUtils.file2Uri(FileUtils.getFileByPath(file));
            }
        });
    ActivityManager.getInstance().addFrontBackCallback(this);
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
implementation 'com.github.cjfsss:EnergyCore:0.0.9'
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

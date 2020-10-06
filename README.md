
# AndroidLog
> 一个wav音频的工具库，快速读取音频信息，写音频文件

![](picture_2020_10_06_11___24_54.png)

[项目地址](https://github.com/worson/WavUtils)

基础功能有：
- 读取音频信息，包括采样率、每帧位数等信息
- 写音频文件时，自动填充头信息



# 导入库到项目

根目录gradle文件下配置
```
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
```
应用目录gradle文件下配置依赖
```
implementation 'com.github.worson:WavUtils:0.1'
```

# 使用方法

## 初始化控制台输出日志
此方法会自适应运行平台
```

```

## 初始化文件输出日志

```

```

## 打印日志
```
L.i(TAG, "onCreate: ")
```
打印日志，lamda方法在过滤日志可减少性能消耗
```
L.d(TAG) {"onCreate: "}
```
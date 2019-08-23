# 使用方式
支持中文语音tts的cordova插件

### 下载
```
cordova plugin add cordova-plugin-chinese-tts
```

### ts中使用

```
1. 申明(ts中使用)
declare let cordova:any;

2. 初始化语音引擎
cordova.plugins.chineseTTS.init();


3. 播放
cordova.plugins.chineseTTS.speak("你好");
```

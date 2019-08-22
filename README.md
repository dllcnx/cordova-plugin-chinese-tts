# 使用方式


### 下载
cordova plugin add cordova-plugin-chinese-tts
```

### ts中使用

```
1. 申明
declare let cordova:any;

2. 初始化
cordova.plugins.chineseTTS.init();


3. 播放
cordova.plugins.chineseTTS.speak("你好");
```
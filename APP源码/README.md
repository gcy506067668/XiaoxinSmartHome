#   APP 搭建指南
----------------------------------------------

###  APP的语音交互部分使用了科大迅飞的语音合成，百度的语音唤醒和语音识别以及图灵机器人，这些功能使用需要申请相应的APPKEY、APPID，然后修改对应部分的代码。

###  百度语音唤醒和识别：
####   APP_ID、API_KEY、SECRET_KEY
####  修改AndroidManifest.xml文件： line 29 到 line37
```
<meta-data
    android:name="com.baidu.speech.APP_ID"
    android:value="input your APP_ID" />
<meta-data
    android:name="com.baidu.speech.API_KEY"
    android:value="input your API_KEY" />
<meta-data
    android:name="com.baidu.speech.SECRET_KEY"
    android:value="input your SECRET_KEY" />
```

###   科大讯飞语音合成、图灵机器人
####  /app/src/main/java/online/letmesleep/iotbasedhome/config/Config.java    line 28 29
```
//科大迅飞 APPID
public static final String IFLY_APPID = "**********";     
//图灵机器人APPKEY         
public static final String TULING_APPKEY = "**********";
```

###   百度翻译
####   /app/src/main/java/online/letmesleep/iotbasedhome/config/Config.java    line 30 31
```
public static final String TRANSLATE_APP_ID = "**********";
public static final String TRANSLATE_SECURITY_KEY = "**********";
```


###  当然如果不想使用语音交互部分的功能也可以，在

# andbaseX   

最新开源源码地址 https://gitee.com/zhaoqp/andbase-x

#### 介绍
《Android快速开发框架》，2021最新Android X版本，网络模块OKHttp+RxJava，采用多模块方式组合，封装开发常用组件，真实项目使用，稳定，可靠，持续更新。

核心模块：
核心andbase，qrcode二维码，album自定义相册，ucrop图片裁剪缩放，蓝牙BLE开发，视频播放器player。

#### 导入
下载源码后导入到你项目内进行使用，在settings.gradle配置模块位置，按照选择添加，不强制要求全部添加

```
rootProject.name = 'xxxx-android'
include ':andbase',':qrcode',':app',':album',':ucrop',':player'

project(':andbase').projectDir = new File('../../../project/andbasex/andbase')
project(':qrcode').projectDir = new File('../../../project/andbasex/qrcode')
project(':album').projectDir = new File('../../../project/andbasex/album')
project(':ucrop').projectDir = new File('../../../project/andbasex/ucrop')
project(':player').projectDir = new File('../../../project/andbasex/player')

```

接着在app目录的build.gradle中增加：

```
implementation project(path: ':andbase')
implementation project (path: ':qrcode')
implementation project (path: ':album')
implementation project (path: ':ucrop')
```

好了大功告成，干活吧，事半功倍。

接口请求示例,传入类型，自动json转实体类：

```
AbOkRequestParams requestParams = new AbOkRequestParams();
requestParams.putUrl("key",'111111');
http.get(USER_INFO_URL, requestParams, new AbOkHttpResponseListener<UserInfo>() {

	@Override
	public void onSuccess(UserInfo userInfo) {
		
	}

	@Override
	public void onError(int code, String message, Throwable error) {
				
	}

	@Override
	public void onStart() {
				
	}

	@Override
	public void onComplete() {
					
	}
});

```

等等。

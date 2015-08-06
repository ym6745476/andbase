package com.andbase.global;



public class Constant {
	
	public static final boolean DEBUG = true;
	public static final String sharePath = "andbase_share";
    public static final String USERSID = "user";
    //页面默认显示南京，登陆后显示注册用户的城市
    public static final String CITYID = "cityId";
    public static final String CITYNAME = "cityName";
    public static final String DEFAULTCITYID = "1001";
    public static final String DEFAULTCITYNAME = "南京";
    
    //cookies
    public static final String USERNAMECOOKIE = "cookieName";
    public static final String USERPASSWORDCOOKIE = "cookiePassword";
    public static final String USERPASSWORDREMEMBERCOOKIE = "cookieRemember";
    public static final String ISFIRSTSTART = "isfirstStart";
    
    // 连接超时
 	public static final int timeOut = 12000;
 	// 建立连接
 	public static final int connectOut = 12000;
 	// 获取数据
 	public static final int getOut = 60000;
 	
 	//1表示已下载完成
 	public static final int downloadComplete = 1;
 	//1表示未开始下载
 	public static final int undownLoad = 0;
 	//2表示已开始下载
 	public static final int downInProgress = 2;
 	//3表示下载暂停
 	public static final int downLoadPause = 3;
 	
 	public static final String BASEURL = "http://www.amsoft.cn/";
 	
 	//应用的key
 	//1512528
 	public final static String APPID = "1512528";
 		
 	//百度地图 jfa97P4HIhjxrAgfUdq1NoKC
 	public final static String APIKEY = "jfa97P4HIhjxrAgfUdq1NoKC";
 	
 	//消息服务
	public static final String xmppHost = "192.168.1.106";
	public static final int xmppPort = 5222;    
}

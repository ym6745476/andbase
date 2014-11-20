package com.andbase.im.global;

public class IMConstant {

    /**
     * 消息拦截
     */
    public static final String ACTION_SUFFIX = "com.andbase.action";
    // 新消息
    public static final String ACTION_NEW_MESSAGE = ACTION_SUFFIX + ".NEW_MESSAGE";
    // 回话消息
    public static final String ACTION_CHAT_MESSAGE = ACTION_SUFFIX + ".CHAT_MESSAGE";
    // 系统消息
    public static final String ACTION_SYS_MESSAGE = ACTION_SUFFIX + ".SYS_MESSAGE";

    /**
     * 好友列表 组名
     */
    public static final String ALL_FRIEND = "所有好友";
    public static final String NO_GROUP_FRIEND = "未分组好友";

    /**
     * 花名册有删除的ACTION和KEY
     */
    public static final String ACTION_ROSTER_DELETED = ACTION_SUFFIX + ".ROSTER_DELETED";
    public static final String ROSTER_DELETED_KEY = "roster.deleted.key";

    /**
     * 花名册有更新的ACTION和KEY
     */
    public static final String ACTION_ROSTER_UPDATED = ACTION_SUFFIX + ".ROSTER_UPDATED";
    public static final String ROSTER_UPDATED_KEY = "roster.updated.key";

    /**
     * 花名册有增加的ACTION和KEY
     */
    public static final String ACTION_ROSTER_ADDED = ACTION_SUFFIX + ".ROSTER_ADDED";
    public static final String ROSTER_ADDED_KEY = "roster.added.key";

    /**
     * 花名册中成员状态有改变的ACTION和KEY
     */
    public static final String ACTION_ROSTER_PRESENCE_CHANGED = ACTION_SUFFIX + ".ROSTE_RPRESENCE_CHANGED";
    public static final String ROSTER_PRESENCE_CHANGED_KEY = "roster.presence.changed.key";

    /**
     * 收到好友邀请请求
     */
    public static final String ACTION_ROSTER_SUBSCRIPTION = ACTION_SUFFIX + ".ROSTER_SUBSCRIBE";
    public static final String ROSTER_SUB_FROM = "roster.subscribe.from";
    

    /**
     * 我的消息
     */
    public static final String MY_NEWS = "my.news";
    public static final String MY_NEWS_DATE = "my.news.date";

    /**
     * 服务器的配置
     */
    public static final String IMSHARE = "im_share";  // 登录设置
    public static final String USERNAME = "user_name"; // 账户
    public static final String PASSWORD = "password"; // 密码
    public static final String XMPP_HOST = "xmpp_host"; // 地址
    public static final String XMPP_PORT = "xmpp_port"; // 端口
    public static final String XMPP_SEIVICE_NAME = "xmpp_service_name"; // 服务名
    public static final String IS_NOVISIBLE = "isNovisible"; // 是否隐身
    public static final String IS_ONLINE = "is_online";
    
    /**录音的文件类型*/
    public static final String AMR_FILE = ".amr";
    
    public static final String XMPP_CONNECTION_CLOSED = "xmpp_connection_closed";// 连接中断

    public static final String LOGIN = "login"; // 登录
    public static final String RELOGIN = "relogin"; // 重新登录

    /**
     * 系统消息
     */
    public static final String MSG_TYPE = "broadcast";// 消息类型关键字
    public static final String SYS_MSG = "sysMsg";// 系统消息关键字
    public static final String SYS_MSG_DIS = "系统消息";// 系统消息
    public static final String ADD_FRIEND_QEQUEST = "好友请求";// 系统消息关键字
    /**
     * 请求某个操作返回的状态值
     */
    public static final int SUCCESS = 0;// 存在
    public static final int FAIL = 1;// 不存在
    public static final int UNKNOWERROR = 2;// 出现莫名的错误.
    public static final int NETWORKERROR = 3;// 网络错误
    /***
     * 企业通讯录根据用户ｉｄ和用户名去查找人员中的请求ｘｍｌ是否包含自组织
     */
    public static final int containsZz = 0;
    /***
     * 创建请求分组联系人列表xml分页参数
     */
    public static final String currentpage = "1";// 当前第几页
    public static final String pagesize = "1000";// 当前页的条数

    /***
     * 创建请求xml操作类型
     */
    public static final String add = "00";// 增加
    public static final String rename = "01";// 增加
    public static final String remove = "02";// 增加

    /**
     * 重连接状态acttion
     * 
     */
    public static final String ACTION_RECONNECT_STATE = ACTION_SUFFIX + ".ACTION_RECONNECT_STATE";
    /**
     * 描述冲连接状态的关机子，寄放的intent的关键字
     */
    public static final String RECONNECT_STATE = "reconnect_state";
    
    /**
     * 描述连接，
     */
    public static final boolean RECONNECT_STATE_SUCCESS = true;
    public static final boolean RECONNECT_STATE_FAIL = false;
  
    
    /**
     * 精确到毫秒
     */
    public static final String MS_FORMART = "yyyy-MM-dd HH:mm:ss SSS";

}

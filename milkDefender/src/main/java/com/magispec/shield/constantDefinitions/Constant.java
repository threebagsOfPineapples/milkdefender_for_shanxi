package com.magispec.shield.constantDefinitions;

import java.util.ArrayList;
import java.util.HashMap;

import com.magispec.shield.domain.AppVersionInfo;
import com.magispec.shield.domain.FWVersionInfo;
import com.magispec.shield.domain.MilkInfo;
import com.magispec.shield.domain.Record;

public class Constant {
	public static final int DATA_SEND=0x01;
	//public static final int SKIP_DETE=2;
	public static final int SCAN_OVER=0x02;
	public static final int STRAT_COLLECT=0x03;
	public static final int CONN_AUTHORIZE=0x04;
	public static final int OUT_TIME=0x05;
	public static final int BT_CONNECT=0x06;
	public static final int BT_DISCONNECTED=0x07;
	public static final int BT_CONNECTING=0x08;
	public static final int MilkName=0x09;
	public static final int BTPAIR=0x10;
	public static final int TEST_DARK=0x10;
	public static final int OAD=0x11;
	public static final int  FW_CHECK=0x12;
	public static final int CHECKGOOD=0x13;
	public static final int CALIBRATE_TIME=0x14;
	public static final int DISCONNECT=0x15;
	public static final String APP_ID = "wxbcd44698935af441";
	public static final String App_SECRET = "d4624c36b6795d1d99dcf0547af5443d";
	public static String  SESSIONID=null;
	public static String  OPENID=null;
	public static String  DARKREFID=null;
	public static MilkInfo ResultmilkInfo=null;
	public static MilkInfo ScanmilkInfo=null;
	public static String  MILKRESULT=null;
	public static String  RECORDSCOUNT=null; 
	public static Record record=null;
	public static FWVersionInfo  fWVersionInfo=null;
	public static AppVersionInfo appVersionInfo=null;
	public static String  LOCATION_PROVICE="";
	public static String  LOCATION_CITY="";
	public static String  LOCATION_DISTRICT="";
	public static String  FW_VERSION;
	public static String resultMilkId=null;
	public static String matchingDegree=null;
	public static String similarity=null;
	public static String milkNames=null;
	public static String recordId="";
	public static String tt="我刚刚用了奶粉卫士检测了宝宝奶粉的真假，快来看看吧";
	public static String ps;
	public static String tm;
	public static String byps;
	public static String bypc;
	public static String tmn;
	public static String tmpu;
	public static String  rmd;
	public static String  cs;
	public static String  desc;
	public static String Location="";
	//6.0动态权限
	public static final int REQUECT_CODE_CAMERA=1;
	public static final int REQUECT_CODE_SDCARD = 2;
	public static final int REQUECT_CODE_LOACTION=3;
	public static final int REQUECT_CODE_DIAGL=4;
	//
	public static final int MILLINFO=0x100;
	public  static int mInit = 0;
	public static final String HTTPS_URL_HEAD = "https://123.56.229.50/";
	public static final String HTTPS_URL_LOGIN = HTTPS_URL_HEAD + "magispec-wp/login.php";
	public static final String HTTPS_URL_MSG_CENTRAL = HTTPS_URL_HEAD + "magispec-wp/msg_central.php";
	public  static final String HTTPS_URL_MSG_AppVersion = HTTPS_URL_HEAD + "magispec-wp/msg_central_nosession.php";
	public static final String JSON_KEY_SESSIONID = "SESSIONID";
	public static final String JSON_KEY_OPENID = "OPENID";
	public static final String JSON_KEY_ACTION = "ACTION";
	public static final String JSON_KEY_TYPE = "TYPE";
	public static final String JSON_KEY_DATA = "DATA";
	public static final String JSON_KEY_RESULT = "RESULT";
	public static final String JSON_KEY_DATA_resultMilkId="resultMilkId";
	public static final String JSON_KEY_DATA_matchingDegree="matchingDegree";
	public static final String JSON_KEY_DATA_similarity ="similarity";
	public static final String JSON_KEY_DATA_milksName ="milksName";
	public static final String JSON_KEY_DATA_ifDarkRefValid ="ifDarkRefValid";
	public static final short ACTION_CREATE_SESSION = 0x0001;
	public static final short ACTION_DESTROY_SESSION = 0x0002;
	public static final short ACTION_UPDATE_SESSION=0x0003;
	public static final short ACTION_MILKDEFENDER_GET_USER_INFO = 0x0100;
	public static final short ACTION_MILKDEFENDER_UPLOAD_DARKREF_DATA = 0x0101;
	public static final short ACTION_MILKDEFENDER_GET_MILK_INFO = 0x0102;
	public static final short ACTION_MILKDEFENDER_RECOGNIZE_MILK = 0x0103;
	public static final short ACTION_MILKDEFENDER_GET_RECORDS_COUNT = 0x0104;
	public static final short ACTION_MILKDEFENDER_GET_RECORDS = 0x0105;
	public static final short ACTION_MILKDEFENDER_GET_FW_LATEST_VERSION = 0x0106;
	public static final short ACTION_MILKDEFENDER_GET_APP_LATEST_VERSION = 0x0107;
	public static final short ACTION_MILKDEFENDER_SET_USER_NICKNAME = 0x0108;	
	public static final short ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_ROLLING=0x0109;
	public static final short  ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_LIST=0x010A;
	public static final short  ACTION_MILKDEFENDER_SUGGESTION =0x010B;
	public static final short ACTION_MILKDEFENDER_GET_DAMAP =0x010C;
	//shied消息
	public static final short ACTION_SHIELD_GET_MODEL_LIST=0x0400;
	public static final short ACTION_SHIELD_UPLOAD_DARKREF_DATA=0x0401;
	public static final short  ACTION_SHIELD_RECOGNIZE=0x0402;
	public static final short  ACTION_SHIELD_GET_STD_CURVE=0x0403;
	public static final short  ACTION_SHIELD_SET_USER_NICKNAME=0x0404;

	public static final byte MSG_REQ = 0x00;
	public static final byte MSG_RES = 0x01;
	//消息组装
	public static final int MSG_STATE_IDLE = 0;
	public static final int MSG_STATE_RECV_HDR = 1;
	public static final int MSG_STATE_RECV_PAYLOAD = 2;
	public static final int MSG_STATE_PROCESSING = 3;	
	public static final byte MSG_MAGISPEC_ID_LSB = (byte) 0xF4;
	public static final byte MSG_MAGISPEC_ID_MSB = 0x52;
	public static final byte MSG_MAGISPEC_TYPE=0x00; 
	public static final byte MSG_CMD_COLLECT=0x09; 
	public static final byte MSG_CMD_SPECTRUM_NOTIFY = (byte) 0xa0;
	public static final byte MSG_CMD_FONT=(byte)0x51;
	public static final byte MSG_CMD_FONT_NAME=(byte)0x52;
	public static final byte MSG_CMD_CONN_AUTHORIZE=(byte)0x53;
	public static final byte MSG_CMD_DARKEVN_CALIB = (byte) 0x54;
	public static final byte MSG_CMD_REF1_CALIB = (byte) 0x55;
	public static final byte MSG_CMD_SET_DARK_SPECTRUM_DATA = (byte) 0x56;	
	public static final byte MSG_CMD_SET_REF_SPECTRUM_DATA = (byte) 0x57;		
	public static final byte MSG_CMD_PAIR=(byte)0x58;
	//获取固件版本
	public static final byte MSG_CMD_GET_FW_VER=(byte)0x59;
	public static final byte MSG_CMD_UPGRADE=(byte)0x60;
	//获取上次校准的时间
	public static final byte MSG_GET_CALIBRATE_TIME = (byte) 0x62;
	//设置自动校准的时间
	public static final byte MSG_SET_AUTO_CALIBRATE_TIME = (byte) 0x63;
	//获取波长步进
	public static final byte MSG_GET_WAVE_STEP=(byte) 0x64;
	//设置起始波长
	public static final byte MSG_SET_WAVE_START=(byte) 0x65;
	//8获取波长映射数据 (0x66)-MS_TM_GET_WAVE_MAP
	public static final byte MSG_GET_WAVE_MAP=(byte)0x66;
	//设置测试目标
	public static final byte MS_TM_SET_TEST_OBJ=(byte) 0x67;
	//测试字体专用
	public static int MilkCount=1;
	//记录条目
	public static int ResultCountInt;
	//记录
	public static  ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	public static    HashMap<String, Object> map = new HashMap<String, Object>();
    public static	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    //发现
    public static   String[] imageDescriptions ;
    public static String []imageUrls;
    public static String []webUrls;
   public static ArrayList<String> labels ;
    public static  ArrayList<HashMap<String, Object>> listItem_discovery_list = new ArrayList<HashMap<String, Object>>();
	public static    HashMap<String, Object> map_discovery = new HashMap<String, Object>();
    public static	ArrayList<HashMap<String, Object>> list_discovery = new ArrayList<HashMap<String, Object>>();
    //记录测量次数
    public static int MeasuringTimes=0;
    //记录 最优测试
    public static int MatchingDegree=0;
	//shied
	public static String degree=null;
	public static String curve=null;
	public static String modeltype="";
	public static String modelid="";
	public static String modelobjectid="";
	public static String modelobjectNumber="";
	public static double [] SampleCurve;
	public static double [] StandardCurve;
    //dark ref 是否合格
    public  static String ifDarkRefValid ;
    //dark ref
    public static boolean IsAppNew=false;
    //是否是在检测页面测试Dark
    public static boolean IsTesting=false;
    //分享界面
    public static String shareUrl="http://123.56.229.50/magispec-1.0/sharing.php?TT=tt&PS=ps&TM=tm&BYPS=byps&BYPC=bypc&TMN=tmn&TMPU=tmpu&RMD=rmd&CS=cs";
    public static String shareUrlHead="http://eincloud.vicp.io//magispec-1.0/sharing.php?";
    public static String shareUrlBranch="TT=tt&PS=pos&TM=tim&BYPS=byps&BYPC=bypc&TMN=tmn&TMPU=tmpu&RMD=rmd";
   
}

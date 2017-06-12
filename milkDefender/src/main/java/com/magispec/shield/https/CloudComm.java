package com.magispec.shield.https;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.magispec.shield.activity.MainActivity;
import com.magispec.shield.activity.PieCustomActivity;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.AppVersionInfo;
import com.magispec.shield.domain.FWVersionInfo;
import com.magispec.shield.domain.MilkInfo;
import com.magispec.shield.fragment.FragmentAuth;
import com.magispec.shield.fragment.FragmentDetection;
import com.magispec.shield.fragment.FragmentHome;
import com.magispec.shield.service.BaseApplicaton;
import com.magispec.shield.utils.SharePreferenceUtil;
import com.magispec.shield.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Context;

import okhttp3.Call;

public class CloudComm {
    static public Context mAppContext;
    boolean result;
    private String mSessionId = "";
    private String mIfNewUser = "";
    private String mOpenId = "";
    private String DarkRefId = "";
    String milkResult = null;
    String recordsCount = null;

    /**
     * for create session from server
     *
     * @param openId : weixin open ID
     * @param type   : app
     * @return ok: true failed: false
     * @throws JSONException
     */
    public void createSession(final String openId, String type) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject json_resp = null;
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, null);
        json.put(Constant.JSON_KEY_OPENID, openId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_CREATE_SESSION);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        data.put("apptype", "APPTYPE_SHIELD");
        data.put("logintype", type);
        data.put("username", "usermagispec");// need to hide ...???
        data.put("password", "11qqaazZ");
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_LOGIN).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        ToastUtil.showToast(BaseApplicaton.getAppContext(), "服务器异常");
                        result = false;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {
                                ToastUtil.showToast(BaseApplicaton.getAppContext(), "服务器数据异常");
                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                System.out.println("message:" + json_resp.get(Constant.JSON_KEY_RESULT) + "Data:");
                                JSONObject data = (JSONObject) json_resp.get(Constant.JSON_KEY_DATA);
                                if (data != null) {
                                    mSessionId = data.getString("sessionID");
                                    mIfNewUser = data.getString("ifNewUser");
                                    System.out.println("createOk" + "mId" + mSessionId + "NU" + mIfNewUser);
                                    Constant.OPENID = openId;
                                    Constant.SESSIONID = mSessionId;
                                } else {
                                    ToastUtil.showToast(BaseApplicaton.getAppContext(), "服务器异常");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                ToastUtil.showToast(BaseApplicaton.getAppContext(), "服务器数据异常");
                            }
                        } else {

                        }
                    }
                });
    }

    public void updateSession(final String openId, String sessionid, String position) throws JSONException {
        JSONObject json = new JSONObject();
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, sessionid);
        json.put(Constant.JSON_KEY_OPENID, openId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_UPDATE_SESSION);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        data.put("position", position);
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println("获取信息json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        result = false;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                                System.out.println("string:" + string + "json33:" + json_resp.get("RESULT"));

                                if (json_resp.get("RESULT").equals("OK")) {
                                    System.out.println("string:" + string + "json33:" + json_resp.get("RESULT"));
                                } else {
                                    ToastUtil.showToast(BaseApplicaton.getAppContext(), "服务器异常");
                                }

                            } catch (JSONException e) {
                                ToastUtil.showToast(BaseApplicaton.getAppContext(), "服务器异常");
                                e.printStackTrace();
                            }
                            /*
							 * try { getUserName(Constant.OPENID,
							 * Constant.SESSIONID); } catch (JSONException e) {
							 * // TODO Auto-generated catch block
							 * e.printStackTrace(); }
							 */
                        } else {

                        }
                    }
                });
    }

    public void setUserName(final String openId, String sessionid, String userName) throws JSONException {
        JSONObject json = new JSONObject();
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, sessionid);
        json.put(Constant.JSON_KEY_OPENID, openId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_SHIELD_SET_USER_NICKNAME);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        data.put("nickname", userName);
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println("获取信息json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        result = false;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
							/*
							 * try { getUserName(Constant.OPENID,
							 * Constant.SESSIONID); } catch (JSONException e) {
							 * // TODO Auto-generated catch block
							 * e.printStackTrace(); }
							 */

                        } else {

                        }
                    }
                });
    }

    public void getUserName(final String openId, String sessionid) throws JSONException {
        JSONObject json = new JSONObject();
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, sessionid);
        json.put(Constant.JSON_KEY_OPENID, openId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_USER_INFO);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();

        // data.put("nickname", userName);
        json.put(Constant.JSON_KEY_DATA, "");

        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        result = false;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);

                        } else {

                        }
                    }
                });
    }

    /**
     * 销毁会话
     *
     * @return :true or false
     * @throws JSONException
     */
    public void destroySession(String mSessionID, String mOpenId) throws JSONException {
        JSONObject json = new JSONObject();
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, mSessionID);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_DESTROY_SESSION);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        json.put(Constant.JSON_KEY_DATA, null);
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        result = false;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")) {

                                } else {
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                        }
                    }

                });
    }

    /**
     * update dark data to service
     *
     * @param :       darkData refData
     * @param mOpenId
     * @return ok: true failed: false
     * @throws JSONException
     */
    public void updateDarkRefData(int[] darkData, int[] refData, String mOpenId, String sessionid)
            throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray jsonDarkArray = new JSONArray();
        JSONArray jsonRefArray = new JSONArray();
        json.put(Constant.JSON_KEY_SESSIONID, sessionid);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_SHIELD_UPLOAD_DARKREF_DATA);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        for (int i = 0; i < darkData.length; i++) {
            jsonDarkArray.put(darkData[i]);
            jsonRefArray.put(refData[i]);
        }
        data.put("dark", jsonDarkArray);
        data.put("ref", jsonRefArray);
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println(json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String string) {
                        System.out.println("---------------string---------:" + string);
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get("RESULT").equals("OK")
                                        && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                    Constant.DARKREFID = (String) json_resp.get(Constant.JSON_KEY_DATA);
                                } else {
                                    Constant.DARKREFID = null;
                                }
                                if (Constant.IsTesting) {
                                    FragmentDetection.mhandler.sendEmptyMessage(3);
                                } else {
                                    MainActivity.mhander.sendEmptyMessage(Constant.CHECKGOOD);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            Constant.DARKREFID = null;
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        Constant.DARKREFID = null;
                    }
                });
    }

    /**
     * @param b         一维码(0) 奶粉ID(1)
     * @param s         一维码数据？奶粉ID？
     * @param mOpenId
     * @param sessionid
     * @throws JSONException
     */
    public void getMilkInfo(final byte b, String s, String mOpenId, String sessionid) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject json_resp = null;
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, sessionid);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_MILK_INFO);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        data.put("way", b);
        data.put("code", s);
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
                                        && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                    Gson gson = new Gson();
                                    if (b == 0x01) {
										/*
										 * if(PieActicity.milk1==null){
										 * PieActicity.milk1 = gson.fromJson(
										 * json_resp.get(Constant.JSON_KEY_DATA)
										 * .toString(), MilkInfo.class);
										 * PieActicity.mhandler.sendEmptyMessage
										 * (0x01);
										 * 
										 * }else{ PieActicity.milk1 =
										 * gson.fromJson(
										 * json_resp.get(Constant.JSON_KEY_DATA)
										 * .toString(), MilkInfo.class);
										 * PieActicity.mhandler.sendEmptyMessage
										 * (0x02); }
										 */
                                        Constant.ResultmilkInfo = gson.fromJson(
                                                json_resp.get(Constant.JSON_KEY_DATA).toString(), MilkInfo.class);

                                        PieCustomActivity.milkList.add(Constant.ResultmilkInfo);
										/*
										 * SharePreferenceUtil.
										 * saveOrUpdateAttribute(BaseApplicaton.
										 * getAppContext(), "SP",
										 * "ResultmilkInfo",
										 * json_resp.get(Constant.JSON_KEY_DATA)
										 * .toString());
										 */

                                    } else {
                                        Constant.ScanmilkInfo = gson.fromJson(
                                                json_resp.get(Constant.JSON_KEY_DATA).toString(), MilkInfo.class);

                                        SharePreferenceUtil.saveOrUpdateAttribute(BaseApplicaton.getAppContext(), "SP",
                                                "ScanmilkInfo", json_resp.get(Constant.JSON_KEY_DATA).toString());
                                        FragmentHome.homeHandler.sendEmptyMessage(Constant.MILLINFO);
                                    }

                                } else {


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                        }
                    }

                });

    }

    /**
     * 识别奶粉
     *
     * @param mac
     * @param position
     * @param darkRefId
     * @param modeltype
     * @param modelid
     * @param modelobjectid
     * @param spectrumData
     * @param mOpenId
     * @param mSessionId
     * @throws JSONException
     */

    public void RecognizeMilk(String mac, String position, String darkRefId, final String modeltype, final String modelid,
                              String modelobjectid, int[] spectrumData, final String mOpenId, final String mSessionId)
            throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray jsonarray = new JSONArray();

        json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_SHIELD_RECOGNIZE);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        if (modelobjectid == null) {
            modelobjectid = "0";
        }
        data.put("mac", mac);
        data.put("position", position);
        data.put("darkrefid", darkRefId);
        data.put("modeltype", modeltype);
        data.put("modelid", modelid);
        data.put("modelobjectid", modelobjectid);
        for (int i = 0; i < spectrumData.length; i++) {
            jsonarray.put(spectrumData[i]);
        }
        data.put("spectrum", jsonarray);
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println("-----json-----" + json.toString());
        final String finalModelobjectid = modelobjectid;
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("onError:" + arg1);
                        milkResult = null;
                    }

                    @Override
                    public void onResponse(String string) {
                        System.out.println("2--------string-------1" + string);
                        JSONObject json_resp = null;
                        JSONObject json_data = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("string111:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
                                        && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                    json_data = json_resp.getJSONObject(Constant.JSON_KEY_DATA);
                                    Constant.curve = (String) json_data.get("curve");
                                    Constant.degree = (String) json_data.get("degree");
                                    String[] test = ((String) json_data.get("curve")).split(" ");
                                    Constant.SampleCurve = new double[test.length];
                                    for (int i = 0; i < test.length; i++) {
                                        Constant.SampleCurve[i] = Double.parseDouble(test[i]);
                                        System.out.println("double["+i+"]"+Double.parseDouble(test[i]));
                                    }
                                    FragmentDetection.mhandler.sendEmptyMessage(Constant.MILLINFO);
                                    String s = (String) SharePreferenceUtil.getAttributeByKey(BaseApplicaton.getAppContext()
                                            , "SP", "standCurv", 0);
                                    if (s.equals(modeltype + modelid + finalModelobjectid) && s.length() > 200) {
                                        String[] test1 = s.split(",");
                                        Constant.StandardCurve = new double[test1.length];
                                        for (int i = 0; i < test1.length; i++) {
                                            System.out.println("---test----" + Double.parseDouble(test1[i]));
                                            Constant.StandardCurve[i] = Double.parseDouble(test1[i]);
                                        }
                                    } else {
                                        getStanderCruv(mOpenId, mSessionId, modeltype, modelid, finalModelobjectid);
                                    }


                                } else {
                                    MainActivity.Sample = null;
                                    Constant.curve = null;
                                    ToastUtil.showToast(BaseApplicaton.getAppContext(), "数据异常");
                                }
                            } catch (Exception e) {
                                ToastUtil.showToast(BaseApplicaton.getAppContext(), "服务器数据异常");
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                FragmentDetection.mhandler.sendEmptyMessage(Constant.MILLINFO);
                            }
                        } else {
                            Constant.curve = null;
                        }
                    }

                });
    }

    /**
     * 获取record 总数
     *
     * @param mOpenId
     * @param mSessionId
     * @return
     * @throws JSONException
     */
    public void getRecordsCount(String mOpenId, String mSessionId) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_RECORDS_COUNT);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        json.put(Constant.JSON_KEY_DATA, "");
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("onError:" + arg1);
                        recordsCount = null;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
                                        && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                    Constant.RECORDSCOUNT = (String) json_resp.get(Constant.JSON_KEY_DATA);

                                } else {
                                    Constant.RECORDSCOUNT = null;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            Constant.RECORDSCOUNT = null;
                        }
                    }

                });
    }

    /**
     * @param startIndex 记录开始索引
     * @param count      从记录开始索引到之前的count个记录（从最新的记录到较新的记录）， count最大为10
     * @param mOpenId
     * @param mSessionId
     * @return Record
     * @throws JSONException
     */

    public void getRecords(String startIndex, String count, String mOpenId, String mSessionId) throws JSONException {
        JSONObject json = new JSONObject();
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_RECORDS);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        data.put("startIndex", startIndex);
        data.put("count", count);
        json.put(Constant.JSON_KEY_DATA, data.toString());
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        Constant.record = null;
                        System.out.println("这是记录error-----------------------------------------------");
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            System.out.println("这是记录-----------------------------------------------");
                            System.out.println("string:" + string + "json:" + json_resp);
							/*
							 * try { if
							 * (json_resp.get(JSON_KEY_RESULT).equals("OK") &&
							 * json_resp.get(JSON_KEY_DATA) != null) { Gson gson
							 * = new Gson(); Constant.record =
							 * gson.fromJson((String)
							 * json_resp.get(JSON_KEY_DATA), Record.class);
							 * 
							 * } else { Constant.record = null; }
							 * 
							 * } catch (JSONException e) { // TODO
							 * Auto-generated catch block e.printStackTrace(); }
							 */

                        } else {
                            Constant.record = null;
                        }
                    }

                });
    }

    /**
     * @param mOpenId
     * @param mSessionId
     * @return
     * @throws JSONException
     */
    public void getFwLastestVersion(String mOpenId, String mSessionId) throws JSONException {
        JSONObject json = new JSONObject();

        json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_FW_LATEST_VERSION);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        json.put(Constant.JSON_KEY_DATA, "");
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        Constant.fWVersionInfo = null;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
                                        && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                    Gson gson = new Gson();
                                    Constant.fWVersionInfo = gson.fromJson(
                                            json_resp.get(Constant.JSON_KEY_DATA).toString(), FWVersionInfo.class);
                                    System.out.println("code:" + Constant.fWVersionInfo.getVersionCode() + "number:"
                                            + Constant.fWVersionInfo.getVersionName());
                                    FragmentAuth.mHandler.sendEmptyMessage(2);
                                } else {
                                    Constant.fWVersionInfo = null;
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            Constant.fWVersionInfo = null;
                        }
                    }

                });
    }

    public void getAppLastestVersion(String mOpenId, String mSessionId) throws JSONException {
        JSONObject json = new JSONObject();
        if (!mSessionId.isEmpty()) {
            System.out.println("Session just can create for once ...");
        }
        json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_APP_LATEST_VERSION);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        json.put(Constant.JSON_KEY_DATA, "");
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                        Constant.appVersionInfo = null;
                    }

                    @Override
                    public void onResponse(String string) {
                        JSONObject json_resp = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
                                        && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                    Gson gson = new Gson();
                                    Constant.appVersionInfo = gson.fromJson(
                                            (String) json_resp.get(Constant.JSON_KEY_DATA), AppVersionInfo.class);
                                } else {
                                    Constant.appVersionInfo = null;
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {

                            Constant.appVersionInfo = null;
                        }

                    }

                });
    }

    public void getbannerPicture(String mOpenId, String mSessionId, String index, String count) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject json_resp = null;
        json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_ROLLING);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        data.put("startIndex", index);
        data.put("count", count);
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String string) {
                        System.out.println("String:" + string);

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        // TODO Auto-generated method stub
                        System.out.println("onError");
                    }
                });
    }

    public void getDiscoveryList(String mOpenId, String mSessionId, String index, String count) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject json_resp = null;
        json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
        json.put(Constant.JSON_KEY_OPENID, mOpenId);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_LIST);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        JSONObject data = new JSONObject();
        data.put("startIndex", index);
        data.put("count", count);
        json.put(Constant.JSON_KEY_DATA, data);
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String string) {
                        System.out.println("String:" + string);

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        // TODO Auto-generated method stub
                        System.out.println("onError");
                    }
                });
    }


    public void getStanderCruv(String openId, String mSessionId, final String modeltype, final String modelid, final String modelobjectid) {
        JSONObject json = new JSONObject();
        JSONObject json_resp = null;
        try {
            json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
            json.put(Constant.JSON_KEY_OPENID, openId);
            json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_SHIELD_GET_STD_CURVE);
            json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
            JSONObject data = new JSONObject();
            data.put("modeltype", modeltype);
            data.put("modelid", modelid);
            data.put("modelobjectid", modelobjectid);
            json.put(Constant.JSON_KEY_DATA, data);
        } catch (JSONException e) {
            ToastUtil.showToast(BaseApplicaton.getAppContext(), "数据处理异常");
            e.printStackTrace();
        }
        System.out.println("json" + json);
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        System.out.println("Error:" + arg1);
                    }

                    @Override
                    public void onResponse(String string) {
                        System.out.println("---------------string---------:" + string);
                        JSONObject json_resp = null;
                        JSONObject json_data = null;
                        if (string != null) {
                            try {
                                json_resp = new JSONObject(string);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("string:" + string + "json:" + json_resp);
                            try {
                                if (json_resp.get("RESULT").equals("OK")
                                        && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                    json_data = json_resp.getJSONObject(Constant.JSON_KEY_DATA);
                                    String data_s = (String) json_data.get("curve");
                                    if (data_s.length() >20) {
                                        String[] test = ((String) json_data.get("curve")).split(",");
                                        Constant.StandardCurve = new double[test.length];
                                        for (int i = 0; i < test.length; i++) {
                                            Constant.StandardCurve[i] = Double.parseDouble(test[i]);
                                        }
                                        SharePreferenceUtil.saveOrUpdateAttribute(BaseApplicaton.getAppContext(), "SP"
                                                , "model", modeltype + modelid + modelobjectid);
                                        SharePreferenceUtil.saveOrUpdateAttribute(BaseApplicaton.getAppContext(), "SP"
                                                , "standCruv", (String) json_data.get("curve"));
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            Constant.DARKREFID = null;

                        }

                    }
                });
    }


}
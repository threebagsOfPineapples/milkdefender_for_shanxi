package com.magispec.shield.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.Midcategory;
import com.magispec.shield.fragment.FragmentDetection;
import com.magispec.shield.utils.ReadTxtFileUtils;
import com.magispec.shield.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * Created by guofe on 2016/9/1 0001.
 */
public class CategoryActivity extends Activity {
    private TagFlowLayout tffl, tffl1, tffl2;
    private ImageView title_back;
    public static int item;
    private TextView tv, tv1;
    private String[] mVals;
    private String[] all = new String[]{"大米", "茶叶", "金银花"};
    private String[] product = new String[]{"大米", "茶叶"};
    private String[] yao = new String[]{"金银花"};
    private String[] s;
    private String[] ss;
    private ArrayList<String> category = new ArrayList<>();
    private HashMap<String, ArrayList<String>> categoryMap = new HashMap<>();
    private ArrayList<String> category1 = new ArrayList<>();
    private HashMap<String, Midcategory> midCatecoryMap = new HashMap<>();
    private HashMap<String, String> littleCategory = new HashMap<>();
    Intent intent = new Intent();
    private HashMap<String, ArrayList<String>> categoryMap1 = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category);

        initWindow();
        try {
            JSONObject json = new JSONObject();
            json.put(Constant.JSON_KEY_SESSIONID, Constant.SESSIONID);
            json.put(Constant.JSON_KEY_OPENID, Constant.OPENID);
            json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_SHIELD_GET_MODEL_LIST);
            json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
            json.put(Constant.JSON_KEY_DATA, "");
            System.out.println("json:-----" + json);
            OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onResponse(String string) {
                            try {
                                System.out.print("String:---" + string);
                                JSONObject json1 = new JSONObject(string);
                                JSONObject json2 = new JSONObject(json1.get("DATA").toString());
                                int a = Integer.valueOf(json2.get("classcount").toString());
                                for (int i = 0; i < a; i++) {
                                    JSONObject json11 = new JSONObject(json2.get("class_" + (i)).toString());
                                    int aa = Integer.valueOf(json11.get("subclasscount").toString());
                                    String sss = json11.get("name").toString();
                                    System.out.println("aa:" + aa + sss);
                                    category.add(sss);
                                    ArrayList<String> ss = new ArrayList<>();
                                    for (int j = 0; j < aa; j++) {
                                        JSONObject json111 = new JSONObject(json11.get("subclass_" + (j)).toString());
                                        Gson gson = new Gson();
                                        Midcategory uu = gson.fromJson(json11.get("subclass_" + (j)).toString(), Midcategory.class);
                                        System.out.println("Midcategory:" + uu.getModleid() + uu.getPhotourl() + uu.getModelobjectdesc());
                                        int aaa = Integer.valueOf(json111.get("modelobjectcount").toString());
                                        System.out.println("aaa:" + aaa);
                                        category1.add(json111.get("name").toString());
                                        midCatecoryMap.put(json111.get("name").toString(), uu);
                                        ss.add(json111.get("name").toString());
                                        ArrayList<String> ss1 = new ArrayList<>();
                                        for (int k = 0; k < aaa; k++) {
                                            JSONObject json1111 = new JSONObject(json111.get("modelobject_" + (k)).toString());
                                            System.out.println("json1111:" + json1111);
                                            ss1.add(json1111.get("name").toString());
                                            littleCategory.put(json1111.get("name").toString(), json1111.get("id").toString());
                                        }
                                        categoryMap1.put(json111.get("name").toString(), ss1);
                                        System.out.println("json111:" + json111);
                                    }
                                    categoryMap.put(json11.get("name").toString(), ss);
                                    System.out.println("json11:" + json11);
                                }
                                System.out.println("++++++++++size++++++++++++++" + categoryMap.size() + category.size()
                                        + categoryMap.get(category.get(0)) + category + category1 + category1.get(0) + categoryMap1.get(category1.get(0))
                                );
                            } catch (Exception e) {
                                // TODO: handle exception
                                System.out.println("++++++++++Exception++++++++++++++");
                                ToastUtil.showToast(CategoryActivity.this, "服务器异常");
                                e.printStackTrace();
                            }
                            mVals = new String[category.size()];
                            for (int i = 0; i < category.size(); i++) {
                                mVals[i] = category.get(i);
                                System.out.println("mvals[" + i + "]" + mVals[i]);
                            }
                            final LayoutInflater mInflater = LayoutInflater.from(getBaseContext());
                            title_back = (ImageView) findViewById(R.id.title_back_imageButton);
                            title_back.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    onBackPressed();
                                }
                            });
                            tffl = (TagFlowLayout) findViewById(R.id.id_flowlayout);
                            tffl1 = (TagFlowLayout) findViewById(R.id.id_flowlayout1);
                            tffl2 = (TagFlowLayout) findViewById(R.id.id_flowlayout11);
                            tv = (TextView) findViewById(R.id.category_tv);
                            tv1 = (TextView) findViewById(R.id.category_tv1);
                            tffl.setAdapter(new TagAdapter<String>(mVals) {
                                @Override
                                public View getView(FlowLayout parent, int position, String s) {
                                    TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                            tffl, false);
                                    tv.setText(s);
                                    return tv;
                                }
                                @Override
                                public boolean setSelected(int position, String s) {
                                    return s.equals("Android");
                                }
                            });
                            tffl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    String l = category.get(position);
                                    s = new String[(categoryMap.get(l)).size()];
                                    for (int i = 0; i < (categoryMap.get(l)).size(); i++) {
                                        s[i] = (categoryMap.get(l)).get(i);
                                        // s[i]= ll;
                                    }
                                    tv.setVisibility(View.VISIBLE);
                                    tv.setText(((TextView) view).getText() + ":");
                                    tffl1.setAdapter(new TagAdapter<String>(s) {
                                        @Override
                                        public View getView(FlowLayout parent, int position, String s) {
                                            TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                                    tffl1, false);
                                            tv.setText(s);
                                            tffl2.setVisibility(View.GONE);
                                            tv1.setVisibility(View.GONE);
                                            return tv;
                                        }
                                        @Override
                                        public boolean setSelected(int position, String s) {
                                            return s.equals("全部");
                                        }
                                    });
                                    return true;
                                }
                            });
                            tffl1.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    tffl2.setVisibility(View.VISIBLE);
                                    String tvs = ((TextView) view).getText() + "";
                                    intent.putExtra("middleID", midCatecoryMap.get(tvs).getModleid());
                                    intent.putExtra("middleUrl", midCatecoryMap.get(tvs).getPhotourl());
                                    tv1.setVisibility(View.VISIBLE);
                                    tv1.setText(midCatecoryMap.get(tvs).getModelobjectdesc());
                                    intent.putExtra("largeID", midCatecoryMap.get(tvs).getModeltype());
                                    intent.putExtra("category", ((TextView) view).getText());
                                    intent.setClass(CategoryActivity.this, MainActivity.class);
                                    System.out.println("++++++++++++++++++++++++++++++++" + ((TextView) view).getText()
                                            + midCatecoryMap.get(tvs).getModleid());
                                    ss = new String[categoryMap1.get(tvs).size()];
                                    if (categoryMap1.get(tvs).size() == 0) {
                                        startActivity(intent);
                                    }
                                    for (int i = 0; i < categoryMap1.get(tvs).size(); i++) {
                                        ss[i] = categoryMap1.get(tvs).get(i);
                                    }
                                    tffl2.setAdapter(new TagAdapter<String>(ss) {
                                        @Override
                                        public View getView(FlowLayout parent, int position, String s) {
                                            TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                                    tffl2, false);
                                            tv.setText(s);
                                            return tv;
                                        }
                                        @Override
                                        public boolean setSelected(int position, String s) {
                                            return s.equals("全部");
                                        }
                                    });
                                    return true;
                                }
                            });
                            tffl2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                              @Override
                               public boolean onTagClick(View view, int position, FlowLayout parent) {
                                 tv1.setText(((TextView) view).getText());
                                  intent.putExtra("littleID", littleCategory.get(((TextView) view).getText()));
                                   intent.putExtra("littleNumber", ((TextView) view).getText());
                                   startActivity(intent);
                                   return true;
                                     }
                                      }
                            );
                            tffl.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                                @Override
                                public void onSelected(Set<Integer> selectPosSet) {
                                }
                            });
                        }
                        @Override
                        public void onError(Call arg0, Exception arg1) {
                            System.out.println("Error:" + arg1);
                            Constant.DARKREFID = null;
                        }
                    });
        } catch (Exception e) {
            ToastUtil.showToast(CategoryActivity.this, "服务器异常");
            e.printStackTrace();
        }
    }
    private void initWindow() {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);// calculateStatusColor(Color.WHITE,
            // (int) alphaValue)
        }
    }
    public void getModelList(String mOpenId, String sessionid)
            throws JSONException {
    }

}

package com.magispec.shield.widgets;

import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ShareDialog extends Dialog implements android.view.View.OnClickListener {
	private EditText wx_share_position, wx_share_price;
	private ImageView wx_frieds,wx_moments,wx_no;

	public ShareDialog(Context context, int theme) {
		super(context, theme);

	}

	// 定义接口 定义onclick 事件
	public interface OnWindowItemClickListener {
		void onClickToFriends();

		void onClickToMoments();
		void onClickToDismiss();
	}

	private OnWindowItemClickListener listener;

	public void setOnWindowItemClickListener(OnWindowItemClickListener listener) {
		this.listener = listener;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_popupwindow);
		wx_share_position = (EditText) findViewById(R.id.et_wx_share_position);
		wx_share_price = (EditText) findViewById(R.id.et_wx_share_price);
		wx_frieds=(ImageView) findViewById(R.id.iv_share_wx1);
		wx_moments=(ImageView) findViewById(R.id.iv_share_wx2);
		wx_no=(ImageView) findViewById(R.id.iv_wx_share_no);
		wx_frieds.setOnClickListener(this);
		wx_moments.setOnClickListener(this);
		wx_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		 switch (v.getId()) {
         case R.id.iv_share_wx1:
             if(listener!=null){
            	 Constant.bypc=wx_share_price.getText().toString();
            	 Constant.byps=wx_share_position.getText().toString();
                 listener.onClickToFriends();
             }
             this.cancel();
             break;
         case R.id.iv_share_wx2:
             if(listener!=null){
            	 Constant.bypc=wx_share_price.getText().toString();
            	 Constant.byps=wx_share_position.getText().toString();
                 listener.onClickToMoments();
             }
             this.cancel();
         case R.id.iv_wx_share_no:
             if(listener!=null){
                 listener.onClickToDismiss();
             }
             this.cancel();
             break;
     }
		
	}
	
	
	
	
	

}

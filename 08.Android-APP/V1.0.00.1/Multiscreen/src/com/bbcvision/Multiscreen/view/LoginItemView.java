package com.bbcvision.Multiscreen.view;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbcvision.Multiscreen.R;

public class LoginItemView extends RelativeLayout {

	private EditText et_login;
	private TextView tv_login;

	public LoginItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}


	public LoginItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		
		String desc = attrs.getAttributeValue("http://schemas.android.com/java/com.bbcvision.Multiscreen", "desc");
		String input = attrs.getAttributeValue("http://schemas.android.com/java/com.bbcvision.Multiscreen ", "input");
		String isPwd = attrs.getAttributeValue("http://schemas.android.com/java/com.bbcvision.Multiscreen ", "pwd");
		setText(input);
		setDescText(desc);
		if(isPwd != null){
			isPassword(true);
		}
	}

	
	public LoginItemView(Context context) {
		super(context);
		init(context);
	}

	public void isPassword(Boolean paw){
		if(paw){
			et_login.setTransformationMethod(PasswordTransformationMethod.getInstance());
		}
	}
	
	private void init(Context context) {
		View.inflate(context, R.layout.item_login, this);
		et_login = (EditText) findViewById(R.id.et_login);
		tv_login = (TextView) findViewById(R.id.tv_desc);
	}
	
	public void setText(String string){
		et_login.setText(string);
	}
	
	public String getText(){
		return et_login.getText().toString().trim();
	}
	
	public void setDescText(String string){
		tv_login.setText(string);
	}
	
	public String getDescText(){
		return tv_login.getText().toString().trim();
	}
	
	public void setmBackground(int id){
		et_login.setBackgroundResource(id);
	}
}

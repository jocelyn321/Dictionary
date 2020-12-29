package zyf.dict.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zyf.dict.domain.WordItem;

public class ShowItemwordActivity extends Activity {


	private TextView name_tv,mean_tv,show_tv;
	private ImageView img_bt,pre_bt,next_bt;
	Bundle extras;
	int key = 0;
	private ArrayList<WordItem> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selected_item_show);
		initComponent();
	}
	private void initComponent(){
		//通过findViewById实例化组件
		name_tv = (TextView)findViewById(R.id.show_word_tv);
	    mean_tv = (TextView)findViewById(R.id.show_mean_tv);
	    show_tv = (TextView)findViewById(R.id.show);
		img_bt = (ImageView)findViewById(R.id.show_detail_btn);
		pre_bt = (ImageView)findViewById(R.id.pre_btn);
		next_bt = (ImageView)findViewById(R.id.next_btn);
		//获取Intent绑定的参数
		extras = getIntent().getExtras(); 
		Bundle mBundle = (Bundle)extras.get("list");
		key = Integer.parseInt(extras.getString("key"));
		list = (ArrayList<WordItem>) mBundle.get("worditems");
		name_tv.setText(list.get(key).getWord_name());
		//添加事件监听器
		img_bt.setOnClickListener(showMean);
		pre_bt.setOnClickListener(preword);
		next_bt.setOnClickListener(nextword);
	}
	
	private OnClickListener showMean = new OnClickListener() {
		@Override
		public void onClick(View v) {
			img_bt.setVisibility(View.INVISIBLE);
			show_tv.setVisibility(View.VISIBLE);
			mean_tv.setVisibility(View.VISIBLE);
			name_tv.setText(list.get(key).getWord_name());
			mean_tv.setText(list.get(key).getWord_mean());
			mean_tv.setTextSize(20f);
		}
	};
	private OnClickListener preword = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			key = key-1;
			img_bt.setVisibility(View.VISIBLE);
			show_tv.setVisibility(View.INVISIBLE);
			mean_tv.setVisibility(View.INVISIBLE);
			if(key==0){
				name_tv.setText(list.get(key).getWord_name());
				mean_tv.setText(list.get(key).getWord_mean());
			}
			else if(key<0){
				name_tv.setText(list.get(0).getWord_name());
				mean_tv.setText(list.get(0).getWord_mean());
				Toast.makeText(ShowItemwordActivity.this, "这是第一个单词", Toast.LENGTH_SHORT).show();
				key = key+1;
			}
			else{
				name_tv.setText(list.get(key).getWord_name());
				mean_tv.setText(list.get(key).getWord_mean());
			}
		    img_bt.setOnClickListener(showMean);
			mean_tv.setTextSize(20f);
		}
	};
	private OnClickListener nextword = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			key = key+1;
			img_bt.setVisibility(View.VISIBLE);
			show_tv.setVisibility(View.INVISIBLE);
			mean_tv.setVisibility(View.INVISIBLE);
			if(key==list.size()-1){
				name_tv.setText(list.get(key).getWord_name());
				mean_tv.setText(list.get(key).getWord_mean());
			}
			else if(key>list.size()-1){
				key = key-1;
				Toast.makeText(ShowItemwordActivity.this, "这是最后一个单词", Toast.LENGTH_SHORT).show();
			}
			else{
				name_tv.setText(list.get(key).getWord_name());
			    mean_tv.setText(list.get(key).getWord_mean());
			}
			mean_tv.setTextSize(20f);
			img_bt.setOnClickListener(showMean);
		}
	};
}

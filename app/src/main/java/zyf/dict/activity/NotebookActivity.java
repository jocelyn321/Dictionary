package zyf.dict.activity;

import java.util.ArrayList;
import java.util.List;
import zyf.dict.adapter.WordItemAdapter;
import zyf.dict.domain.WordItem;
import zyf.dict.service.WordService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class NotebookActivity extends Activity {
	static ListView listView;
	private TextView textview;
	private WordService wordService;
	static List<WordItem> list;
	AlertDialog.Builder builder;

	MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notebook);
		myApp = MyApplication.getInstance(); // 获得自定义的应用程序MyApp
		initComponent();
	}

	private void initComponent(){
		//实例化组件
		textview = (TextView) findViewById(R.id.text_tv);
		listView = (ListView)findViewById(R.id.list);

		wordService = new WordService(this);
		list = wordService.getword(String.valueOf(myApp.getUser().get_id()));
		wordService.close();
		//初始化WordItemAdapter适配器
		WordItemAdapter adapter = new WordItemAdapter(list, this);
		listView.setAdapter(adapter);
		//提示对话框
		builder = new AlertDialog.Builder(NotebookActivity.this);
		//设置ListView的Item的长按事件监听
		listView.setOnItemLongClickListener(itemlongclick);
		//设置ListView的Item的单击事件监听
		listView.setOnItemClickListener(itemclick);
	}
	//ListView的Item的长按事件处理
	private OnItemLongClickListener itemlongclick = new OnItemLongClickListener(){
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
			// TODO Auto-generated method stub
			final int temp = arg2;
			builder.setMessage("是否确定删除").setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					wordService = new WordService(NotebookActivity.this);
					wordService.delete(list.get(temp));
					wordService.close();
					//重新查询
					wordService = new WordService(NotebookActivity.this);
					List<WordItem> list = wordService.getword(String.valueOf(myApp.getUser().get_id()));
					wordService.close();
					WordItemAdapter adapter = new WordItemAdapter(list, NotebookActivity.this);
					listView.setAdapter(adapter);
					SelectActivity.imgbt.setImageResource(R.drawable.add_note);
				}
			}).setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			//创建提示对话框
			AlertDialog ag = builder.create();
			//显示提示对话框
			ag.show();
			return true;
		}
	};
	//ListView的Item的单击事件处理
	private OnItemClickListener itemclick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent intent = new Intent(NotebookActivity.this,ShowItemwordActivity.class);
			Bundle mBundle = new Bundle();
			ArrayList<WordItem> mlist = new ArrayList<WordItem>();
			for(WordItem item:list){
				mlist.add(item);
			}
			mBundle.putParcelableArrayList("worditems", mlist);
			intent.putExtra("list", mBundle);
			intent.putExtra("key", arg2+"");
			intent.setComponent(new ComponentName(NotebookActivity.this,ShowItemwordActivity.class));
			startActivity(intent);
		}
	};

}

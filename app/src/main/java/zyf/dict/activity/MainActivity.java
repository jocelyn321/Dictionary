package zyf.dict.activity;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import java.util.List;

import zyf.dict.adapter.WordItemAdapter;
import zyf.dict.domain.WordItem;
import zyf.dict.service.WordService;

public class MainActivity extends TabActivity {

	MyApplication myApp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost th = getTabHost();   //TabHost就像一个容器，里面可以存放多个Tab
		myApp = MyApplication.getInstance(); // 获得自定义的应用程序MyApp
		LayoutInflater.from(this).inflate(R.layout.dict, th.getTabContentView(),true);
		//用于将tab添加到tabHost
		th.addTab(th.newTabSpec("dictionary")
				.setIndicator("词典",getResources().getDrawable(R.drawable.dictab))
				.setContent(new Intent(MainActivity.this,SelectActivity.class)));
		th.addTab(th.newTabSpec("translate")
				.setIndicator("翻译",getResources().getDrawable(R.drawable.transtab))
				.setContent(new Intent(MainActivity.this,TranslateActivity.class)));
		th.addTab(th.newTabSpec("notebook")
				.setIndicator("生词本",getResources().getDrawable(R.drawable.notetab))
				.setContent(new Intent(MainActivity.this,NotebookActivity.class)));
		th.addTab(th.newTabSpec("lishi")
				.setIndicator("搜索历史",getResources().getDrawable(R.drawable.notetab))
				.setContent(new Intent(MainActivity.this,LishiActivity.class)));
		//添加标签切换事件监听
		th.setOnTabChangedListener(changeTab);
	}
	//标签切换事件处理
	private OnTabChangeListener changeTab = new OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {
			if(tabId.equals("notebook")){
				//SelectActivity.dbManager.closeDatabase();
				WordService wordService = new WordService(MainActivity.this);
				List<WordItem> list = wordService.getword(String.valueOf(myApp.getUser().get_id()));
				wordService.close();
				//初始化WordItemAdapter适配器
				WordItemAdapter adapter = new WordItemAdapter(list, MainActivity.this);
				NotebookActivity.listView.setAdapter(adapter);
			}
		}
	};
}
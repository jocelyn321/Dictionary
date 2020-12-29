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
		TabHost th = getTabHost();   //TabHost����һ��������������Դ�Ŷ��Tab
		myApp = MyApplication.getInstance(); // ����Զ����Ӧ�ó���MyApp
		LayoutInflater.from(this).inflate(R.layout.dict, th.getTabContentView(),true);
		//���ڽ�tab��ӵ�tabHost
		th.addTab(th.newTabSpec("dictionary")
				.setIndicator("�ʵ�",getResources().getDrawable(R.drawable.dictab))
				.setContent(new Intent(MainActivity.this,SelectActivity.class)));
		th.addTab(th.newTabSpec("translate")
				.setIndicator("����",getResources().getDrawable(R.drawable.transtab))
				.setContent(new Intent(MainActivity.this,TranslateActivity.class)));
		th.addTab(th.newTabSpec("notebook")
				.setIndicator("���ʱ�",getResources().getDrawable(R.drawable.notetab))
				.setContent(new Intent(MainActivity.this,NotebookActivity.class)));
		th.addTab(th.newTabSpec("lishi")
				.setIndicator("������ʷ",getResources().getDrawable(R.drawable.notetab))
				.setContent(new Intent(MainActivity.this,LishiActivity.class)));
		//��ӱ�ǩ�л��¼�����
		th.setOnTabChangedListener(changeTab);
	}
	//��ǩ�л��¼�����
	private OnTabChangeListener changeTab = new OnTabChangeListener() {
		@Override
		public void onTabChanged(String tabId) {
			if(tabId.equals("notebook")){
				//SelectActivity.dbManager.closeDatabase();
				WordService wordService = new WordService(MainActivity.this);
				List<WordItem> list = wordService.getword(String.valueOf(myApp.getUser().get_id()));
				wordService.close();
				//��ʼ��WordItemAdapter������
				WordItemAdapter adapter = new WordItemAdapter(list, MainActivity.this);
				NotebookActivity.listView.setAdapter(adapter);
			}
		}
	};
}
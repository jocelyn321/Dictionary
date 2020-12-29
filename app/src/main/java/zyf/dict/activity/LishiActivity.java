package zyf.dict.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zyf.dict.adapter.LishiItemAdapter;
import zyf.dict.domain.Lishi;
import zyf.dict.service.LishiService;

public class LishiActivity extends Activity {
    static ListView listView;
    private TextView textview;
    private LishiService lishiService;
    static List<Lishi> list;
    AlertDialog.Builder builder;
    static LishiItemAdapter adapter;
    MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lishi);
        myApp = MyApplication.getInstance(); // 获得自定义的应用程序MyApp
        initComponent();
    }

    private void initComponent() {
        //实例化组件
        textview = (TextView) findViewById(R.id.text_tv);
        listView = (ListView) findViewById(R.id.list);


        lishiService = new LishiService(this);

        list=new ArrayList<Lishi>();
        list = lishiService.getword(String.valueOf(myApp.getUser().get_id()));
        adapter = new LishiItemAdapter(list, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lishiService.close();
        //初始化WordItemAdapter适配器

        //提示对话框
        builder = new AlertDialog.Builder(LishiActivity.this);
        //设置ListView的Item的长按事件监听
        listView.setOnItemLongClickListener(itemlongclick);

    }

    //ListView的Item的长按事件处理
    private OnItemLongClickListener itemlongclick = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // TODO Auto-generated method stub
            final int temp = arg2;
            builder.setMessage("是否确定删除").setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lishiService = new LishiService(LishiActivity.this);
                    lishiService.delete(list.get(temp));
                    lishiService.close();
                    //重新查询
                    lishiService = new LishiService(LishiActivity.this);
                    List<Lishi> list = lishiService.getword(String.valueOf(myApp.getUser().get_id()));
                    lishiService.close();
                    LishiItemAdapter adapter = new LishiItemAdapter(list, LishiActivity.this);
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


}

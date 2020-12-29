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
        myApp = MyApplication.getInstance(); // ����Զ����Ӧ�ó���MyApp
        initComponent();
    }

    private void initComponent() {
        //ʵ�������
        textview = (TextView) findViewById(R.id.text_tv);
        listView = (ListView) findViewById(R.id.list);


        lishiService = new LishiService(this);

        list=new ArrayList<Lishi>();
        list = lishiService.getword(String.valueOf(myApp.getUser().get_id()));
        adapter = new LishiItemAdapter(list, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lishiService.close();
        //��ʼ��WordItemAdapter������

        //��ʾ�Ի���
        builder = new AlertDialog.Builder(LishiActivity.this);
        //����ListView��Item�ĳ����¼�����
        listView.setOnItemLongClickListener(itemlongclick);

    }

    //ListView��Item�ĳ����¼�����
    private OnItemLongClickListener itemlongclick = new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            // TODO Auto-generated method stub
            final int temp = arg2;
            builder.setMessage("�Ƿ�ȷ��ɾ��").setPositiveButton("��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lishiService = new LishiService(LishiActivity.this);
                    lishiService.delete(list.get(temp));
                    lishiService.close();
                    //���²�ѯ
                    lishiService = new LishiService(LishiActivity.this);
                    List<Lishi> list = lishiService.getword(String.valueOf(myApp.getUser().get_id()));
                    lishiService.close();
                    LishiItemAdapter adapter = new LishiItemAdapter(list, LishiActivity.this);
                    listView.setAdapter(adapter);
                    SelectActivity.imgbt.setImageResource(R.drawable.add_note);
                }
            }).setNegativeButton("��", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            //������ʾ�Ի���
            AlertDialog ag = builder.create();
            //��ʾ��ʾ�Ի���
            ag.show();
            return true;
        }
    };


}

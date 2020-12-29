package zyf.dict.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import zyf.dict.adapter.DictionaryAdapter;
import zyf.dict.domain.Lishi;
import zyf.dict.domain.WordItem;
import zyf.dict.service.DBManager;
import zyf.dict.service.LishiService;
import zyf.dict.service.WordService;
import zyf.dict.tools.LanguageAnalysisTools;

public class SelectActivity extends Activity implements OnClickListener, TextWatcher {
    private AutoCompleteTextView selectWord;
    private ImageButton btn;
    static ImageView imgbt;
    static DBManager dbManager;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    static Cursor cur;
    MyApplication myApp;

    LishiService sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���õ�ǰ��ͼ
        setContentView(R.layout.selectword);
        myApp = MyApplication.getInstance(); //
        sc = new LishiService(SelectActivity.this);
        initComponent();
    }

    private void initComponent() {
        //ͨ��FindViewByIdʵ�������
        selectWord = (AutoCompleteTextView) findViewById(R.id.input_act);
        btn = (ImageButton) findViewById(R.id.select_btn);
        //ʵ�����������ѯ������
        tv1 = (TextView) findViewById(R.id.select_word);
        //��ʾ��ѯ������������ݵ���˼
        tv2 = (TextView) findViewById(R.id.select_word_mean);
        tv3 = (TextView) findViewById(R.id.show);
        imgbt = (ImageView) findViewById(R.id.add_note);
        //����Զ�����ı���ļ����¼�
        selectWord.addTextChangedListener(this);
        selectWord.setThreshold(1); //���Ĵ�����ʾ���ַ����ȣ�dafault=2
        //��Ӳ�ѯ��ť�ļ����¼�
        btn.setOnClickListener(this);
        imgbt.setOnClickListener(this);
    }


    @Override
    public void afterTextChanged(Editable s) {
        dbManager = new DBManager(this);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        imgbt.setVisibility(View.INVISIBLE);
        if (s.length() > 0) {
            int language = LanguageAnalysisTools.getLanguage(s.toString());
            if (language == 0) {
                String sql = "select chinese as _id from t_words where chinese like ?";
                cur = dbManager.fuzzySelect(sql, new String[]{s.toString() + "%"});
                if (cur.getCount() > 0) {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            cur, true);
                    selectWord.setAdapter(dictionaryAdapter);
                } else {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            null, true);
                    selectWord.setAdapter(dictionaryAdapter);
                }
            } else if (language == 1) {
                //���뽫english�ֶεı�����Ϊ_id
                String sql = "select english as _id from t_words where english like ?";
                cur = dbManager.fuzzySelect(sql, new String[]{s.toString() + "%"});
                if (cur.getCount() > 0) {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            cur, true);
                    selectWord.setAdapter(dictionaryAdapter);
                } else {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            null, true);
                    selectWord.setAdapter(dictionaryAdapter);
                }
            }
        } else {
            DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                    null, true);
            selectWord.setAdapter(dictionaryAdapter);
        }
        dbManager.closeDatabase();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
    }

    //�����¼�
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_note:
                note();
                break;
            case R.id.select_btn:
                search();
                break;
            default:
                break;
        }

    }

    //��ѯ����
    public void search() {
        cur.close();
        //��ʼ��DBManager
        dbManager = new DBManager(this);
        if (!selectWord.getText().toString().equals("")) {
            //��ȡ��ѯ������
            String selword = selectWord.getText().toString();
            //��ȡ��ѯ���ݵ�����
            String sql = null;
            String result = "δ�ҵ��õ���.";
            if (selword != null) {
            /*����������ʷ*/
                if(!sc.exist(selword,String.valueOf(myApp.getUser().get_id())))
                {
                    Lishi scobj=new Lishi();
                    scobj.setUserid(String.valueOf(myApp.getUser().get_id()));
                    scobj.setWord_name(selword);
                    sc.save(scobj);
                    sc = new LishiService(SelectActivity.this);

                    LishiActivity.list=sc.getword(String.valueOf(myApp.getUser().get_id()));
                    sc.close();
                }

                int language = LanguageAnalysisTools.getLanguage(selword);
                if (language == 0) {
                    sql = "select english from t_words where chinese like ?";
                    Cursor cursor = dbManager.fuzzySelect(sql, new String[]{"%" + selword + "%"});
                    //������ҵ����ʣ���ʾ��Ӣ�ĵ���˼
                    if (cursor.getCount() > 0) {
                        //����ʹ��moveToFirst��������¼ָ���ƶ�����һ����¼��λ��
                        cursor.moveToFirst();
                        result = cursor.getString(cursor.getColumnIndex("english"));
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        imgbt.setVisibility(View.VISIBLE);
                        //����TextView������
                        tv1.setText(selectWord.getText());
                        tv2.setText(result);
                        //��ʼ��WordItem
                        WordItem w = new WordItem(tv1.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));
                        //��ʼ��WordService
                        WordService ws = new WordService(SelectActivity.this);
                        //����������ʰ�ť�ɼ�

                        if (!ws.isExist(w)) {
                            imgbt.setImageResource(R.drawable.add_note);
                        } else {
                            imgbt.setImageResource(R.drawable.del_note);
                        }
                        ws.close();
                    } else {
                        DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                                null, true);
                        selectWord.setAdapter(dictionaryAdapter);
                        Toast.makeText(SelectActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                } else if (language == 1) {
                    sql = "select chinese from t_words where english = ?";
                    Cursor cursor = dbManager.select(sql, new String[]{selword});
                    //
                    if (cursor.getCount() > 0) {
                        //����ʹ��MoveToFirst��������¼ָ���ƶ�����һ����¼��λ��
                        cursor.moveToFirst();
                        result = cursor.getString(cursor.getColumnIndex("chinese"));
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        imgbt.setVisibility(View.VISIBLE);
                        tv2.setText(result);
                        //����TextView������
                        tv1.setText(selectWord.getText());
                    } else {
                        DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                                null, true);
                        selectWord.setAdapter(dictionaryAdapter);
                        Toast.makeText(SelectActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();
                }
            }
        } else {
            DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                    null, true);
            selectWord.setAdapter(dictionaryAdapter);
            Toast.makeText(SelectActivity.this, "�����뵥�ʻ����", Toast.LENGTH_SHORT).show();
        }
        dbManager.closeDatabase();
    }

    //��� ɾ������
    public void note() {
        //��ʼ��WordItem
        WordItem w = new WordItem(tv1.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));
        //��ʼ��WordService
        WordService ws = new WordService(SelectActivity.this);
        //�жϸõ����Ƿ��Ѵ���notebook��
        if (!ws.isExist(w)) {
            //�������������WordService�е�save�������������
            ws.save(w);
            ws.close();
            Toast.makeText(SelectActivity.this, "��������ʱ��ɹ�!", Toast.LENGTH_SHORT).show();
            ws = new WordService(SelectActivity.this);
            NotebookActivity.list = ws.getword(String.valueOf(myApp.getUser().get_id()));
            ws.close();
            imgbt.setImageResource(R.drawable.del_note);
        } else {
            ws = new WordService(SelectActivity.this);
            //�������������WordService�е�save�������������
            ws.delete(w);
            ws.close();
            ws = new WordService(SelectActivity.this);
            Toast.makeText(SelectActivity.this, "�����ʱ�ɾ���ɹ�!", Toast.LENGTH_SHORT).show();
            NotebookActivity.list = ws.getword(String.valueOf(myApp.getUser().get_id()));
            ws.close();
            imgbt.setImageResource(R.drawable.add_note);
        }
    }
}

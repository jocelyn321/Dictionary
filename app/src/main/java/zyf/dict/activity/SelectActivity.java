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
        //设置当前视图
        setContentView(R.layout.selectword);
        myApp = MyApplication.getInstance(); //
        sc = new LishiService(SelectActivity.this);
        initComponent();
    }

    private void initComponent() {
        //通过FindViewById实例化组件
        selectWord = (AutoCompleteTextView) findViewById(R.id.input_act);
        btn = (ImageButton) findViewById(R.id.select_btn);
        //实例化组件，查询的内容
        tv1 = (TextView) findViewById(R.id.select_word);
        //显示查询结果，输入内容的意思
        tv2 = (TextView) findViewById(R.id.select_word_mean);
        tv3 = (TextView) findViewById(R.id.show);
        imgbt = (ImageView) findViewById(R.id.add_note);
        //添加自动完成文本框的监听事件
        selectWord.addTextChangedListener(this);
        selectWord.setThreshold(1); //更改触发提示的字符长度，dafault=2
        //添加查询按钮的监听事件
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
                //必须将english字段的别名设为_id
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

    //监听事件
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

    //查询单词
    public void search() {
        cur.close();
        //初始化DBManager
        dbManager = new DBManager(this);
        if (!selectWord.getText().toString().equals("")) {
            //获取查询的内容
            String selword = selectWord.getText().toString();
            //获取查询内容的类型
            String sql = null;
            String result = "未找到该单词.";
            if (selword != null) {
            /*加入搜索历史*/
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
                    //如果查找到单词，显示其英文的意思
                    if (cursor.getCount() > 0) {
                        //必须使用moveToFirst方法将记录指针移动到第一条记录的位置
                        cursor.moveToFirst();
                        result = cursor.getString(cursor.getColumnIndex("english"));
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        imgbt.setVisibility(View.VISIBLE);
                        //设置TextView的内容
                        tv1.setText(selectWord.getText());
                        tv2.setText(result);
                        //初始化WordItem
                        WordItem w = new WordItem(tv1.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));
                        //初始化WordService
                        WordService ws = new WordService(SelectActivity.this);
                        //设置添加生词按钮可见

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
                        //必须使用MoveToFirst方法将记录指针移动到第一条记录的位置
                        cursor.moveToFirst();
                        result = cursor.getString(cursor.getColumnIndex("chinese"));
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        imgbt.setVisibility(View.VISIBLE);
                        tv2.setText(result);
                        //设置TextView的内容
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
            Toast.makeText(SelectActivity.this, "请输入单词或词语", Toast.LENGTH_SHORT).show();
        }
        dbManager.closeDatabase();
    }

    //添加 删除单词
    public void note() {
        //初始化WordItem
        WordItem w = new WordItem(tv1.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));
        //初始化WordService
        WordService ws = new WordService(SelectActivity.this);
        //判断该单词是否已存在notebook中
        if (!ws.isExist(w)) {
            //若不存在则调用WordService中的save方法添加生单词
            ws.save(w);
            ws.close();
            Toast.makeText(SelectActivity.this, "添加至生词本成功!", Toast.LENGTH_SHORT).show();
            ws = new WordService(SelectActivity.this);
            NotebookActivity.list = ws.getword(String.valueOf(myApp.getUser().get_id()));
            ws.close();
            imgbt.setImageResource(R.drawable.del_note);
        } else {
            ws = new WordService(SelectActivity.this);
            //若不存在则调用WordService中的save方法添加生单词
            ws.delete(w);
            ws.close();
            ws = new WordService(SelectActivity.this);
            Toast.makeText(SelectActivity.this, "从生词本删除成功!", Toast.LENGTH_SHORT).show();
            NotebookActivity.list = ws.getword(String.valueOf(myApp.getUser().get_id()));
            ws.close();
            imgbt.setImageResource(R.drawable.add_note);
        }
    }
}

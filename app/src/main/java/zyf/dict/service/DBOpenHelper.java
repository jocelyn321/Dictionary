package zyf.dict.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "note.db"; //���ݿ�����
	private static final int DATABASEVERSION = 1;//���ݿ�汾

	public DBOpenHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE notebook (_id integer primary key autoincrement, word_name varchar(40), word_mean varchar(100),word_time INTEGER,user_id varchar(100))");//ִ���и��ĵ�sql���

		db.execSQL("CREATE TABLE user (_id integer primary key autoincrement, loginname varchar(100), loginpsw varchar(100), tel varchar(100),email varchar(100))");//ִ���и��ĵ�sql���

		db.execSQL("CREATE TABLE lishi (_id integer primary key autoincrement, word_name varchar(100), userid varchar(100))");//ִ���и��ĵ�sql���
	}
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS notebook");
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS lishi");
		onCreate(db);
	}

}

package zyf.dict.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zyf.dict.domain.Lishi;


public class LishiService {
	private DBOpenHelper dbOpenHelper;
    public LishiService(Context context) {
    	this.dbOpenHelper = new DBOpenHelper(context);
	}
	public void save(Lishi sc){
		
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into lishi (word_name,userid) values(?,?)",
				new Object[]{sc.getWord_name(),sc.getUserid()});
		db.close();
	}
	
	public void delete(Lishi sc){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from lishi where userid=? and word_name=?", new String[]{String.valueOf(sc.getUserid()),sc.getWord_name()});
		db.close();
	}
	/**
	 * 判断单词是否存在
	 * @param sc
	 * @return
	 */
	public boolean isExist(Lishi sc){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from lishi where  userid=? and word_name = ?", new String[]{String.valueOf(sc.getUserid()),sc.getWord_name()});
		boolean result = cursor.getCount() > 0? true : false ;
		cursor.close();
		db.close();
		return result ;
	}
	public boolean exist(String wordname,String userid){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from lishi where userid=? and  word_name = ?", new String[]{userid,wordname});
		boolean result = cursor.getCount() > 0? true : false ;
		cursor.close();
		db.close();
		return result;
		
	}

	public List<Lishi> getword(String userid){
		List<Lishi> word_items = new ArrayList<Lishi>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("lishi", null, "userid="+userid,null,  null, null, null);
		while(cursor.moveToNext()){
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String word_name = cursor.getString(cursor.getColumnIndex("word_name"));

			String user_id = cursor.getString(cursor.getColumnIndex("userid"));
			//System.out.println(word_name);
			Lishi ls=new Lishi();
			ls.setWord_name(word_name);
			ls.setUserid(userid);
			ls.set_id(_id);


			word_items.add(ls);
		}
		cursor.close();
		db.close();
		return word_items;
	}
	public void close(){
		dbOpenHelper.close();
	}

}

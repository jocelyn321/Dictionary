package zyf.dict.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zyf.dict.domain.Lishi;

public class LishiItemAdapter extends BaseAdapter {





	private List<Lishi> list = null;
	Context context = null;
	public LishiItemAdapter(List<Lishi> list , Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	public int getCount() {

		return list.size();
	}

	public Object getItem(int position) {
		
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout item_layout = new LinearLayout(context);

		Lishi wordItem = (Lishi) this.getItem(position);
		TextView word_name_TV = new TextView(context);
		word_name_TV.setText(wordItem.getWord_name());
		word_name_TV.setTextSize(20f);
		word_name_TV.setTextColor(Color.BLACK);
		word_name_TV.setMaxLines(1);

		item_layout.addView(word_name_TV);
		return item_layout;
	}

}

package com.example.news.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.example.news.entity.NewEntity;
import com.example.silencedance.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewAdapter extends ArrayAdapter<NewEntity> {    // 适配器，泛型表示想要适配的数据类型

	private int resourceId;
	private Context con;
	private List<NewEntity> newLists;

	public NewAdapter(Context context, int textViewResourceId,
			List<NewEntity> objects) {                         // 第一个参数是上下文环境，第二个参数是每一项的子布局，第三个参数是数据
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;                   //获取子布局
		this.con=context;
		this.newLists=objects;
	}

	@Override         //getView方法在每个子项被滚动到屏幕内的时候都会被调用，每次都将布局重新加载一边
	public View getView(int position, View convertView, ViewGroup parent) {//第一个参数表示位置，第二个参数表示缓存布局，第三个表示绑定的view对象
		View view;
		ViewHolder viewHolder;                  //实例ViewHolder，当程序第一次运行，保存获取到的控件，提高效率
		if(convertView==null){  
			viewHolder=new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(//convertView为空代表布局没有被加载过，即getView方法没有被调用过，需要创建
					resourceId, null);          // 得到子布局，非固定的，和子布局id有关
			viewHolder.ivItemPicture = (ImageView) view.findViewById(R.id.ivItemPicture);//获取控件,只需要调用一遍，调用过后保存在ViewHolder中
			viewHolder.tvItemTitle = (TextView) view.findViewById(R.id.tvItemTitle);   //获取控件
			view.setTag(viewHolder);
		}else{
			view=convertView;           //convertView不为空代表布局被加载过，只需要将convertView的值取出即可
			viewHolder=(ViewHolder) view.getTag();
		}
		
		NewEntity newOne = getItem(position);//实例指定位置的水果
		
		//viewHolder.ivItemPicture.setImageResource(R.drawable.);
		//viewHolder.ivItemPicture.setImageBitmap(BitmapFactory.decodeFile(newOne.getImg()));
		Glide.with(con).load(newLists.get(position).getImg()).into(viewHolder.ivItemPicture);
		viewHolder.tvItemTitle.setText(newOne.getTitle());      
		return view;

	}
} 
class ViewHolder{      //当布局加载过后，保存获取到的控件信息。
	ImageView ivItemPicture;
	TextView tvItemTitle;
}

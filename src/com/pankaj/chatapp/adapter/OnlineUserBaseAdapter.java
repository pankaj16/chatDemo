package com.pankaj.chatapp.adapter;

import java.util.ArrayList;

import com.pankaj.chatapp.R;
import com.pankaj.chatapp.adapter.MessageListAdapter.ViewHolder;
import com.pankaj.chatapp.entities.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OnlineUserBaseAdapter extends BaseAdapter{
	
	Context context;
	ArrayList<User> userArrayList;
	LayoutInflater inflater;
	
	public OnlineUserBaseAdapter(Context context, ArrayList<User> userArrayList) {
		this.userArrayList = userArrayList;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if(userArrayList != null || userArrayList.size() > 0 ){
			return userArrayList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return userArrayList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		convertView = inflater.inflate(
				R.layout.online_userlist_items_layout, null);
		ImageView image = (ImageView) convertView
				.findViewById(R.id.imageview_online_userlist_image);
		TextView text = (TextView) convertView
				.findViewById(R.id.textview_online_userlist_username);
		viewHolder = new ViewHolder(image, text);
		
		convertView.setTag(viewHolder);
		
		text.setText(userArrayList.get(position).username);
		
		return convertView;
	}

	public class ViewHolder {
		TextView text;
		ImageView image;

		public ViewHolder(TextView text) {
			this.text = text;
		}

		public ViewHolder(ImageView image, TextView text) {
			this.image = image;
			this.text = text;
		}

		public TextView getText() {
			return text;
		}

		public void setText(TextView text) {
			this.text = text;
		}

		public ImageView getImage() {
			return image;
		}

		public void setImage(ImageView image) {
			this.image = image;
		}

	}
}

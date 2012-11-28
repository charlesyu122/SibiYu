package com.actone.sibij.yuc.act01;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact>{
	
	private Context context;
	Contact[] values = null;
	
	public ContactAdapter(Context context, ArrayList<Contact> contactList) {
		super(context, R.layout.list_item, contactList);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.values = contactList.toArray(new Contact[contactList.size()]);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;
		if(rowView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		TextView tvCName = (TextView)rowView.findViewById(R.id.tvContactName);
		TextView tvCNum = (TextView)rowView.findViewById(R.id.tvContactNumber);
		tvCName.setText(values[position].name);
		tvCNum.setText(values[position].number);
		
		rowView.setTag(values[position]);
		return rowView;
	}

}

package com.mgr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ModelChooser extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AssetManager assetManager = getAssets();
		Vector<Item> models = new Vector<Item>();
		Item item = new Item();
		item.text = getResources().getString(R.string.choose_a_model);
		item.type = Item.TYPE_HEADER;
		models.add(item);
		try {
			String[] modelFiles = assetManager.list("models");
			List<String> modelFilesList = Arrays.asList(modelFiles);
			for(int i = 0; i < modelFiles.length; i++) {
				String currentFileName = modelFiles[i];
				if (currentFileName.endsWith(".obj")) {
					item = new Item();
					String trimmedFileName = currentFileName.substring(0, currentFileName.lastIndexOf(".obj"));
					item.text = trimmedFileName;
					models.add(item);
					if (modelFilesList.contains(trimmedFileName+".jpg")) {
						InputStream is = assetManager.open("models/"+trimmedFileName+".jpg");
						item.icon = (BitmapFactory.decodeStream(is));
					} else if (modelFilesList.contains(trimmedFileName+".png")) {
						InputStream is = assetManager.open("models/"+trimmedFileName+".png");
						item.icon = (BitmapFactory.decodeStream(is));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		setListAdapter(new ModelChooserListAdapter(models));
//		setContentView(R.layout.activity_model_chooser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.model_chooser, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		Item item = (Item) this.getListAdapter().getItem(position);
		String str = item.text;
		Intent intent = new Intent(ModelChooser.this, ModelViewer.class);
		intent.putExtra("name", str+".obj");
		intent.putExtra("type", ModelViewer.TYPE_INTERNAL);
		intent.setAction(Intent.ACTION_VIEW);
		startActivity(intent);
	}
	
	class ModelChooserListAdapter extends BaseAdapter {
		private Vector<Item> items;
		
		public ModelChooserListAdapter(Vector<Item> items) {
			this.items = items;
		}
		
		public int getCount() {
			return items.size();
		}
		
		public Object getItem(int position) {
			return items.get(position);
		}
		
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}
		
		@Override
		public boolean isEnabled(int position) {
			return !(getItemViewType(position) == Item.TYPE_HEADER);
		}
		
		@Override
		public int getItemViewType(int position) {
			return items.get(position).type;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Item item = items.get(position);
			if (view == null) {
				LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	switch(item.type) {            	
            	case Item.TYPE_HEADER:            		
                    view = layoutInflater.inflate(R.layout.list_header, null);
            		break;
            	case Item.TYPE_ITEM:
            		view = layoutInflater.inflate(R.layout.choose_model_row, null);
            		break;
            	}                
            }   
            if (item != null) {
	            switch (item.type) {            	
	        	case Item.TYPE_HEADER: 
	        		TextView headerText = (TextView) view.findViewById(R.id.list_header_title);
	        		if(headerText != null) {
	        			headerText.setText(item.text);
	        		}
	        		break;
	        	case Item.TYPE_ITEM:
	        		Object iconImage = item.icon;
	            	ImageView icon = (ImageView) view.findViewById(R.id.choose_model_row_icon);
	            	if(icon!=null) {
	            		if(iconImage instanceof Integer) {
	            			icon.setImageResource(((Integer)iconImage).intValue());
	            		} else if(iconImage instanceof Bitmap) {
	            			icon.setImageBitmap((Bitmap)iconImage);
	            		}
	            	}
	            	TextView text = (TextView) view.findViewById(R.id.choose_model_row_text);
	            	if (text!=null) {
	            		text.setText(item.text);   
	            	}
	        		break;
	        	}      
            }
			return view;
		}
	}
	
	class Item {
		private static final int TYPE_ITEM=0;
		private static final int TYPE_HEADER=1;
		private int type = TYPE_ITEM;
		private Object icon = new Integer(R.drawable.missingimage);
		private String text;
	}
}
	

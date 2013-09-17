package com.zhengwenhui.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	//private TextView messageTextView;
	private StringBuilder mStringBuilder;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contect);
		listview = (ListView) findViewById(R.id.listview);
		getDefaultWorkspace();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		//setContentView(R.layout.contect);
		//layout = R.layout.contect;

		switch (item.getItemId()) {
		case R.id.DefaultWorkspace:
			getDefaultWorkspace();
			break;
		case R.id.LauncherDb:
			getLauncherDb();
			break;
		case R.id.Favorite:
			getFavorite();
			break;
		case R.id.Appwidget:
			getAppwidget();
			break;
		case R.id.About:
			getAbout();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void getDefaultWorkspace(){
		this.setTitle(R.string.DefaultWorkspace);
		final String AUTHORITY = "com.android.launcher2.settings";  
		final Uri CONTENT_URI = Uri.parse("content://" +  AUTHORITY + "/favorites?notify=true");  

		String appwidgetBegin = "    <appwidget\n";
		String favoriteBegin = "    <favorite\n";
		String favoritesEnd = "</favorites>";
		String tagEnd = "        />";
		String attributeEnd = "\"\n";

		String packageName = "        launcher:packageName=\"";
		String className = "        launcher:className=\"";
		String container = "        launcher:container=\"";
		String screen = "        launcher:screen=\"";
		String x = "        launcher:x=\"";
		String y = "        launcher:y=\"";
		String spanX = "        launcher:spanX=\"";
		String spanY = "        launcher:spanY=\"";

		int itemtype;
		String intent;
		String title;
		String container_db;
		String screen_db;
		String cellX_db;
		String cellY_db;
		String spanX_db;
		String spanY_db;
		String[] in = {"",""};

		Cursor cursor = getContentResolver().query(CONTENT_URI,  
				null,  
				null,  
				null, 
				null);
		
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		StringBuilder builder = new StringBuilder();
		mStringBuilder =  new StringBuilder();
		
		//builder.delete(0, builder.length());

		if(cursor!=null && cursor.getCount()>0){

			builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			builder.append("<favorites xmlns:launcher=\"http://schemas.android.com/apk/res/com.android.launcher\">");

			map = new HashMap<String, Object>();
			map.put("text", builder.toString());
			listData.add(map);
			mStringBuilder.append(builder);
			builder.delete(0, builder.length());
			
			while (cursor.moveToNext()) {

				itemtype = cursor.getInt(9);
				title = cursor.getString(1);
				intent = cursor.getString(2);
				container_db =  cursor.getString(3);
				screen_db = cursor.getString(4);
				cellX_db = cursor.getString(5);
				cellY_db = cursor.getString(6);
				spanX_db = cursor.getString(7);
				spanY_db = cursor.getString(8);

				switch (itemtype) {
				case 0:
					if(intent!=null){
						in = intent.substring(intent.indexOf("component=")+10, intent.indexOf(";end")).split("/");
					}
					builder.append("    <!-- ");
					builder.append(title);
					builder.append(" -->\n");

					builder.append(favoriteBegin);

					builder.append(packageName);
					builder.append(in[0]);
					builder.append(attributeEnd);

					builder.append(className);
					if(in[1].startsWith(".")){
						builder.append(in[0]);
					}
					builder.append(in[1]);
					builder.append(attributeEnd);

					if(container_db.equals("-101")){
						builder.append(container);
						builder.append(container_db);
						builder.append(attributeEnd);
					}

					builder.append(screen);
					builder.append(screen_db);
					builder.append(attributeEnd);

					builder.append(x);
					builder.append(cellX_db);
					builder.append(attributeEnd);

					builder.append(y);
					builder.append(cellY_db);
					builder.append(attributeEnd);

					builder.append(tagEnd);
					
					map = new HashMap<String, Object>();
					map.put("text", builder.toString());
					listData.add(map);
					mStringBuilder.append(builder);
					builder.delete(0, builder.length());
					
					break;
				case 4:
					if(intent!=null){
						in = intent.substring(intent.indexOf("{")+1, intent.indexOf("}")).split("/");
					}

					builder.append(appwidgetBegin);

					builder.append(packageName);
					builder.append(in[0]);
					builder.append(attributeEnd);

					builder.append(className);
					if(in[1].startsWith(".")){
						builder.append(in[0]);
					}
					builder.append(in[1]);
					builder.append(attributeEnd);

					builder.append(screen);
					builder.append(screen_db);
					builder.append(attributeEnd);

					builder.append(x);
					builder.append(cellX_db);
					builder.append(attributeEnd);

					builder.append(y);
					builder.append(cellY_db);
					builder.append(attributeEnd);

					builder.append(spanX);
					builder.append(spanX_db);
					builder.append(attributeEnd);

					builder.append(spanY);
					builder.append(spanY_db);
					builder.append(attributeEnd);

					builder.append(tagEnd);
					
					map = new HashMap<String, Object>();
					map.put("text", builder.toString());
					listData.add(map);
					mStringBuilder.append(builder);
					builder.delete(0, builder.length());
					
					break;
				default:
					break;
				}
			}
			
			map = new HashMap<String, Object>();
			map.put("text", favoritesEnd);
			listData.add(map);
			mStringBuilder.append(favoritesEnd);
			builder.delete(0, builder.length());
		}
		
		SimpleAdapter mSchedule = new SimpleAdapter(this,
				listData,//数据来源   
				R.layout.listitem,
				new String[] {"text"},   
				new int[] {R.id.message});
		listview.setAdapter(mSchedule);
		
		cursor.close();
		writeFile();
	}

	private void writeFile() {
		File targetFile = new File(Environment.getExternalStorageDirectory().getPath()  
				+ "/default_workspace.xml");  
		if (targetFile.exists()) {  
			targetFile.delete();  
		}  

		OutputStreamWriter osw;
		try{  
			osw = new OutputStreamWriter(  
					new FileOutputStream(targetFile),"utf-8");  

			osw.write(mStringBuilder.toString());  
			osw.flush();  
			osw.close();  
			mStringBuilder.delete(0, mStringBuilder.length());
		} catch (Exception e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		}  
	}   

	public void getLauncherDb(){
		this.setTitle(R.string.LauncherDb);
		final String AUTHORITY = "com.android.launcher2.settings";  
		final Uri CONTENT_URI = Uri.parse("content://" +  AUTHORITY + "/favorites?notify=true");  

		Cursor cursor = getContentResolver().query(CONTENT_URI,  
				null,  
				null,  
				null, 
				"screen");

		int mColumnCount = cursor.getColumnCount();

		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		StringBuilder builder = new StringBuilder();
		
		if(cursor!=null && cursor.getCount()>0){
			while (cursor.moveToNext()) {
				builder.delete(0, builder.length());
				
				builder.append(format(cursor.getColumnName(0)));
				builder.append(": "+String.valueOf(cursor.getInt(0)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(1)));
				builder.append(": "+cursor.getString(1));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(2)));
				builder.append(": "+cursor.getString(2));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(3)));
				builder.append(": "+String.valueOf(cursor.getInt(3)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(4)));
				builder.append(": "+String.valueOf(cursor.getInt(4)));
				builder.append("\n");


				builder.append(format(cursor.getColumnName(5)));
				builder.append(": "+String.valueOf(cursor.getInt(5)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(6)));
				builder.append(": "+String.valueOf(cursor.getInt(6)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(7)));
				builder.append(": "+String.valueOf(cursor.getInt(7)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(8)));
				builder.append(": "+String.valueOf(cursor.getInt(8)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(9)));
				builder.append(": "+String.valueOf(cursor.getInt(9)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(10)));
				builder.append(": "+String.valueOf(cursor.getInt(10)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(11)));
				builder.append(": "+String.valueOf(cursor.getInt(11)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(12)));
				builder.append(": "+String.valueOf(cursor.getInt(12)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(13)));
				builder.append(": "+cursor.getString(13));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(14)));
				builder.append(": "+cursor.getString(14));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(15)));
				builder.append(": "+String.valueOf(cursor.getBlob(15)));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(16)));
				builder.append(": "+cursor.getString(16));
				builder.append("\n");

				builder.append(format(cursor.getColumnName(17)));
				builder.append(": "+String.valueOf(cursor.getInt(17)));

				map = new HashMap<String, Object>();
				//map.put("img", getIcon(packageName, name));
				map.put("text", builder.toString());
				listData.add(map);
			}
			
			SimpleAdapter mSchedule = new SimpleAdapter(this,
					listData,//数据来源   
					R.layout.listitem,
					new String[] {"text"},   
					new int[] {R.id.message});
			listview.setAdapter(mSchedule);
		}
		cursor.close();
	}  

	private String format(String object){
		return object;
	}

	private void getFavorite(){
		this.setTitle(R.string.Favorite);

		final PackageManager packageManager = getPackageManager();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);

		ImageSimpleAdapter mSchedule = new ImageSimpleAdapter(this,
				getFavoriteData(apps),//数据来源   
				R.layout.listitem,
				new String[] {"img","text"},   
				new int[] {R.id.image, R.id.message});
		listview.setAdapter(mSchedule);

	}

	private List<Map<String, Object>> getFavoriteData(List<ResolveInfo> apps) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		String packageName, name, text;
		java.text.DecimalFormat format = new java.text.DecimalFormat("000"); 

		for (ResolveInfo resolveInfo : apps) {
			packageName = resolveInfo.activityInfo.packageName;
			name = resolveInfo.activityInfo.name;
			text = "              "+packageName+"\n              "+name;

			map = new HashMap<String, Object>();
			map.put("img", packageName+","+name);
			map.put("text", text);
			list.add(map);
		}
		return list;
	}

	private void getAppwidget(){
		this.setTitle(R.string.Appwidget);

		AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(this);
		List<AppWidgetProviderInfo> listAppWidget = mAppWidgetManager.getInstalledProviders();

		String packageName;
		String name, text;
		int i = 0;
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		java.text.DecimalFormat format = new java.text.DecimalFormat("000"); 

		for (AppWidgetProviderInfo appWidgetProviderInfo : listAppWidget)
		{
			name = appWidgetProviderInfo.provider.getClassName();
			packageName = appWidgetProviderInfo.provider.getPackageName();

			text = format.format(i)+".\n           "+packageName+"\n           "+name;

			map = new HashMap<String, Object>();
			map.put("img", packageName+","+name);
			map.put("text", text);
			listData.add(map);
			i++;
		}

		ImageSimpleAdapter mSchedule = new ImageSimpleAdapter(this,
				listData,//数据来源   
				R.layout.listitem,
				new String[] {"img","text"},
				new int[] {R.id.image,R.id.message});
		listview.setAdapter(mSchedule);
	}

	private void getAbout(){
		//this.setTitle(R.string.About);
		Uri uri = Uri.parse("http://blog.csdn.net/yesterdaylike/article/details/8961855");  
		startActivity(new Intent(Intent.ACTION_VIEW,uri));
	}
}

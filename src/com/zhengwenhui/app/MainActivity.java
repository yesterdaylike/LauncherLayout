package com.zhengwenhui.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView messageTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contect);
		messageTextView = (TextView) findViewById(R.id.message);
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
		messageTextView = (TextView) findViewById(R.id.message);
		messageTextView.setText("");
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
		this.setTitle("default_workspace.xml");
		final String AUTHORITY = "com.android.launcher2.settings";  
		final Uri CONTENT_URI = Uri.parse("content://" +  AUTHORITY + "/favorites?notify=true");  

		String appwidgetBegin = "    <appwidget\n";
		String favoriteBegin = "    <favorite\n";
		String favoritesEnd = "</favorites>";
		String tagEnd = "        />\n\n";
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

		if(cursor!=null && cursor.getCount()>0){

			messageTextView.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			messageTextView.append("<favorites xmlns:launcher=\"http://schemas.android.com/apk/res/com.android.launcher\">\n");

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
					messageTextView.append("    <!-- ");
					messageTextView.append(title);
					messageTextView.append(" -->\n");

					messageTextView.append(favoriteBegin);

					messageTextView.append(packageName);
					messageTextView.append(in[0]);
					messageTextView.append(attributeEnd);

					messageTextView.append(className);
					if(in[1].startsWith(".")){
						messageTextView.append(in[0]);
					}
					messageTextView.append(in[1]);
					messageTextView.append(attributeEnd);

					if(container_db.equals("-101")){
						messageTextView.append(container);
						messageTextView.append(container_db);
						messageTextView.append(attributeEnd);
					}

					messageTextView.append(screen);
					messageTextView.append(screen_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(x);
					messageTextView.append(cellX_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(y);
					messageTextView.append(cellY_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(tagEnd);
					break;
				case 4:
					Log.i("catch zhengwenhui", ""+intent);

					if(intent!=null){
						in = intent.substring(intent.indexOf("{")+1, intent.indexOf("}")).split("/");
					}

					messageTextView.append(appwidgetBegin);

					messageTextView.append(packageName);
					messageTextView.append(in[0]);
					messageTextView.append(attributeEnd);

					messageTextView.append(className);
					if(in[1].startsWith(".")){
						messageTextView.append(in[0]);
					}
					messageTextView.append(in[1]);
					messageTextView.append(attributeEnd);

					messageTextView.append(screen);
					messageTextView.append(screen_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(x);
					messageTextView.append(cellX_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(y);
					messageTextView.append(cellY_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(spanX);
					messageTextView.append(spanX_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(spanY);
					messageTextView.append(spanY_db);
					messageTextView.append(attributeEnd);

					messageTextView.append(tagEnd);
					break;
				default:
					break;
				}
			}

			messageTextView.append(favoritesEnd);
		}
		cursor.close();
		writeFile();
	}  

	private void writeFile() {

		Log.e("zhengwenhui", Environment.getExternalStorageDirectory().getPath()  
				+ "/default_workspace.xml");

		File targetFile = new File(Environment.getExternalStorageDirectory().getPath()  
				+ "/default_workspace.xml");  
		if (targetFile.exists()) {  
			targetFile.delete();  
		}  

		OutputStreamWriter osw;
		try{  
			osw = new OutputStreamWriter(  
					new FileOutputStream(targetFile),"utf-8");  

			osw.write(messageTextView.getText().toString());  
			osw.flush();  
			osw.close();  

		} catch (Exception e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		}  
	}   

	public void getLauncherDb(){

		this.setTitle("LauncherDb");

		final String AUTHORITY = "com.android.launcher2.settings";  
		final Uri CONTENT_URI = Uri.parse("content://" +  AUTHORITY + "/favorites?notify=true");  

		Cursor cursor = getContentResolver().query(CONTENT_URI,  
				null,  
				null,  
				null, 
				"screen");

		int mColumnCount = cursor.getColumnCount();
		Log.v(""+mColumnCount, "   "+mColumnCount);

		//java.text.DecimalFormat format = new java.text.MessageFormat(template)

		if(cursor!=null && cursor.getCount()>0){
			while (cursor.moveToNext()) {

				messageTextView.append("\n-------------------------------------------------\n");
				messageTextView.append(format(cursor.getColumnName(0)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(0)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(1)));
				messageTextView.append(": "+cursor.getString(1));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(2)));
				messageTextView.append(": "+cursor.getString(2));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(3)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(3)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(4)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(4)));
				messageTextView.append("\n");


				messageTextView.append(format(cursor.getColumnName(5)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(5)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(6)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(6)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(7)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(7)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(8)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(8)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(9)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(9)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(10)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(10)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(11)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(11)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(12)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(12)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(13)));
				messageTextView.append(": "+cursor.getString(13));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(14)));
				messageTextView.append(": "+cursor.getString(14));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(15)));
				messageTextView.append(": "+String.valueOf(cursor.getBlob(15)));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(16)));
				messageTextView.append(": "+cursor.getString(16));
				messageTextView.append("\n");

				messageTextView.append(format(cursor.getColumnName(17)));
				messageTextView.append(": "+String.valueOf(cursor.getInt(17)));
				messageTextView.append("\n");
			}
		}
		cursor.close();
	}  

	private String format(String object){
		return object;
	}

	private void getFavorite(){

		this.setTitle("Favorite");

		final PackageManager packageManager = getPackageManager();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);

		String packageName;
		String name;
		int i = 0;
		java.text.DecimalFormat format = new java.text.DecimalFormat("000"); 
		messageTextView.append("---------Favorite---------\n");

		for (ResolveInfo resolveInfo : apps) {
			packageName = resolveInfo.activityInfo.packageName;
			name = resolveInfo.activityInfo.name;
			Log.v(""+i, packageName+"   "+name);
			messageTextView.append(format.format(i)+".\n           "+packageName+"\n           "+name+"\n\n");
			i++;
		}
	}

	private void getAppwidget(){

		this.setTitle("Appwidget");

		AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(this);
		List<AppWidgetProviderInfo> list = mAppWidgetManager.getInstalledProviders();

		String packageName;
		String name;
		int i = 0;

		java.text.DecimalFormat format = new java.text.DecimalFormat("000"); 

		messageTextView.append("---------Appwidget---------\n");
		for (AppWidgetProviderInfo appWidgetProviderInfo : list)
		{
			name = appWidgetProviderInfo.provider.getClassName();
			packageName = appWidgetProviderInfo.provider.getPackageName();

			Log.v(""+i, packageName+"   "+name);
			messageTextView.append(format.format(i)+".\n           "+packageName+"\n           "+name+"\n\n");
			i++;
		}

		messageTextView.setSelected(true);

	}
	private void getAbout(){
		this.setTitle("About");
		Uri uri = Uri.parse("http://blog.csdn.net/yesterdaylike/article/details/8961855");  
		startActivity(new Intent(Intent.ACTION_VIEW,uri));
	}
	
	/*private void getIcon(String pkg, String cls){
		ComponentName  name = new ComponentName(pkg, cls);
		PackageManager mPackageManager = getPackageManager();
		try {
			Drawable drawable = mPackageManager.getActivityIcon(name);
			mImage.setImageDrawable(drawable);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}

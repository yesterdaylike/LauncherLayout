package com.zhengwenhui.app;

import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class ImageSimpleAdapter extends SimpleAdapter {
	private Context mContext;
	public ImageSimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
					int[] to) {
		super(context, data, resource, from, to);
		mContext = context;
		// TODO Auto-generated constructor stub
	}
	private Drawable getIcon(String pkg, String cls){
		Drawable drawable = null;
		ComponentName  name = new ComponentName(pkg, cls);
		PackageManager mPackageManager = mContext.getPackageManager();
		try {
			drawable = mPackageManager.getActivityIcon(name);
			//mImage.setImageDrawable(drawable);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return drawable;
	}
	@Override
	public void setViewImage(ImageView v, String value) {
		// TODO Auto-generated method stub
		String [] pkgAndCls = value.split(",");
		
		Log.i("zhengwenhui", pkgAndCls[0]);
		Log.v("zhengwenhui", pkgAndCls[1]);
		
		Drawable drawable = getIcon(pkgAndCls[0], pkgAndCls[1]);
		if( null!=drawable ){
			v.setImageDrawable(drawable);
		}
	}
}

package com.adrorodri.control;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.widget.Toast;

public class Appsdb extends SQLiteOpenHelper
{
	static final int DATABASE_VERSION=1;
	static final String DATABASE_NAME="BlackList";
	static final String TABLE_APPS="Blocked";
	static final String KEY_ID="id";
	static final String KEY_APP_NAME="name";
	static final String KEY_PACKAGE="package";
	static final String KEY_ON="on";
	static final String KEY_TIME="time";
	static final String KEY_DATE_TIME_BLOCKED="timeblocked";
	
	public Appsdb(Context context) 
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_POINTS_TABLE = "CREATE TABLE "+TABLE_APPS+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_APP_NAME+" TEXT,"+KEY_PACKAGE+" TEXT,"+KEY_TIME+" INT,"+KEY_DATE_TIME_BLOCKED+" TEXT)";
		db.execSQL(CREATE_POINTS_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_APPS);
		db.delete(TABLE_APPS, null, null);
		onCreate(db);
	}
	public void addApp(Aplicacion app)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(KEY_APP_NAME,app.get_name());
		values.put(KEY_PACKAGE,app.get_packagename());
		values.put(KEY_TIME,60);
		values.put(KEY_DATE_TIME_BLOCKED,"2000:0:0:0:0:0");
		db.insert(TABLE_APPS,null,values);
		db.close();
	}
	public boolean addDateTimeBlocked(String packageName,String datetime)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues args = new ContentValues();
	    args.put(KEY_DATE_TIME_BLOCKED,datetime);
	    db.update(TABLE_APPS, args, KEY_PACKAGE + " = '" + packageName+"'", null);
	    db.close();
	    return true;
	}
	public String getDateTimeBlocked(String packageName)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor = null;
		try
		{
			cursor=db.query(TABLE_APPS,new String[]{KEY_ID,KEY_DATE_TIME_BLOCKED},KEY_PACKAGE+" =?",new String[]{packageName},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			String result = cursor.getString(1);
			db.close();
			cursor.close();
			return result;
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			db.close();
			cursor.close();
			return null;
		}
	}
	public Aplicacion getApp(int id)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor = null;
		try
		{
			cursor=db.query(TABLE_APPS,new String[]{KEY_ID,KEY_APP_NAME,KEY_PACKAGE},KEY_ID+" =?",new String[]{String.valueOf(id)},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			Aplicacion app = new Aplicacion(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
			db.close();
			cursor.close();
			return app;
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			db.close();
			cursor.close();
			return null;
		}
	}
	public Aplicacion getApp(String packagename)
	{
		Cursor cursor = null;
		try
		{
			SQLiteDatabase db=this.getReadableDatabase();
			cursor=db.query(TABLE_APPS,new String[]{KEY_ID,KEY_APP_NAME,KEY_PACKAGE},KEY_PACKAGE+" =?",new String[]{packagename},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			Aplicacion app = new Aplicacion(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
			db.close();
			return app;
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			cursor.close();
			return null;
		}
	}
	public boolean exists(String packagename)
	{
		Cursor cursor = null;
		SQLiteDatabase db=this.getReadableDatabase();
		try
		{
			cursor=db.query(TABLE_APPS,new String[]{KEY_ID,KEY_APP_NAME,KEY_PACKAGE},KEY_PACKAGE+" =?",new String[]{packagename},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			Aplicacion app = new Aplicacion(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
			db.close();
			cursor.close();
			return true;
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			db.close();
			cursor.close();
			return false;
		}
	}
	public List<Aplicacion> getAllApps()
	{
		List<Aplicacion> appList=new ArrayList<Aplicacion>();
		String selectQuery="SELECT * FROM "+TABLE_APPS;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if(cursor.moveToFirst())
		{
			do{
				Aplicacion app=new Aplicacion();
				app.setID(Integer.parseInt(cursor.getString(0)));
				app.set_name(cursor.getString(1));
				app.set_packagename(cursor.getString(2));
				appList.add(app);
			}
			while(cursor.moveToNext());
		}
		db.close();
		return appList;
	}
	public void deleteApp(Aplicacion app)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_APPS,KEY_PACKAGE+" =?",new String[]{String.valueOf(app.get_packagename())});
		db.close();
		rebuildDb();
	}
	public void deleteApp(String packageName)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_APPS,KEY_PACKAGE+" =?",new String[]{String.valueOf(packageName)});
		db.close();
		rebuildDb();
	}
	public void rebuildDb()
	{
		List<Aplicacion> appList=new ArrayList<Aplicacion>();
		String selectQuery="SELECT * FROM "+TABLE_APPS;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if(cursor.moveToFirst())
		{
			do{
				Aplicacion app=new Aplicacion();
				app.setID(Integer.parseInt(cursor.getString(0)));
				app.set_name(cursor.getString(1));
				app.set_packagename(cursor.getString(2));
				appList.add(app);
			}
			while(cursor.moveToNext());
		}
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_APPS);
		String CREATE_POINTS_TABLE = "CREATE TABLE "+TABLE_APPS+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_APP_NAME+" TEXT,"+KEY_PACKAGE+" TEXT,"+KEY_TIME+" INT,"+KEY_DATE_TIME_BLOCKED+" TEXT)";
		db.execSQL(CREATE_POINTS_TABLE);
		for(Aplicacion bk: appList)
		{
			String name=bk.get_name();
			String packagename=bk.get_packagename();
			addApp(new Aplicacion(name,packagename));
		}
		db.close();
	}
}

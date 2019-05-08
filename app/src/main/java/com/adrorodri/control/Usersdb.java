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

public class Usersdb extends SQLiteOpenHelper
{
	static final int DATABASE_VERSION=1;
	static final String DATABASE_NAME="Users";
	static final String TABLE_USERS="TableUsers";
	static final String KEY_ID="id";
	static final String KEY_USER="user";
	static final String KEY_PASSWORD="password";
	static final String KEY_TYPE="type";
	
	public Usersdb(Context context) 
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_POINTS_TABLE = "CREATE TABLE "+TABLE_USERS+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_USER+" TEXT,"+KEY_PASSWORD+" TEXT,"+KEY_TYPE+" TEXT)";
		db.execSQL(CREATE_POINTS_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
		db.delete(TABLE_USERS, null, null);
		onCreate(db);
	}
	public void addUser(User user)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(KEY_USER,user.get_user());
		values.put(KEY_PASSWORD,user.get_password());
		values.put(KEY_TYPE,user.get_type());
		db.insert(TABLE_USERS,null,values);
		db.close();
	}
	public boolean isAdmin(String username)
	{
		Cursor cursor = null;
		try
		{
			SQLiteDatabase db=this.getReadableDatabase();
			cursor=db.query(TABLE_USERS,new String[]{KEY_ID,KEY_USER,KEY_PASSWORD,KEY_TYPE},KEY_USER+" =?",new String[]{username},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			User user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
			db.close();
			if(user.get_type().equals("ADMIN"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			cursor.close();
			return false;
		}
	}
	public User getUser(int id)
	{
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor = null;
		try
		{
			cursor=db.query(TABLE_USERS,new String[]{KEY_ID,KEY_USER,KEY_PASSWORD},KEY_ID+" =?",new String[]{String.valueOf(id)},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			User user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
			db.close();
			cursor.close();
			return user;
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			db.close();
			cursor.close();
			return null;
		}
	}
	public User getUser(String username)
	{
		Cursor cursor = null;
		try
		{
			SQLiteDatabase db=this.getReadableDatabase();
			cursor=db.query(TABLE_USERS,new String[]{KEY_ID,KEY_USER,KEY_PASSWORD,KEY_TYPE},KEY_USER+" =?",new String[]{username},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			User user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
			db.close();
			return user;
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			cursor.close();
			return null;
		}
	}
	public boolean exists(String password)
	{
		Cursor cursor = null;
		SQLiteDatabase db=this.getReadableDatabase();
		try
		{
			cursor=db.query(TABLE_USERS,new String[]{KEY_ID,KEY_USER,KEY_PASSWORD,KEY_TYPE},KEY_PASSWORD+" =?",new String[]{password},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			User user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
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
	public boolean exists(String username, String password) {
		Cursor cursor = null;
		SQLiteDatabase db=this.getReadableDatabase();
		try
		{
			cursor=db.query(TABLE_USERS,new String[]{KEY_ID,KEY_USER,KEY_PASSWORD,KEY_TYPE},KEY_USER+" =?",new String[]{username},null,null,null);
			if(cursor != null)
			{
				cursor.moveToFirst();
			}
			User user = new User(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
			if(user.get_password().equals(password))
			{
				db.close();
				cursor.close();
				return true;
			}
			else
			{
				db.close();
				cursor.close();
				return false;
			}
		}
		catch(CursorIndexOutOfBoundsException cioofe)
		{
			db.close();
			cursor.close();
			return false;
		}
	}
	public List<User> getAllUsers()
	{
		List<User> userList=new ArrayList<User>();
		String selectQuery="SELECT * FROM "+TABLE_USERS;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if(cursor.moveToFirst())
		{
			do{
				User user=new User();
				user.setID(Integer.parseInt(cursor.getString(0)));
				user.set_user(cursor.getString(1));
				user.set_password(cursor.getString(2));
				user.set_type(cursor.getString(3));
				userList.add(user);
			}
			while(cursor.moveToNext());
		}
		db.close();
		return userList;
	}
	public void deleteUser(User user)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_USERS,KEY_USER+" =?",new String[]{String.valueOf(user.get_user())});
		db.close();
		rebuildDb();
	}
	public void deleteUser(String user)
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_USERS,KEY_USER+" =?",new String[]{String.valueOf(user)});
		db.close();
		rebuildDb();
	}
	public void rebuildDb()
	{
		List<User> userList=new ArrayList<User>();
		String selectQuery="SELECT * FROM "+TABLE_USERS;
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor=db.rawQuery(selectQuery,null);
		if(cursor.moveToFirst())
		{
			do{
				User user=new User();
				user.setID(Integer.parseInt(cursor.getString(0)));
				user.set_user(cursor.getString(1));
				user.set_password(cursor.getString(2));
				user.set_type(cursor.getString(3));
				userList.add(user);
			}
			while(cursor.moveToNext());
		}
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
		String CREATE_POINTS_TABLE = "CREATE TABLE "+TABLE_USERS+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+KEY_USER+" TEXT,"+KEY_PASSWORD+" TEXT,"+KEY_TYPE+" TEXT)";
		db.execSQL(CREATE_POINTS_TABLE);
		for(User us: userList)
		{
			String user=us.get_user();
			String password=us.get_password();
			String type=us.get_type();
			addUser(new User(user,password,type));
		}
		db.close();
	}
}
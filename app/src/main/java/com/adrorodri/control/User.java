package com.adrorodri.control;

public class User
{
    int _id;
    String _user;
    String _password;
    String _type;
    public User()
    {
    	
    }
    public User(String user,String password,String type)
    {
        this._user=user;
        this._password=password;
        this._type=type;
    }
    public User(int id,String user,String password,String type)
    {
        this._id=id;
        this._user=user;
        this._password=password;
        this._type=type;
    }
    public int getID()
    {
        return this._id;
    }
    public void setID(int id)
    {
        this._id=id;
    }
	public String get_user() {
		return _user;
	}
	public void set_user(String user) {
		this._user=user;
	}
	public String get_password() {
		return _password;
	}
	public void set_password(String password) {
		this._password=password;
	}
	public String get_type() {
		return _type;
	}
	public void set_type(String type) {
		this._type=type;
	}
}
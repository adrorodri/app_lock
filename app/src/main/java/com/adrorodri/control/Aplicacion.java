package com.adrorodri.control;

public class Aplicacion
{
    int _id;
    String _name;
    String _packagename;
    public Aplicacion ()
    {
    	
    }
    public Aplicacion(String name,String packagename)
    {
        this._name=name;
        this._packagename=packagename;
    }
    public Aplicacion(int id,String name,String packagename)
    {
        this._id=id;
        this._name=name;
        this._packagename=packagename;
    }
    public int getID()
    {
        return this._id;
    }
    public void setID(int id)
    {
        this._id=id;
    }
	public String get_name() {
		return _name;
	}
	public void set_name(String name) {
		this._name=name;
	}
	public String get_packagename() {
		return _packagename;
	}
	public void set_packagename(String packagename) {
		this._packagename=packagename;
	}
}
package com.adrorodri.control;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.text.format.Time;
import android.widget.Toast;

public class Interceptor extends Service {

private Handler handler;
Runnable runnable;
private Handler innerHandler;
List<Aplicacion>blockedApps;
String currentPackageName;
Time now = new Time();
Time dbtime=new Time();
//private AlertDialog alerta
	@Override
	public void onCreate() {
	    // TODO Auto-generated method stub
	    super.onCreate();
	    Appsdb db=new Appsdb(getApplicationContext());
	    blockedApps=db.getAllApps();
	    handler = new Handler();
	    runnable = new Runnable() {
	        @Override
	        public void run() {
	        	while(true)
	        	{
	        		try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                	ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                    ComponentName componentInfo = taskInfo.get(0).topActivity;
                    String currentActivityName=componentInfo.getClassName();
                    String packageName=componentInfo.getPackageName();
                    currentPackageName=packageName;
//	    	                	if(whitelist.contains(currentActivityName))
//	    	                    {
//	    	                        Intent launchIntent = new Intent();
//	    	                        launchIntent.setComponent(new ComponentName(blockActivityPackageName,blockActivityName));
//	    	                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    	                        startActivity(launchIntent);
//	    	                    }
                    //try
                    //{
                    //Appsdb db=new Appsdb(getApplicationContext());
            		if(comprobar(packageName) && dentrodeltiempo(packageName)==false)
            		{
            			//System.out.println("LA ALPLICACION MOSTRARA EL INTENT!!!");
            			//db.close();
                    	message(currentActivityName,packageName);
            		}
            		else
            		{
            			if(packageName.equals("com.adrorodri.control"))
            			{
            				parar();
            				break;
            			}
            			else
            			{
            				//System.out.println("LA APLICACION NO DEBERIA MOSTRAR EL INTENT");
            				//db.close();
            			}
            		}
	                    //System.out.println("Current activity name: "+currentActivityName);
	                    //System.out.println("Current package name: "+packageName);
                    //}
                    //catch (Exception e)
                    //{
                    	//System.out.println("Hubo algun error al leer la base de datos "+packageName);
                    //}
    			}
	        }
	    };
        handler.postDelayed(runnable, 1000);
	}
	public boolean comprobar(String packageName)
	{
		for(Aplicacion app : blockedApps)
		{
			if(app.get_packagename().equals(packageName))
			{
				return true;
			}
		}
		return false;
	}
	public boolean dentrodeltiempo(String packageName)
	{
		now.setToNow();
		Appsdb db=new Appsdb(this);
		String [] time= db.getDateTimeBlocked(packageName).split(":");
		System.out.println(time[5]+":"+time[4]+":"+time[3]+":"+time[2]+":"+time[1]+":"+time[0]);
		String year=time[0];
		String month=time[1];
		String monthday=time[2];
		String hour=time[3];
		String minute=time[4];
		String second=time[5];
		dbtime.set(Integer.parseInt(second),Integer.parseInt(minute),Integer.parseInt(hour),Integer.parseInt(monthday),Integer.parseInt(month),Integer.parseInt(year));
		System.out.println("ACTUAL: A�O: "+now.year+" MES: "+now.month+" DIA: "+now.monthDay+" HORA: "+now.hour+" MINUTO: "+now.minute+" SEGUNDO: "+now.second);
		System.out.println("BASEDD: A�O: "+dbtime.year+" MES: "+dbtime.month+" DIA: "+dbtime.monthDay+" HORA: "+dbtime.hour+" MINUTO: "+dbtime.minute+" SEGUNDO: "+dbtime.second);
		if(now.before(dbtime))
		{
			System.out.println("El tiempo now esta antes de dbtime");
		}
		else
		{
			System.out.println("El tiempo dbtime esta antes de now");
		}
		return now.before(dbtime);
	}
	public void parar()
	{
		System.out.println("El servicio se paro");
		handler.removeCallbacks(runnable);
		this.stopSelf();
	}
	public void message(String currentActivityName,String currentPackageName) {
		//final Toast toast = Toast.makeText(getApplicationContext(), "Current activity"+currentActivityName, Toast.LENGTH_SHORT);
		//System.out.println("Mensaje");
		//toast.cancel();
		//System.out.println("start");
		//toast.show();
		Intent intent = new Intent(Interceptor.this, Advertencia.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("currentPackageName",currentPackageName);
		System.out.println("CURRENT PACKAGE NAME: "+currentPackageName);
		startActivity(intent);
//		alerta=new AlertDialog.Builder(getApplicationContext()).setTitle("Se intercept� la aplicacion!:").setMessage("Nombre: prueba1"+"\nPaquete: prueba2").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
//				vib.vibrate(50);
//			}
//		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
//				vib.vibrate(50);
//		    }
//		}).create();
//		alerta.show();
	}
	@Override
	public void onStart(Intent intent, int startId) {
	    super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
	    return null;
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    /*try
	    {
	    	if(currentPackageName.equals("com.example.control")==false)
		    {
		    	System.out.println("El sevicio comenz� a correr ahora");
		    	startService(new Intent(this, Interceptor.class));
		    }
	    }
	    catch (NullPointerException npe)
	    {
	    	Toast.makeText(getApplicationContext(),"Control: �Poca Memoria!", Toast.LENGTH_SHORT).show();
	    }*/
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    return START_STICKY;
	}
}
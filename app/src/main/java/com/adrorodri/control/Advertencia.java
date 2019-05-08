package com.adrorodri.control;

import java.util.Date;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Advertencia extends Activity {
	String packageName="ERROR";
	RelativeLayout rl;
	Handler handler;
	Handler handler2;
	EditText et2;
	AlertDialog alertaUsuario2;
	AlertDialog alertaProblema;
	int aux=0;
	@Override
	public void onBackPressed() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		stopService(new Intent(this, Interceptor.class));
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.intercepted_main);
		Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		vib.vibrate(150);
		rl=(RelativeLayout)findViewById(R.id.layout);
		//setTitle("ALERTA");
		if(getIntent().hasExtra("currentPackageName"))
		{
			Bundle bundle=getIntent().getExtras();
			String currentPackageName=bundle.getString("currentPackageName");
			packageName=currentPackageName;
			System.out.println("SI SE ENCONTRO EL NOMBRE DEL PAQUETE: "+currentPackageName);
		}
		else
		{
			Button continuar1min=(Button) findViewById(R.id.button1);
			continuar1min.setVisibility(View.INVISIBLE);
		}
		handler = new Handler();
		    (new Thread(){
		        @Override
		        public void run(){
		        	final int[]rosa={227,109,227};//ROSADO
		        	final int[]azul={109,109,227};//AZUL
		        	final int[]cyan={109,227,227};//CYAN
		        	final int[]verde={109,227,109};//VERDE
		        	final int[]amarillo={227,227,109};//AMARILLO
		        	final int[]rojo={227,109,109};//ROJO
		        	int coloractual=1;
		        	while(coloractual>0 && aux==0)
		        	{
		        		System.out.println("Color actual: "+coloractual);
			        	switch(coloractual)
			        	{
				        	case 1:
				        	{
					            for(int i=rosa[0]; i>109; i--){
					            	final int ii=i;
					                handler.post(new Runnable(){
					                    public void run(){
					                        rl.setBackgroundColor(Color.argb(255, ii, rosa[1], rosa[2]));
					                    }
					                });
					                // next will pause the thread for some time
					                try{ sleep(100); }
					                catch(Exception e){ break; }
					                coloractual=2;
					            }
					            break;
				        	}
				        	case 2:
				        	{
					            for(int i=azul[1]; i<227; i++){
					            	final int ii=i;
					                handler.post(new Runnable(){
					                    public void run(){
					                        rl.setBackgroundColor(Color.argb(255, azul[0], ii, azul[2]));
					                    }
					                });
					                // next will pause the thread for some time
					                try{ sleep(100); }
					                catch(Exception e){ break; }
					                coloractual=3;
					            }
					            break;
				        	}
				        	case 3:
				        	{
					            for(int i=cyan[2]; i>109; i--){
					            	final int ii=i;
					                handler.post(new Runnable(){
					                    public void run(){
					                        rl.setBackgroundColor(Color.argb(255,cyan[0], cyan[1], ii));
					                    }
					                });
					                // next will pause the thread for some time
					                try{ sleep(100); }
					                catch(Exception e){ break; }
					                coloractual=4;
					            }
					            break;
				        	}
				        	case 4:
				        	{
					            for(int i=verde[0]; i<227; i++){
					            	final int ii=i;
					                handler.post(new Runnable(){
					                    public void run(){
					                        rl.setBackgroundColor(Color.argb(255,ii,verde[1],verde[2]));
					                    }
					                });
					                // next will pause the thread for some time
					                try{ sleep(100); }
					                catch(Exception e){ break; }
					                coloractual=5;
					            }
					            break;
				        	}
				        	case 5:
				        	{
					            for(int i=amarillo[1]; i>109; i--){
					            	final int ii=i;
					                handler.post(new Runnable(){
					                    public void run(){
					                        rl.setBackgroundColor(Color.argb(255,amarillo[0],ii, amarillo[2]));
					                    }
					                });
					                // next will pause the thread for some time
					                try{ sleep(100); }
					                catch(Exception e){ break; }
					                coloractual=6;
					            }
					            break;
				        	}
				        	case 6:
				        	{
					            for(int i=rojo[2]; i<227; i++){
					            	final int ii=i;
					                handler.post(new Runnable(){
					                    public void run(){
					                        rl.setBackgroundColor(Color.argb(255,rojo[0],rojo[1],ii));
					                    }
					                });
					                // next will pause the thread for some time
					                try{ sleep(100); }
					                catch(Exception e){ break; }
					                coloractual=1;
					            }
					            break;
				        	}
				        	default:
				        	{
				        		//Toast.makeText(getApplicationContext(),"Error en colores",Toast.LENGTH_SHORT).show();
				        		coloractual=0;
				        		break;
				        	}
			        	}
		        	}
		        }
		    }).start();
	}
	@Override
	public void onPause() {
        super.onDestroy();
        System.out.println("PAUSE");
    }
	@Override
	public void onStop() {
        super.onDestroy();
        aux=1;
        System.out.println("STOP");
	    if(isMyServiceRunning())
		{
	    	System.out.println("El sevicio ya esta corriendo");
			stopService(new Intent(this, Interceptor.class));
			System.out.println("El sevicio se paro");
			startService(new Intent(this, Interceptor.class));
			System.out.println("El sevicio comenz— a correr ahora");
		}
		else
		{
			System.out.println("El sevicio comenz— a correr ahora");
			startService(new Intent(this, Interceptor.class));
		}
    }
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (Interceptor.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	@Override
	public void onDestroy() {
        super.onDestroy();
        System.out.println("DESTROY");
    }
	public void Continuar (View view)
	{
//		Appsdb db=new Appsdb(this);
//		db.deleteApp(packageName);
//		Toast.makeText(getApplicationContext(),"Aplicacion Desbloqueada",Toast.LENGTH_SHORT).show();
//		db.close();
		LayoutInflater inflater1 = getLayoutInflater();
		View view2 = inflater1.inflate(R.layout.login_popup,null);
		et2 = (EditText) view2.findViewById(R.id.editText2);
		alertaUsuario2=new AlertDialog.Builder(this).setTitle("Confirmar: ")/*.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
				if(comprobar())
				{
					finish();
					Intent intent = new Intent(Advertencia.this, MainActivity.class);
					startActivity(intent);
				}
			}
		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
		    }
		})*/.setView(view2).setCancelable(false).create();
		Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		vib.vibrate(50);
		alertaUsuario2.show();
	}
	protected boolean comprobar() {
		Usersdb udb=new Usersdb(this);
		/*if(udb.exists(String.valueOf(et1.getText())))
		{*/
			if(udb.exists(String.valueOf(et2.getText())))
			{
				/*if(udb.isAdmin(String.valueOf(et1.getText())))
				{*/
					Toast.makeText(getApplicationContext(),"Password ingresado correctamente",Toast.LENGTH_SHORT).show();
					return true;
				/*}
				else
				{
					Toast.makeText(getApplicationContext(),"El usuario no tiene privilegios",Toast.LENGTH_SHORT).show();
					return false;
				}*/
			}
			else
			{
				//Toast.makeText(getApplicationContext(),"Password incorrecto",Toast.LENGTH_SHORT).show();
				return false;
			}
		/*}
		else
		{
			Toast.makeText(getApplicationContext(),"Usuario o password incorrecto",Toast.LENGTH_SHORT).show();
			return false;
		}*/
	}
	public void add1(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"1");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add2(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"2");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add3(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"3");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add4(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"4");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add5(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"5");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add6(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"6");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add7(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"7");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add8(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"8");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add9(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"9");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void add0(View view)
	{
		et2.setText(String.valueOf(et2.getText())+"0");
		if(comprobar())
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaUsuario2.cancel();
			finish();
			Intent intent = new Intent(Advertencia.this, ConfigActivity.class);
			startActivity(intent);
		}
	}
	public void deleteInput(View view)
	{
		if(String.valueOf(et2.getText()).length()>0)
		{
			et2.setText(String.valueOf(et2.getText()).substring(0,String.valueOf(et2.getText()).length()-1));
		}
	}
	public void CerrarPopup(View view)
	{	
		Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		vib.vibrate(50);
		alertaUsuario2.cancel();
	}
	public void Continuar1Min(View view)
	{
		LayoutInflater inflater1 = getLayoutInflater();
		View view2 = inflater1.inflate(R.layout.solve_problem_popup,null);
		final EditText number= (EditText) view2.findViewById(R.id.editText1);
		TextView problema=(TextView) view2.findViewById(R.id.textView1);
		int numero1=(int)(Math.random()*39+10);
		int numero2=(int)(Math.random()*39+10);
		final int resultado=numero1+numero2;
		problema.setText(String.valueOf(numero1)+" + "+String.valueOf(numero2)+"=");
		alertaProblema=new AlertDialog.Builder(this).setTitle("Confirmar: ").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if(String.valueOf(number.getText()).equals(String.valueOf(resultado)))
					{
						Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
						vib.vibrate(50);
						Time now = new Time();
						now.setToNow();
						Appsdb db=new Appsdb(getApplicationContext());
						db.addDateTimeBlocked(packageName,now.year+":"+now.month+":"+now.monthDay+":"+now.hour+":"+(now.minute+1)+":"+now.second);
						System.out.println("Updated time: "+db.getDateTimeBlocked(packageName));
						db.close();
						finish();
					}
					else
					{
						alertaProblema.cancel();
						Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
						vib.vibrate(50);
						Toast.makeText(getApplicationContext(),"Usted no resolvi— el problema",Toast.LENGTH_SHORT).show();
					}
				}
			}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
					vib.vibrate(50);
					Toast.makeText(getApplicationContext(),"Usted no resolvi— el problema",Toast.LENGTH_SHORT).show();
			    }
			}).setView(view2).setCancelable(false).create();
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			alertaProblema.show();
	}
}

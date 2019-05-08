package com.adrorodri.control;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RelativeLayout;
public class MainActivity extends Activity
{
	ListView lv;
	AlertDialog info=null;
	String AppSeleccionada="ERROR";
	//int pos;
	String PaqueteSeleccionado="ERROR";
	String packageName="ERROR";
	Button bt;
	AlertDialog alertaUsuario=null;
	AlertDialog alertaUsuario2=null;
	EditText et1;
	EditText et2;
	ImageView im;
	View line;
	ArrayList<String> ListaPackages;
	ArrayList<String> ListaApps;
	Handler handler;
	int pass;
	@Override
	public void onBackPressed() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
		startActivity(intent);

		//System.out.println(leerArchivo());
//		if(leerArchivo().equals("ERROR"))
//		{
			this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.login_main);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titulo_first);
			et1= (EditText) findViewById(R.id.editText1);
			et1.requestFocus();
		}
/*		else
		{
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.activity_main);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titulo_main);
			//setTitle("Control de Aplicaciones");
			bt=(Button)findViewById(R.id.activarbutton);
			im=(ImageView)findViewById(R.id.imageView2);
			LayoutInflater inflater = getLayoutInflater();
			View view1 = inflater.inflate(R.layout.login_popup,null);
			et2= (EditText) view1.findViewById(R.id.editText2);
			line=(View)findViewById(R.id.view1);
			if(leerArchivo().equals("NO"))
			{
				bt.setText("SERVICIO APAGADO");
				im.setImageResource(R.drawable.gradient_shape_red);
				line.setBackgroundColor(Color.RED);
			}
			if(leerArchivo().equals("SI"))
			{
				bt.setText("SERVICIO INICIADO");
				im.setImageResource(R.drawable.gradient_shape_green);
				line.setBackgroundColor(Color.GREEN);
			}
			final PackageManager pm = getPackageManager();
			//get a list of installed apps.
			List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			ListaPackages=new ArrayList<String>();
			ListaApps=new ArrayList<String>();
			//ArrayList<Intent> Activities=new ArrayList<Intent>();
			for (ApplicationInfo packageInfo : packages)
			{
				if (packageInfo.sourceDir.startsWith("/data/app/") && packageInfo.packageName.toLowerCase().contains("widget")==false && packageInfo.packageName.equals("com.adrorodri.control")==false && packageInfo.packageName.equals("com.example.interceptor")==false)
				{
					ListaPackages.add(packageInfo.packageName);
				    //Activities.add(pm.getLaunchIntentForPackage(packageInfo.packageName));   
				    ListaApps.add(String.valueOf(pm.getApplicationLabel(packageInfo)));
				}
			}
			lv=(ListView)findViewById(R.id.listView1);
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			String[] StringApps = new String[ListaPackages.size()];
			for (int i = 0; i < ListaPackages.size(); i++)
			{
				StringApps[i] = ListaApps.get(i);
			}
			ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,StringApps);
			lv.setAdapter(adapter);
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {
	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
	            	Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
	        		vib.vibrate(50);
	        		AppSeleccionada=ListaApps.get(position);
	        		PaqueteSeleccionado=ListaPackages.get(position);
	        		infopopup();
	            	return true;
	            }
	        });
	//		lv.setAdapter(new ArrayAdapter(this, R.id.counlistView, groups)
	//		{
	//		    @Override
	//		    public View getView(int position, View convertView, ViewGroup parent)
	//		    {
	//		        final View renderer = super.getView(position, convertView, parent);
	//		        if (position == selectedListItem)
	//		        {
	//		            //TODO: set the proper selection color here:
	//		            renderer.setBackgroundResource(android.R.color.darker_gray);
	//		        }
	//		        return renderer;
	//		    }
	//		});
			//lv.setItemChecked(1,true);
			mostrarBloqueados(ListaPackages);
			lv.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			    	int pos=position;
					mostrarAlertaUsuario(pos);
			    //lv.setSelection(position);
			    }});
		}
	}*/
	public void AceptarInicial(View view)
	{
		final Usersdb udb=new Usersdb(this);
		if(String.valueOf(et1.getText()).equals(""))
		{
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(150);
			Toast.makeText(getApplicationContext(),"Ingrese datos correctamente",Toast.LENGTH_SHORT).show();
		}
		else
		{	
			Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
			vib.vibrate(50);
			setContentView(R.layout.pass_main);
			TextView tv1 = (TextView) findViewById(R.id.textViewFirst);
			pass=(int)(Math.random()*899999+100000);
			tv1.setText(String.valueOf(pass));
			Toast.makeText(getApplicationContext(),"Usuario y password agregados",Toast.LENGTH_SHORT).show();
			udb.addUser(new User(String.valueOf(et1.getText()),String.valueOf(pass),"ADMIN"));
			escribirArchivo();
		}
	}
	public void continuar(View view)
	{
		alertaUsuario=new AlertDialog.Builder(this).setTitle("ADVERTENCIA:").setMessage("Al aceptar, esta aplicaci�n desaparecer� de tu pantalla de inicio.\n\nPodr�s acceder a ella llamando al numero:\n\n *CONTROL (*2668765)").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
					vib.vibrate(50);
					alertaUsuario.cancel();
					PackageManager p = getPackageManager();
					p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
					Intent refresh =new Intent(getApplicationContext(), ConfigActivity.class);
					finish();
					startActivity(refresh);
				}
			}).setCancelable(false).create();
		Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		vib.vibrate(50);
		alertaUsuario.show();
	}
	public void sendEmail(View view)
	{
		Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "Password Administrador de Control");
		intent.putExtra(Intent.EXTRA_TEXT, "El password es: "+pass);
		intent.setData(Uri.parse("mailto:"+et1.getText())); // or just "mailto:" for blank
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
		vib.vibrate(50);
		switch (item.getItemId()) {
	        case R.id.acercaDe:
	            acercaDe();
	            return true;
	        /*case R.id.anadirUsuario:
	        	anadirUsuario();
	        	return true;*/
	        default:
	        {
			    return true;
		    }
        }
	}
	private void acercaDe() {
		AlertDialog acercaDe = new AlertDialog.Builder(this).setTitle("Acerca De:").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
		    }
		}).setMessage("Creado por: \nAdri�n Mauricio Rodr�guez Andrade").setView(null).create();
		acercaDe.show();
	}
	@Override
	public void onDestroy() {
        super.onDestroy();
        //PackageManager p = getPackageManager();
		//p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        System.out.println("DESTROY");
    }
	public boolean escribirArchivo() {
        String nomarchivo="Settings.txt";
        String contenido="NO";
        try
        {        
	        FileOutputStream fOut = openFileOutput(nomarchivo,MODE_WORLD_WRITEABLE);
	        OutputStreamWriter osw = new OutputStreamWriter(fOut); 
	        osw.write(contenido);
	        osw.flush();
	        osw.close();
            return true;
        } 
        catch (IOException ioe) 
        {
        	return false;
        }
    }
	public boolean escribirArchivo(String truefalse) {
        String nomarchivo="Settings.txt";
        String contenido=truefalse;
        try
        {    
	        FileOutputStream fOut = openFileOutput(nomarchivo,MODE_WORLD_WRITEABLE);
	        OutputStreamWriter osw = new OutputStreamWriter(fOut); 
	        osw.write(contenido);
	        osw.flush();
	        osw.close();
            return true;
        } 
        catch (IOException ioe) 
        {
        	return false;
        }
    }
	public String leerArchivo() {
        String nomarchivo = "Settings.txt";
        StringBuffer outStringBuf = new StringBuffer();
        String inputLine = "";
        try
        {
        	FileInputStream fIn = getApplicationContext().openFileInput(nomarchivo);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader inBuff = new BufferedReader(isr);
            while ((inputLine = inBuff.readLine()) != null) {
                outStringBuf.append(inputLine);
                outStringBuf.append("\n");
            }
            inBuff.close();
            String [] Data=outStringBuf.toString().split("\n");
            String truefalse=Data[0];
            return truefalse;
        } 
        catch (IOException e)
        {
        	return "ERROR";
        }
    }
}
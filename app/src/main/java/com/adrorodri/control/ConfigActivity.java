package com.adrorodri.control;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ConfigActivity extends Activity {
    ListView lv;
    AlertDialog info = null;
    String AppSeleccionada = "ERROR";
    int pos;
    String PaqueteSeleccionado = "ERROR";
    String packageName = "ERROR";
    Button bt;
    AlertDialog alertaUsuario = null;
    AlertDialog alertaUsuario2 = null;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println(leerArchivo());
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titulo_main);
        //setTitle("Control de Aplicaciones");
        bt = (Button) findViewById(R.id.activarbutton);
        im = (ImageView) findViewById(R.id.imageView2);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.login_popup, null);
        et2 = (EditText) view1.findViewById(R.id.editText2);
        line = (View) findViewById(R.id.view1);
        if (leerArchivo().equals("NO")) {
            bt.setText("SERVICIO APAGADO");
            im.setImageResource(R.drawable.gradient_shape_red);
            line.setBackgroundColor(Color.RED);
        }
        if (leerArchivo().equals("SI")) {
            bt.setText("SERVICIO INICIADO");
            im.setImageResource(R.drawable.gradient_shape_green);
            line.setBackgroundColor(Color.GREEN);
        }
        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ListaPackages = new ArrayList<String>();
        ListaApps = new ArrayList<String>();
        //ArrayList<Intent> Activities=new ArrayList<Intent>();
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.sourceDir.startsWith("/data/app/") && packageInfo.packageName.toLowerCase().contains("widget") == false && packageInfo.packageName.equals("com.adrorodri.control") == false && packageInfo.packageName.equals("com.example.interceptor") == false) {
                ListaPackages.add(packageInfo.packageName);
                //Activities.add(pm.getLaunchIntentForPackage(packageInfo.packageName));
                ListaApps.add(String.valueOf(pm.getApplicationLabel(packageInfo)));
            }
        }
        lv = (ListView) findViewById(R.id.listView1);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String[] StringApps = new String[ListaPackages.size()];
        for (int i = 0; i < ListaPackages.size(); i++) {
            StringApps[i] = ListaApps.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, StringApps);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(50);
                AppSeleccionada = ListaApps.get(position);
                PaqueteSeleccionado = ListaPackages.get(position);
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
                pos = position;
                mostrarAlertaUsuario();
                //lv.setSelection(position);
            }
        });
    }

    public void AceptarInicial(View view) {
        final Usersdb udb = new Usersdb(this);
        if (String.valueOf(et1.getText()).equals("")) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(150);
            Toast.makeText(getApplicationContext(), "Ingrese datos correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setContentView(R.layout.pass_main);
            TextView tv1 = (TextView) findViewById(R.id.textViewFirst);
            pass = (int) (Math.random() * 899999 + 100000);
            tv1.setText(String.valueOf(pass));
            Toast.makeText(getApplicationContext(), "Usuario y password agregados", Toast.LENGTH_SHORT).show();
            udb.addUser(new User(String.valueOf(et1.getText()), String.valueOf(pass), "ADMIN"));
            escribirArchivo();
            PackageManager p = getPackageManager();
            p.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    public void continuar(View view) {
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    public void sendEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Password Administrador de Control");
        intent.putExtra(Intent.EXTRA_TEXT, "El password es: " + pass);
        intent.setData(Uri.parse("mailto:" + et1.getText())); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    public void add1(View view) {
        et2.setText(String.valueOf(et2.getText()) + "1");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add2(View view) {
        et2.setText(String.valueOf(et2.getText()) + "2");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add3(View view) {
        et2.setText(String.valueOf(et2.getText()) + "3");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add4(View view) {
        et2.setText(String.valueOf(et2.getText()) + "4");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add5(View view) {
        et2.setText(String.valueOf(et2.getText()) + "5");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add6(View view) {
        et2.setText(String.valueOf(et2.getText()) + "6");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add7(View view) {
        et2.setText(String.valueOf(et2.getText()) + "7");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add8(View view) {
        et2.setText(String.valueOf(et2.getText()) + "8");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add9(View view) {
        et2.setText(String.valueOf(et2.getText()) + "9");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add0(View view) {
        et2.setText(String.valueOf(et2.getText()) + "0");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            setTrueFalse(ListaApps.get(pos), ListaPackages.get(pos));
            alertaUsuario.cancel();
        }
    }

    public void add11(View view) {
        et2.setText(String.valueOf(et2.getText()) + "1");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add12(View view) {
        et2.setText(String.valueOf(et2.getText()) + "2");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add13(View view) {
        et2.setText(String.valueOf(et2.getText()) + "3");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add14(View view) {
        et2.setText(String.valueOf(et2.getText()) + "4");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add15(View view) {
        et2.setText(String.valueOf(et2.getText()) + "5");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add16(View view) {
        et2.setText(String.valueOf(et2.getText()) + "6");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add17(View view) {
        et2.setText(String.valueOf(et2.getText()) + "7");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add18(View view) {
        et2.setText(String.valueOf(et2.getText()) + "8");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add19(View view) {
        et2.setText(String.valueOf(et2.getText()) + "9");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void add10(View view) {
        et2.setText(String.valueOf(et2.getText()) + "0");
        if (comprobar()) {
            Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            vib.vibrate(50);
            if (leerArchivo().equals("SI")) {
                System.out.println("valia SI cambia a NO");
                bt.setText("SERVICIO APAGADO");
                im.setImageResource(R.drawable.gradient_shape_red);
                line.setBackgroundColor(Color.RED);
                escribirArchivo("NO");
            } else {
                System.out.println("valia NO cambia a SI");
                bt.setText("SERVICIO INICIADO");
                im.setImageResource(R.drawable.gradient_shape_green);
                line.setBackgroundColor(Color.GREEN);
                escribirArchivo("SI");
            }
            alertaUsuario.cancel();
        }
    }

    public void deleteInput(View view) {
        if (String.valueOf(et2.getText()).length() > 0) {
            et2.setText(String.valueOf(et2.getText()).substring(0, String.valueOf(et2.getText()).length() - 1));
        }
    }

    public void CerrarPopup(View view) {
        alertaUsuario.cancel();
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(50);
        if (lv.isItemChecked(pos)) {
            lv.setItemChecked(pos, false);
        } else {
            lv.setItemChecked(pos, true);
        }
    }

    public void CerrarPopup1(View view) {
        alertaUsuario.cancel();
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(50);
    }

    protected void mostrarAlertaUsuario() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.login_popup, null);
        et2 = (EditText) view.findViewById(R.id.editText2);
        alertaUsuario = new AlertDialog.Builder(this).setTitle("Confirmar: ")/*.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
				if(comprobar())
				{
					setTrueFalse(ListaApps.get(pos),ListaPackages.get(pos));
				}
				else
				{
					if(lv.isItemChecked(pos))
					{
						lv.setItemChecked(pos,false);
					}
					else
					{
						lv.setItemChecked(pos,true);
					}
				}
			}
		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
				if(lv.isItemChecked(pos))
				{
					lv.setItemChecked(pos,false);
				}
				else
				{
					lv.setItemChecked(pos,true);
				}
		    }
		})*/.setView(view).setCancelable(false).create();
        alertaUsuario.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(50);
        switch (item.getItemId()) {
            case R.id.acercaDe:
                acercaDe();
                return true;
	        /*case R.id.anadirUsuario:
	        	anadirUsuario();
	        	return true;*/
            default: {
                return true;
            }
        }
    }

    /*private void anadirUsuario() {
        final Usersdb udb=new Usersdb(this);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.new_user_popup,null);
        final EditText et3= (EditText) view1.findViewById(R.id.editText1);
        final EditText et4 = (EditText) view1.findViewById(R.id.editText2);
        final CheckBox cb1 = (CheckBox) view1.findViewById(R.id.checkBox1);
        alertaUsuario=new AlertDialog.Builder(this).setTitle("Ingrese datos nuevos: ").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(50);
                if(cb1.isChecked())
                {
                    alertaUsuario.cancel();
                    alertaUsuario2.show();
                }
                else
                {
                    udb.addUser(new User(String.valueOf(et3.getText()),String.valueOf(et4.getText()),"GUEST"));
                    Toast.makeText(getApplicationContext(),"Usuario y password agregados",Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(50);
            }
        }).setView(view1).create();
        alertaUsuario.show();
        LayoutInflater inflater1 = getLayoutInflater();
        View view2 = inflater1.inflate(R.layout.login_popup,null);
        et2 = (EditText) view2.findViewById(R.id.editText2);
        alertaUsuario2=new AlertDialog.Builder(this).setTitle("Confirmar: ").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(50);
                if(comprobar())
                {
                    udb.addUser(new User(String.valueOf(et3.getText()),String.valueOf(et4.getText()),"ADMIN"));
                    Toast.makeText(getApplicationContext(),"Usuario y password agregados",Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(50);
            }
        }).setView(view2).setCancelable(false).create();
    }*/
    private void acercaDe() {
        AlertDialog acercaDe = new AlertDialog.Builder(this).setTitle("Acerca De:").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(50);
            }
        }).setMessage("Creado por: \nAdri�n Mauricio Rodr�guez Andrade").setView(null).create();
        acercaDe.show();
    }

    protected boolean comprobar() {
        Usersdb udb = new Usersdb(this);
		/*if(udb.exists(String.valueOf(et1.getText())))
		{*/
        if (udb.exists(String.valueOf(et2.getText()))) {
				/*if(udb.isAdmin(String.valueOf(et1.getText())))
				{*/
            Toast.makeText(getApplicationContext(), "Password ingresado correctamente", Toast.LENGTH_SHORT).show();
            return true;
				/*}
				else
				{
					Toast.makeText(getApplicationContext(),"El usuario no tiene privilegios",Toast.LENGTH_SHORT).show();
					return false;
				}*/
        } else {
            //Toast.makeText(getApplicationContext(),"Usuario o password incorrecto",Toast.LENGTH_SHORT).show();
            return false;
        }
		/*}
		else
		{
			Toast.makeText(getApplicationContext(),"Usuario o password incorrecto",Toast.LENGTH_SHORT).show();
			return false;
		}*/
    }

    protected void infopopup() {
        info = new AlertDialog.Builder(this).setTitle("Informaci�n de " + AppSeleccionada).setMessage("Nombre de paquete: \n" + PaqueteSeleccionado).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                vib.vibrate(50);
            }
        })/*.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
		    }
		})*/.create();
        info.show();
    }

    @Override
    public void onPause() {
        super.onDestroy();
        System.out.println("PAUSE");
    }

    @Override
    public void onStop() {
        super.onDestroy();
        finish();
        System.out.println("STOP");
        if (leerArchivo().equals("SI")) {
            if (isMyServiceRunning()) {
                System.out.println("El sevicio ya esta corriendo");
                stopService(new Intent(this, Interceptor.class));
                System.out.println("El sevicio se paro");
                startService(new Intent(this, Interceptor.class));
                System.out.println("El sevicio comenz� a correr ahora");
            } else {
                System.out.println("El sevicio comenz� a correr ahora");
                startService(new Intent(this, Interceptor.class));
            }
        }
        registrar();
    }

    public void registrar() {
        Intent intent = new Intent();
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("DESTROY");
    }

    private void mostrarBloqueados(ArrayList<String> ListaPackages) {
        Appsdb db = new Appsdb(this);
        List<View> listaViews = new ArrayList<View>();
        for (int i = 0; i < ListaPackages.size(); i++) {
            listaViews.add(lv.getChildAt(i));
        }
        try {
            for (int i = 0; i < listaViews.size(); i++) {
                if (db.exists(ListaPackages.get(i))) {
                    lv.setItemChecked(i, true);
                    //listaViews.get(i).setBackgroundColor(Color.GREEN);
                } else {
                    lv.setItemChecked(i, false);
                    //listaViews.get(i).setBackgroundColor(Color.RED);
                }
            }
        } catch (NullPointerException npe) {
            Toast.makeText(getApplicationContext(), "Error fuck", Toast.LENGTH_SHORT).show();
        }
    }

    protected void setTrueFalse(String name, String packagename) {
        Appsdb db = new Appsdb(this);
        if (db.exists(packagename)) {
            Toast.makeText(getApplicationContext(), "Eliminando: " + name, Toast.LENGTH_SHORT).show();
            db.deleteApp(new Aplicacion(name, packagename));
        } else {
            Toast.makeText(getApplicationContext(), "A�adiendo: " + name, Toast.LENGTH_SHORT).show();
            db.addApp(new Aplicacion(name, packagename));
        }
        db.close();
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

    /*public void Cerrar (View view)
    {
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(50);
        Intent showOptions = new Intent(Intent.ACTION_MAIN);
        showOptions.addCategory(Intent.CATEGORY_HOME);
        startActivity(showOptions);
        //startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER));
    }*/
    public void AbrirOpciones(View view) {
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(50);
        openOptionsMenu();
        //startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER));
    }

    public void Toggle(View view) {
        Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        vib.vibrate(50);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.login_popup_toggle, null);
        et2 = (EditText) view1.findViewById(R.id.editText2);
        alertaUsuario = new AlertDialog.Builder(this).setTitle("Ingrese sus datos: ")/*.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
				if(comprobar())
				{
					if(leerArchivo().equals("SI"))
					{
						System.out.println("valia SI cambia a NO");
						bt.setText("SERVICIO APAGADO");
						im.setImageResource(R.drawable.gradient_shape_red);
						line.setBackgroundColor(Color.RED);
						escribirArchivo("NO");
					}
					else
					{
						System.out.println("valia NO cambia a SI");
						bt.setText("SERVICIO INICIADO");
						im.setImageResource(R.drawable.gradient_shape_green);
						line.setBackgroundColor(Color.GREEN);
						escribirArchivo("SI");
					}
				}
			}
		}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Vibrator vib = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
				vib.vibrate(50);
		    }
		})*/.setView(view1).setCancelable(false).create();
        alertaUsuario.show();
    }

    public boolean escribirArchivo() {
        String nomarchivo = "Settings.txt";
        String contenido = "NO";
        try {
            FileOutputStream fOut = openFileOutput(nomarchivo, MODE_WORLD_WRITEABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(contenido);
            osw.flush();
            osw.close();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public boolean escribirArchivo(String truefalse) {
        String nomarchivo = "Settings.txt";
        String contenido = truefalse;
        try {
            FileOutputStream fOut = openFileOutput(nomarchivo, MODE_WORLD_WRITEABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(contenido);
            osw.flush();
            osw.close();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public String leerArchivo() {
        String nomarchivo = "Settings.txt";
        StringBuffer outStringBuf = new StringBuffer();
        String inputLine = "";
        try {
            FileInputStream fIn = getApplicationContext().openFileInput(nomarchivo);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader inBuff = new BufferedReader(isr);
            while ((inputLine = inBuff.readLine()) != null) {
                outStringBuf.append(inputLine);
                outStringBuf.append("\n");
            }
            inBuff.close();
            String[] Data = outStringBuf.toString().split("\n");
            String truefalse = Data[0];
            return truefalse;
        } catch (IOException e) {
            return "ERROR";
        }
    }
}
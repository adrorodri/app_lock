package com.adrorodri.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

		     String number = getResultData();   
		     if (number!=null) 
		     {
		        if(number.equals("*2668765")){

		             setResultData(null);
		             System.out.println("SE DEBERIA ABRIR");
		             Intent newintent = new Intent(context,Advertencia.class);
		             newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		             context.startActivity(newintent);
		        }
	        }
	    }
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			System.out.println("BOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOTTTTTTTTTTTTTTTTTTTTTTTTTT CCCCCCCOOOOOOOOOOOOOOOOOMMMMMMMMMMPPPPPPPPPPPPPPPPPPLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEETTTTTTTTTTTTTTTEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEDDDDDDDDDDDDDDDDDDDDDDD");
			String nomarchivo = "Settings.txt";
	        StringBuffer outStringBuf = new StringBuffer();
	        String inputLine = "";
	        String truefalse="NO";
	        try
	        {
	        	FileInputStream fIn = context.openFileInput(nomarchivo);
	            InputStreamReader isr = new InputStreamReader(fIn);
	            BufferedReader inBuff = new BufferedReader(isr);
	            while ((inputLine = inBuff.readLine()) != null) {
	                outStringBuf.append(inputLine);
	                outStringBuf.append("\n");
	            }
	            inBuff.close();
	            String [] Data=outStringBuf.toString().split("\n");
	            truefalse=Data[0];
	        } 
	        catch (IOException e)
	        {
	        	System.out.println("Error al leer archivo de preferencias");
	        }
			if(truefalse.equals("SI"))
	        {
				System.out.println("El sevicio comenz— a correr ahora");
				context.startService(new Intent(context, Interceptor.class));
	        }
	    }
	}
}

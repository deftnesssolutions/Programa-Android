package br.com.crudsqlliteandroid.UI;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class prevendaUI extends Activity implements OnClickListener{
	public String msg="Pressione o botão Sair para fechar a prevenda";
	public Integer idVendedor;//codigo de vendedor
	public String nomeVendedor;//nombre del vendedor
	TextView tvVendedor,tvData;
	java.util.Date noteTS ; 
	public String data,tempo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		chamaPrincipal();
	}

	@Override
	public void onClick(View v) {
		
		
	}
	
	//Metodo que puede ser llamado desde cualquier punto, este metodo abre el layout principal de prevenda
	//ya que desde este mismo programa van ser llamdo varios layout como o cartao,cliente, consumo entre otros
	public void chamaPrincipal()
	{
		setContentView(R.layout.prevenda);
		//recuperamos el id y el nombre del vendor que llamo la prevenda
		Intent extras= getIntent();
		if(extras != null)
		{
			Bundle recibirIDNombre= extras.getExtras();
			if(recibirIDNombre!=null)
			{
				idVendedor= recibirIDNombre.getInt("idVendedor");
				nomeVendedor=recibirIDNombre.getString("nome");
			}
		}
		
		tvVendedor=(TextView)findViewById(R.id.tvVendedor);
		tvVendedor.setText(nomeVendedor);
		tvData=(TextView)findViewById(R.id.tvData);
		//O tread é o encargado de atualizar o date time no layout 
		Thread t = new Thread ()  {

	        @Override 
	        public  void run ()  { 
	            try  { 
	                while  (! isInterrupted ())  { 
	                    Thread . sleep ( 1000 ); 
	                    runOnUiThread ( new  Runnable ()  { 
	                        @Override 
	                        public  void run ()  { 
	                            updateTextView (); 
	                        } 
	                    }); 
	                } 
	            }  catch  ( InterruptedException e )  { 
	            } 
	        } 
	    };

	    t.start();
	}
	
	//Este metodo es llamdo en el Thread que estara actualizando el date time en el layout principal en el campo que mustra
	//la fecha y hora 
	public  void updateTextView ()  { 
	    noteTS =  Calendar . getInstance (). getTime ();

	    tempo =  "dd/MM/yyyy hh:mm:ss" ;  // 00:00 
	    tvData . setText ( DateFormat . format ( tempo , noteTS ));

	    //data =  "aaaa dd MMMMM" ;  // 01 de janeiro de 2013 
	    //tvDate . setText ( DateFormat . formato ( data , noteTS )); 
	}
}

package com.example.prevendaandroid;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

/*###
 *  Autor: Gustavo Tomas Ovelar Benítez
 	 Analista Programador
 	 
 	Esta clase se encarga presentar um Splash Screen con un progressBar
 	para darle un toque profesional al acceso al aplicatico
 	
 	Utiliza una clase interna que extiende del AsyncTask que es el que posibilita la ejecucion de tareas
 	en segundo plano y exibe en pantalla un progressbar, en este caso un circular...
 	Buendo qualquier programador que llegue hasta aqui analizando las lineas de codigo va entender lo que esta pasando
 	
 	Esta tela o pantalla se encarga tambien de pasar algunos datos al pantalla principal 
 	tales como el id, el nombre del vendedor y el ip del servidor
 	Para que esta tela funcione debe existir en la caperta layout un xml llamado barra_cargado, en la carpeta menu un xml llamado barra_cargado
 	y en la carpe values en el xml llamado string la linea <string name="title_activity_barra_cargado">Barra_cargado</string>,
 	tambian es fundamental y obligatorio crear un activity en el AndroidManifies.xml
 	<activity
            android:name="com.example.creativeprevenda.Barra_cargado"
            android:label="@string/app_name"
            android:screenOrientation = "landscape" >
            <intent-filter>
                <action android:name="android.intent.action.Barra_cargado" />

                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
      </activity>
      y las permisiones correspondientes para conexion 
      <uses-permission android:name="android.permission.VIBRATE"/>  
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    sin todo esto no va funcionar, recordando que esta pantalla es accionado en el boton acessar de la tela 
    de inicio de logueo de vendedores 
 	
 ###*/
public class Barra_cargado extends Activity {

	public int idVendedor;
	public String nomVendedor;
	public String Servidor;
	int progreso;//Guardará un numero entero que será el numero de segundos que ha pasado desde que hemos inicializado la aplicación.
    ProgressBar barraProgreso;//Declaración de la barra de progreso.
    int cantPorcent=1;
    TextView txtPorcentaje;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barra_cargado);
		
		Intent extras= getIntent();
		if(extras != null)
		{
			Bundle recibirIDNombre= extras.getExtras();
			if(recibirIDNombre!=null)
			{
				idVendedor= recibirIDNombre.getInt("idKey");
				nomVendedor=recibirIDNombre.getString("nombreKey");
				Servidor=recibirIDNombre.getString("serverKey");
			}
		}
		
		 barraProgreso = (ProgressBar)findViewById(R.id.progressBar1);//Relacionamos el la barra de progreso del layout con el de java
	        new TareaSegundoPlano().execute();//Iniciamos la tarea en segundo plano, en este caso no necesitamos pasarle nada.
	
	        
	}

	
	  //Clase interna que extiende de AsyncTask
 public class TareaSegundoPlano extends AsyncTask<Void, Void, Void>{

     //Método que se ejecutará antes de la tarea en segundo plano, normalmente utilizado para iniciar el entorno gráfico
     protected void onPreExecute(){           
			barraProgreso.setProgress(0);//Ponemos la barra de progreso a 0
         barraProgreso.setMax(10);//El máximo de la barra de progreso son 10, de los 10 segundo que va a durar la tarea en segundo plano.
     }

     //Este método se ejecutará después y será el que ejecute el código en segundo plano
     @Override
     protected Void doInBackground(Void... params) {
         for(int progreso = 1;progreso<=10;progreso++){//Creamos un for de 1 a 10 que irá contando los segundos.
         	
             try {
                     Thread.sleep(1000);//Esto lo que hace es ralentizar este proceso un segundo (el tiempo que se pone entre paréntesis es en milisegundos) tiene que ir entre try y catch
                    
             } catch (InterruptedException e) {}
            
             publishProgress();//Actualizamos el progreso, es decir al llamar a este proceso en realidad estamos llamamos al método onProgresssUpdate()
         }
         
         
             return null;//Al llegar aquí, no devolvemos nada y acaba la tarea en segundo plano y se llama al método onPostExecute().
         }
  
         protected void onProgressUpdate(Void... values) {
             barraProgreso.setProgress(progreso);//Actualizamos la barra de progreso con los segundos que vayan.
         }
  
         protected void onPostExecute(Void result){//A este método se le llama cada vez que termine la tarea en segundo plano.
             
      	   //Toast.makeText(getBaseContext(), "Tarea Finalizada", Toast.LENGTH_LONG).show();//Nos muestra una notificación informando de que la tarea en segundo plano ha finalizado
      	 // ENVIA Al otro activity
				Intent intent=new Intent("android.intent.action.Menu_principal");
				
				intent.putExtra("idKey", idVendedor);
				intent.putExtra("nombreKey", nomVendedor);
				intent.putExtra("serverKey", Servidor);
				startActivityForResult(intent, 1);
				finish();
         } 
	
 }
	
 
 //Definimos que para cuando se presione la tecla BACK no volvamos para atras  	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
	     if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	         // no hacemos nada.
	         return true;
	     }

	     return super.onKeyDown(keyCode, event);
	 }
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barra_cargado, menu);
		return true;
	}

}

package com.example.prevendaandroid;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*####
	Clase inicial de Acceso al aplicativo
	Esta clase realiza el gerenciamiento de la conexión al servidor y el acceso
	de los usuarios al aplicativo, en este caso los vendedores(Mozos o Garçonetes).
	Para la conexion al servidor el crea un archivo txt donde almacena la ip del servidor 
	donde se encuentra la base de datos gerenciado por un Web Service Asp.Net MVC3 y lo guarda 
	en el aparato donde se ejecuta el android.
	De esta forma queda la conexion dinamico, es decir si muda el ip del servido es solo editar el txt
	y cambiar la ip
	Autor:Gustavo Tomas Ovelar Benítez
	Analista Programador
//###*/
@TargetApi(Build.VERSION_CODES.GINGERBREAD) public class MainActivity extends Activity
{
	 Button btnIngresar,btnIP,btnVerificarIP, btnExit;
	 EditText txtCodigo,txtIP;
	 TextView lblResultado;
	 //public String Servidor="10.0.2.2";//:49702";
	 //public String Servidor="192.168.25.190/RestService";
	 public String Servidor;
	 private final static String FILE = "servidor.txt";
	 private final static String TAG = "MSPantanal";
	 public static boolean ipOk=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        chamaMain();
        establecerIPServidor();
        
    }
    
    //Cuadro de mensaje personalizado para utilizar en toda la aplicación
    public void mensagemExibir(String titulo, String texto)
    {
    	AlertDialog.Builder mensagem = new AlertDialog.Builder(MainActivity.this);
    	mensagem.setTitle(titulo);
    	mensagem.setMessage(texto);
    	mensagem.setNeutralButton("OK", null);
    	mensagem.show();
    	
    }
    
    //Establece el IP del servidor donde se encuentra el servidor Web para toda la aplicación
    private void establecerIPServidor() {
        // Comprobamos si existe el archivo
        if (existsFile(FILE)) {
            // En el caso de que exista, intentamos recuperar el IP del servidor, si no
            // recupera, el archivo.txt estaba corrupto.
            if (!recuperarIP()) {
                // Avisamos al usuario de que el archivo era corrupto.
                Toast.makeText(MainActivity.this,
                        "Archivo corrupto, reiniciando parámetros...",
                        Toast.LENGTH_LONG).show();
                // Creamos de nuevo el archivo.
                chamaCargarIP();
            }
            // Recuperamos el valor asignado por como IP
            // en el archvio txt.
            recuperarIP();
        } else {
            // En el caso de que no existiera el archivo txt pedimos para ingresar el ip del servidor y creamos y recuperamos
            // en la variable Servidor.
            chamaCargarIP();  
        }
    }
    
  //Verifica si existe el archivo especificado en la variable FILE y que es pasado como argumento para fileName
    public boolean existsFile(String fileName) {
        for (String tmp : fileList()) {
            if (tmp.equals(fileName))
                return true;
        }
        return false;
    }
    
  //Recupera el IP del servidor guardado el un archivo
    private boolean recuperarIP() {
        try {
            // Creamos un objeto InputStreamReader, que será el que nos permita
            // leer el contenido del archivo de texto.
            InputStreamReader archivo = new InputStreamReader(
                    openFileInput(FILE));
            // Creamos un objeto buffer, en el que iremos almacenando el contenido
            // del archivo.
            BufferedReader br = new BufferedReader(archivo);
            //leemos la línea y escribimos el contenido en la
            // variable Servidor que declaramos bien arriba.
            Servidor = br.readLine();

            // Cerramos el flujo de lectura del archivo.
            br.close();
            return true;
 
        } catch (Exception e) {
            return false;
        }
    }
    
    
  /*###Metodo que abre la tela para ingresar ip del servidor
    este ocurre solo la primera vez en forma automatica y luego solo en el caso de cambiar
    la ip de servidor###*/
    public void chamaCargarIP()
    {
    	setContentView(R.layout.ingresar_ipservidor);
    	txtIP=(EditText)findViewById(R.id.txtIP);
    	btnIP=(Button)findViewById(R.id.btnIP);
    	
    	btnIP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(txtIP.getText().toString().equals(""))
				{
					mensagemExibir("Asignar IP Servidor", "Por favor, digite o IP do servidor");
					return;
				}
				Servidor= txtIP.getText().toString();
				
				crearArchivoIP();
				chamaMain();
			}
		});
    	
    }
    
  //Verifica si el IP del servidor esta respondiendo
    private void verificaConexao() {
   	 String ip=Servidor;
   	 //String ip="10.0.2.2";
   	 int puerto=80;
   	 //log(" socket " + ip + " " + puerto);
   	 try {
   		 Socket sk = new Socket(ip,puerto);
   		 //BufferedReader entrada = new BufferedReader(new InputStreamReader(sk.getInputStream()));
   		 //PrintWriter salida = new PrintWriter(new OutputStreamWriter(sk.getOutputStream()),true);
   		 //log("enviando...");
   		 	//salida.println("Hola Mundo");
   		 	//log("recibiendo ... " + entrada.readLine());
   		 	ipOk=true;
   		 	sk.close();
   		 	TareaWSObtener tarea= new TareaWSObtener();
			tarea.execute(txtCodigo.getText().toString());
   	 } catch (Exception e) {
   	    //log("error: " + e.toString());
   		mensagemExibir("Perda de comunicação com o servidor:", "Servidor indisponível no momento, por favor, verifique suas configurações de rede.");
   	 }
   }
    
    // Crea un archivo que guarda el IP del servidor
    private void crearArchivoIP() {
        try {
            // Creamos un objeto OutputStreamWriter, que será el que nos permita
            // escribir en el archivo de texto. Si el archivo no existía se creará
            // automáticamente.
            // La ruta en la que se creará el archivo será /ruta de nuestro programa/data/data/
            OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                    openFileOutput(FILE, Context.MODE_PRIVATE));
            // Escribimos el IP ingresado en el archivo.
            outSWMensaje.write(Servidor + "\n");
            // Cerramos el flujo de escritura del archivo, este paso es obligatorio,
            // de no hacerlo no se podrá acceder posteriormente al archivo.
            outSWMensaje.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
    
    //Abre la tele principal de login de vendedores
    @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi")
    public void chamaMain()
    {
    	setContentView(R.layout.activity_main);
    	btnIngresar=(Button)findViewById(R.id.btnIngresar);
    	btnVerificarIP=(Button)findViewById(R.id.btnVerificarIP);
    	btnExit=(Button)findViewById(R.id.btnExit);
        txtCodigo=(EditText)findViewById(R.id.txtCodigo);
        txtIP=(EditText)findViewById(R.id.txtIP);
        lblResultado = (TextView)findViewById(R.id.lblResultado);
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        
        btnIngresar.setOnClickListener(new OnClickListener()
        	{			
				@Override
				public void onClick(View v)
				{
					if(txtCodigo.getText().toString().equals(""))
					{
						mensagemExibir("Acessar", "Por favor, digite seu codigo de vendedor");
						return;
					}					
					verificaConexao();					
				}
        	}
        );
        
        btnVerificarIP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chamaCargarIP();
				txtIP.setText(Servidor);
				
			}
		});
        
        btnExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.exit(0);
				
			}
		});
        
    }	 
   	  /*private void log(String string) {
   	     output.append(string + "\n");
   	  }*/
    
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
  //Tarea Asíncrona para llamar al WS de consulta de vendedores en segundo plano
  	private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {
  		
  		private int idVend;
  		private String nomVend;
  		 
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	
  	    	boolean resul = true;
  	    	
  	    	HttpClient httpClient = new DefaultHttpClient();
  	        
  			String id = params[0];
  			
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Vendedores/Vendedor/" + id); 			
  			del.setHeader("content-type", "application/json");
  			//AcessoBD bd= new AcessoBD();
  			
  			try
  	        {			
  	        	HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	
  	        	JSONObject respJSON = new JSONObject(respStr);
  	        	
  	        	 idVend = respJSON.getInt("idvendedor");
  	             nomVend = respJSON.getString("nome");
  	        	  
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        }
  	 
  	        return resul;
  	    }
  	    
  	    protected void onPostExecute(Boolean result) {
  	    	
  	    	if (result)
  	    	{
  	    		lblResultado.setText("" + idVend + "-" + nomVend );
  	    		//envia al otro activity
				Intent intent=new Intent("android.intent.action.Barra_cargado");
				intent.putExtra("idKey", idVend);
				intent.putExtra("nombreKey", nomVend);
				intent.putExtra("serverKey", Servidor);
				//startActivity(intent);
				startActivityForResult(intent, 1);
				finish();
  	    	}
  	    	if(!result)
  	    	{
  	    		mensagemExibir("Código do vendedor incorreto:", "Vendedor não encontrado, por favor, verifique.");
  	    	}
  	    }
  	}
  	
}


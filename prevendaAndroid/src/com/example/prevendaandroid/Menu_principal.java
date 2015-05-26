package com.example.prevendaandroid;

import java.util.Calendar;
import java.util.Date;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/*##########
 	Prevenda para restaurante
 	Autor: Gustavo Ovelar
 	
 	Bom
 Esta clase es el motor del aplicavo, todas operaciones es realizado desde esta, por eso voy a tratar de agrupar 
 o dividir para poder dejar los mas claro posible
 
 Observando el layout que es el menu_principal.xml que ya creamos anteriomente, ahi tenemos el nombre del vendedor
 ese obtenemos gracia a la bondad generosa del Splash Screen barra_cargado.java, el se encargo de pasar el id del vendedor,
 el nombre y el ip del servidor para realizar las requisiciones del banco atraver del web service.
 El resto de las aplicaciones voy a dar en cada bloque de los codigo
 ######*/

public class Menu_principal extends Activity implements OnClickListener
{
	
	int passo=1;//incremento de nro de carton
	public String nroCartao="Não Definido";
	public String idConsumo="0";
	public int itemOrdem;
	public String ultimaLinha;
	public String tipoAR;
	public String descComplemento;
	public String login="F";//despues de login coloca verdadero
	public Integer idVendedor;//codigo de vendedor
	public String nomeVendedor;//nombre del vendedor
	public String nomeCliente="";
	public int codigo;//codigo de produto
	public String descricao;//descripción del produto
	public String unitario;
	public String codBarras;
	public String produtoPermiteAlterar;
	public String itemEliminar="";
	public String quantidad;
	public int enter=0;//coloca entre 1 y 0 para codigo de produto y quantidad
	public boolean abierto=false;
	private ListView lvConsumo;
	public Consumo[] alConsumo; 
	public String data,tempo;
	public String Servidor;
	public int totItem=0;
	public String msg="Pressione o botão Sair para fechar o aplicativo";
	public static boolean resdialog = false;
	java.util.Date noteTS ; 
	private static String[] lstProdutos;
    //@SuppressWarnings("unused")
	//private static ArrayList<String> lstProdutos_Encontrados = new ArrayList<String>();
    public static ArrayAdapter<String> adapterProduto;
    public String[] mesaOcupadas;
    
    BotonPrevenda[] botPrevenda;
	EditText etNro,etQuant;
	Boton[] bot;
	TextView tvCartao,tvVendedor,tvData,tvDescricao,tvtotal,tvItem,tvDesc,tvCliente;
	Button btnCartao,btnConsumo,btnSair,btnRetornar,btnIngresar,btnEnter,btnConfirmar, btnCancelar,btnClose, btnCancelConsumo,btnSumar,btnRestar, btnOk, btnCancel;
	EditText txtCodigo, etProduto,etAdicionar,etRemover,etProcurar,etNomeCliente;
	ListView lvProduto;
	ImageButton imgVoltar;
   
    /*##CLASES INTERNA
 	 Boton: Esta clase fue necesario para dar fucionalidad al layout MESA O CARTAO, esa pantalla exibe 53 cajas o numeros
 	mas un boton + y un boton -, que pasa si el negocio restaurante o lo que sea tiene 100 o 1000 mesas
 	para en ese caso el usuario solo clica el + y ya tiene otro 53 mas por encima de lo que ya tenia y vice versa
 	toda esa funcionalidad es gracias a esta clase y el metodo findButtons(), la clase proporciona el boton y el metodo
 	lo acumula en un vector, y tambien mediante esa clase el aplicativo sabe cual boton fue accionado 
 	
 	BotonPrevenda: Proporciona funcionalidad al Layout que permite ingresar el codigo del producto es un panel numerico proprio
 	de esa pantalla solamente para facilitar la vida del usuario
 	
  	adapterConsumo:
  	Esta clase extiende de ArrayAdapter y se encarga de dar funcionalidad al la Lista de consumo ListView
  	es el que facilita la exibición de datos en la lista y en el layout, para ello fue creado un layout llamado lista_personal.xml
  	para poder manipular los datos exibidos en la lista ya que esta herramiente no tiene las funcionalidades que tiene 
  	otro ambiente o lenguage de programacion
	##*/
	class Boton
	{
		private Button bot;
		private int valor;
		private TextView tvCartao;
		
		
		
		public void setValor(int valor)
		{
			this.valor=valor;
		}
		
		public void settvCartao(TextView tvCartao)
		{
			this.tvCartao=tvCartao;
		}
		
		public Button getBot()
		{
			return this.bot;
		}
		
		public TextView getTexto()
		{
			return this.tvCartao;
		}
		
		public int getValor()
		{
			return this.valor;
		}
		
		public TextView gettvCartao()
		{
			return this.gettvCartao();
		}
		
		public Boton(Button bot)
		{
			this.bot= bot;
			this.valor=0;
			
			this.bot.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					
					 if (v.getId() == R.id.botMas)
					 {
						 Button btnultimo= (Button)findViewById(R.id.bot53);
						 
						 int nrofin= Integer.parseInt(btnultimo.getText().toString());
						 Boton[] bot=  findButtons();
						 for(int i=0;i<=52;i++)
						 {
							 bot[i].getBot().setText(Integer.toString(nrofin));
							 bot[i].getBot().setBackgroundColor(getResources().getColor(R.color.verde));
							 nrofin++;
						 }
						 passo++;
						 TareaWSListarMesasOcupadas tarea= new TareaWSListarMesasOcupadas();
						 tarea.execute();
				     }
					 
					 if(v.getId() == R.id.botMenos)
					 {
						 if(passo >1)
						 {
							 Button btnprimero= (Button)findViewById(R.id.bot1);
							 
							 int nrofin= Integer.parseInt(btnprimero.getText().toString());
							 Boton[] bot=  findButtons();
							 for(int i=52;i>=0;i--)
							 {
								 
								 bot[i].getBot().setText(Integer.toString(nrofin));
								 bot[i].getBot().setBackgroundColor(getResources().getColor(R.color.verde));
								 nrofin--;
							 }
							 passo--;
							 TareaWSListarMesasOcupadas tarea= new TareaWSListarMesasOcupadas();
							 tarea.execute();
						 }
					 }
					 
					 if(v.getId() == R.id.botSair)
					 {
						 chamaPrincipal();						 
					 }
					 if(v.getId()!= R.id.botSair && v.getId()!=R.id.botMas && v.getId()!=R.id.botMenos)
					 {
						 nroCartao=getBot().getText().toString();										 
						 chamaPrincipal();
					 }
				}
			});
		}	
	};
	 
	class BotonPrevenda
	{
		private Button botPrevenda;
		private int valor;
		private EditText etNro;
		
		public void setValor(int valor)
		{
			this.valor=valor;
		}
		
		public void setetNro(EditText etNro)
		{
			this.etNro=etNro;
		}
		
		public Button getBot()
		{
			return this.botPrevenda;
		}
		
		public EditText getTexto()
		{
			return this.etNro;
		}
		
		public int getValor()
		{
			return this.valor;
		}
		
		public TextView getetNro()
		{
			return this.getetNro();
		}
		
		public BotonPrevenda(Button botPrevenda)
		{
			this.botPrevenda= botPrevenda;
			this.valor=0;
			
			this.botPrevenda.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(enter==0)
					 {
						EditText etnro=(EditText)findViewById(R.id.etProduto);
						String Nros;
						if (v.getId() != R.id.botApagar && v.getId() != R.id.botEnter)
						{
							Nros= etnro.getText().toString();
							etnro.setText(Nros + getBot().getText().toString());					    
						}
					 
						if(v.getId() == R.id.botApagar)
						{
							Nros= etnro.getText().toString();
							if(Nros.length()>0)
							{
								Nros=Nros.substring(0,Nros.length()-1);
								etnro.setText(Nros);
							}
						}
					 
						if(v.getId()==R.id.botEnter)
						{	
							if(etnro.length()<=0)
							{
								chamaPesquisa();
							}
							else
							{
								TareaWSObtenerProduto tarea= new TareaWSObtenerProduto();
								tarea.execute(etProduto.getText().toString());
							}
						}					  
				  }
					if(enter==1)
					 {
						EditText etnro=(EditText)findViewById(R.id.etQuantidade);
						String Nros;
						if (v.getId() != R.id.botApagar && v.getId() != R.id.botEnter)
						{
							Nros= etnro.getText().toString();
							etnro.setText(Nros + getBot().getText().toString());					    
						}					 
						if(v.getId() == R.id.botApagar)
						{
							Nros= etnro.getText().toString();
							if(Nros.length()>0)
							{
								Nros=Nros.substring(0,Nros.length()-1);
								etnro.setText(Nros);
							}
						}					 
										  
				  }
				}
			});
		}
		
		
	};
	
	class adapterConsumo extends ArrayAdapter<Consumo>
	{
		Menu_principal context;
		public adapterConsumo (Menu_principal context)
		{
			super(context,R.layout.listapersonal,alConsumo);
			this.context = context;
		}
		
		public View getView(int position,View view, ViewGroup parent)
		{
			LayoutInflater inflater= context.getLayoutInflater();
			View item= inflater.inflate(R.layout.listapersonal, null);
			
			TextView tvCodigo= (TextView)item.findViewById(R.id.txtCod);
			//TextView tvBarra= (TextView)item.findViewById(R.id.txtBarra);
			TextView tvOrdem= (TextView)item.findViewById(R.id.txtOrdem);
			TextView tvDescricao= (TextView)item.findViewById(R.id.txtDescricao);
			TextView tvQuantidade= (TextView)item.findViewById(R.id.txtQuantidade);
			TextView tvUnitario= (TextView)item.findViewById(R.id.txtUnitario);
			TextView tvTotal= (TextView)item.findViewById(R.id.txtTotal);
			TextView tvSituacao= (TextView)item.findViewById(R.id.txtSituacao);
			
			
			//tvBarra.setText(alConsumo[position].getProdutoBarra());
			tvOrdem.setText(alConsumo[position].getProdutoOrdem());
			tvCodigo.setText(alConsumo[position].getProdutoCodigo());
			tvDescricao.setText(alConsumo[position].getDescricao());
			tvQuantidade.setText(alConsumo[position].getProdutoQuantidade());
			tvUnitario.setText(alConsumo[position].getProdutoUnitario());
			tvTotal.setText(alConsumo[position].getTotal());
			tvSituacao.setText(alConsumo[position].getProdutoSituacao());
			/*if(tvSituacao.getText().toString().equals("Normal"))
			{
				totItem=totItem+1;
			}*/
			
			if(tvSituacao.getText().toString().equals("Cancelado"))
			{
				tvCodigo.setTextColor(Color.RED);
				//tvBarra.setTextColor(Color.RED);
				tvOrdem.setTextColor(Color.RED);
				tvDescricao.setTextColor(Color.RED);
				tvQuantidade.setTextColor(Color.RED);
				tvUnitario.setTextColor(Color.RED);
				tvTotal.setTextColor(Color.RED);
				tvSituacao.setTextColor(Color.RED);
			}		
			return item;
		}
	}
	//## FIN CLASES INTERNA
	
	//###METODO PROTEGIDO QUE SE ENCARGA DE ACCIONAR Y MOSTRAL LA PANTALLA 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chamaPrincipal();
        //setContentView(R.layout.activity_main);
        
    }
    //##FIN METODO PROTEGIDO
    
    //##METODOS PUBLICOS 
    @Override 
    public  void onBackPressed ()  {// Bloqueo el boton back do tablet 
    // fazer algo nas costas. 
    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    	return ; 
    }
    //Otra forma de bloquear boton
    /*@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch(keyCode){
			case KeyEvent.KEYCODE_BACK:
				Toast.makeText(this, "Atras presionado",
                                                        Toast.LENGTH_SHORT).show();
				return true;
			case KeyEvent.KEYCODE_HOME:
				Toast.makeText(this, "HOME presionado",
                                                        Toast.LENGTH_SHORT).show();
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				Toast.makeText(this, "Boton de Volumen Down presionado",
                                                        Toast.LENGTH_SHORT).show();
		}
		return super.onKeyDown(keyCode, event);
	}*/  
    @Override
	public void onClick(View v) {
    	
		if(v.getId()==R.id.btncartao)
		{
			
			chamaCartao();
			
		}
		
		if(v.getId()==R.id.btnConsumo)
		{
			if(nroCartao=="Não Definido")
			{
				mensagemExibir("Consumo", "Por favor, informe o Cartão/Mesa para o novo consumo.");
				return;
			}
			chamaItemprevenda();
		}
		
		if(v.getId()==R.id.botSair)
			 
			chamaPrincipal();
		
		if(v.getId()==R.id.botRetornar)
			chamaPrincipal();
		    enter=0;
		
		if(v.getId()==R.id.btnConfirmar)
		{
			if(etProduto.getText().toString().equals(""))
			{
				mensagemExibir("Consumo", "Por favor, selecione um produto para confirmar.");
				etProduto.requestFocus();
				return;
			}
			if(etQuant.getText().toString().equals(""))
			{
				mensagemExibir("Consumo", "Ingrese a qunatidade");
				etQuant.requestFocus();
				return;
			}
			
			itemOrdem ++;
			TareaWSInsertarItemConsumo tarea = new TareaWSInsertarItemConsumo();
			tarea.execute(etQuant.getText().toString());	
		}
		
		if(v.getId()==R.id.btnCancelar)
		{ 
	        if(itemEliminar.equals(""))
	        {
	            //Si no había elementos seleccionados...
	        	mensagemExibir("Cancelar item", "Selecione um item para cancelar.");
	        	return;
	        }
	        if(alConsumo.length<2)
	        {
	        	//Si nao tem mas de un elemento nao permite eliminar
	        	mensagemExibir("Cancelar item", "O consumo tem um único item, não foi possivel cancelar");
	        	return;
	        }
	        
	        int count = lvConsumo.getAdapter().getCount();
	        totItem=0;
	        for (int i = 0; i < count; i++) 
	        {
	            ViewGroup row = (ViewGroup) lvConsumo.getChildAt(i);
	           //CheckBox tvTest = (CheckBox) row.findViewById(R.id.check);
	            TextView situacao= (TextView)row.findViewById(R.id.txtSituacao);
	           //  Get your controls from this ViewGroup and perform your task on them =)        
	           if (situacao.getText().toString().equals("Normal"))
	           {
	               totItem++;
	           }
	                
	         }
	        if(totItem<2)
	        {
	        	//Si nao tem mas de un elemento nao permite eliminar
	        	mensagemExibir("Cancelar item", "O consumo tem um único item, não foi possivel cancelar");
	        	return;
	        }
	        
	          TareaWSCancelar tarea= new TareaWSCancelar();
	          tarea.execute();    
		}
		/*if(v.getId()==R.id.btnCancelConsumo)
		{ 
	        
			if(nroCartao.equals("Não Definido"))
	        {
	            //Si no había elementos seleccionados...
	        	mensagemExibir("Cancelar Consumo", "Não tem nenhuma mesa no contexto");
	        	return;
	        }
			alertDialog("Cancelar Consumo", "Tem certeza que deseja cancelar este Consumo?");
				
		}*/
		
		if(v.getId()==R.id.btnSumar)
		{ 
			int nro= Integer.parseInt(etQuant.getText().toString());
			nro= nro+1;
			etQuant.setText(Integer.toString(nro));
		}
		if(v.getId()==R.id.btnRestar)
		{ 
			int nro= Integer.parseInt(etQuant.getText().toString());
			if(nro>1)
			{
				nro= nro-1;
			}
			etQuant.setText(Integer.toString(nro));
		}
		if(v.getId()==R.id.btnOk)
		{ 
			nomeCliente=etNomeCliente.getText().toString();
			TareaWSInsertarPrevenda tarea= new TareaWSInsertarPrevenda();
			tarea.execute(nroCartao);
		}
		if(v.getId()==R.id.btnx)
		{ 
			nomeCliente=" ";
			TareaWSInsertarPrevenda tarea= new TareaWSInsertarPrevenda();
			tarea.execute(nroCartao);
		}
		
		if(v.getId()==R.id.btnClose)
		{ 
			System.exit(0);
		}
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
        
    }
    public Boton[] findButtons() {
		
		bot= new Boton[56];
        bot[0]= new 	Boton((Button)findViewById(R.id.bot1));
		bot[1]= new 	Boton((Button)findViewById(R.id.bot2));
		bot[2]= new 	Boton((Button)findViewById(R.id.bot3));
		bot[3]= new 	Boton((Button)findViewById(R.id.bot4));
		bot[4]= new 	Boton((Button)findViewById(R.id.bot5));
		bot[5]= new 	Boton((Button)findViewById(R.id.bot6));
		bot[6]= new 	Boton((Button)findViewById(R.id.bot7));
		bot[7]= new 	Boton((Button)findViewById(R.id.bot8));
		bot[8]= new 	Boton((Button)findViewById(R.id.bot9));
		bot[9]= new 	Boton((Button)findViewById(R.id.bot10));
		bot[10]= new 	Boton((Button)findViewById(R.id.bot11));
		bot[11]= new 	Boton((Button)findViewById(R.id.bot12));
		bot[12]= new 	Boton((Button)findViewById(R.id.bot13));
		bot[13]= new 	Boton((Button)findViewById(R.id.bot14));
		bot[14]= new 	Boton((Button)findViewById(R.id.bot15));
		bot[15]= new 	Boton((Button)findViewById(R.id.bot16));
		bot[16]= new 	Boton((Button)findViewById(R.id.bot17));
		bot[17]= new 	Boton((Button)findViewById(R.id.bot18));
		bot[18]= new 	Boton((Button)findViewById(R.id.bot19));
		bot[19]= new 	Boton((Button)findViewById(R.id.bot20));
		bot[20]= new 	Boton((Button)findViewById(R.id.bot21));
		bot[21]= new 	Boton((Button)findViewById(R.id.bot22));
		bot[22]= new 	Boton((Button)findViewById(R.id.bot23));
		bot[23]= new 	Boton((Button)findViewById(R.id.bot24));
		bot[24]= new 	Boton((Button)findViewById(R.id.bot25));
		bot[25]= new 	Boton((Button)findViewById(R.id.bot26));
		bot[26]= new 	Boton((Button)findViewById(R.id.bot27));
		bot[27]= new 	Boton((Button)findViewById(R.id.bot28));
		bot[28]= new 	Boton((Button)findViewById(R.id.bot29));
		bot[29]= new 	Boton((Button)findViewById(R.id.bot30));
		bot[30]= new 	Boton((Button)findViewById(R.id.bot31));
		bot[31]= new 	Boton((Button)findViewById(R.id.bot32));
		bot[32]= new 	Boton((Button)findViewById(R.id.bot33));
		bot[33]= new 	Boton((Button)findViewById(R.id.bot34));
		bot[34]= new 	Boton((Button)findViewById(R.id.bot35));
		bot[35]= new 	Boton((Button)findViewById(R.id.bot36));
		bot[36]= new 	Boton((Button)findViewById(R.id.bot37));
		bot[37]= new 	Boton((Button)findViewById(R.id.bot38));
		bot[38]= new 	Boton((Button)findViewById(R.id.bot39));
		bot[39]= new 	Boton((Button)findViewById(R.id.bot40));
		bot[40]= new 	Boton((Button)findViewById(R.id.bot41));
		bot[41]= new 	Boton((Button)findViewById(R.id.bot42));
		bot[42]= new 	Boton((Button)findViewById(R.id.bot43));
		bot[43]= new 	Boton((Button)findViewById(R.id.bot44));
		bot[44]= new 	Boton((Button)findViewById(R.id.bot45));
		bot[45]= new 	Boton((Button)findViewById(R.id.bot46));
		bot[46]= new 	Boton((Button)findViewById(R.id.bot47));
		bot[47]= new 	Boton((Button)findViewById(R.id.bot48));
		bot[48]= new 	Boton((Button)findViewById(R.id.bot49));
		bot[49]= new 	Boton((Button)findViewById(R.id.bot50));
		bot[50]= new 	Boton((Button)findViewById(R.id.bot51));
		bot[51]= new 	Boton((Button)findViewById(R.id.bot52));
		bot[52]= new 	Boton((Button)findViewById(R.id.bot53));
		bot[53]= new 	Boton((Button)findViewById(R.id.botSair));
		bot[54]= new 	Boton((Button)findViewById(R.id.botMas));
		bot[55]= new 	Boton((Button)findViewById(R.id.botMenos));
		return bot;
    } 
    public void chamaCartao()
    {
    	setContentView(R.layout.activity_cartao);
    	findButtons();
    	
    	
    	TareaWSListarMesasOcupadas tarea= new TareaWSListarMesasOcupadas();
		tarea.execute();
		
    	
    }
    public void chamaNomeCliente()
    {
    	setContentView(R.layout.inserir_nome_cliente);
 
    	btnOk = (Button)findViewById(R.id.btnOk);
    	btnOk.setOnClickListener(this);
    	btnCancel=(Button)findViewById(R.id.btnx);
    	btnCancel.setOnClickListener(this);
    	etNomeCliente=(EditText)findViewById(R.id.txtNomeCliente);	
    }
    public void chamaPesquisa()
    {
    	
    	setContentView(R.layout.activity_pesq_produto);
		
		 lvProduto = (ListView) findViewById(R.id.lvProduto);
        etProcurar = (EditText) findViewById(R.id.etProcurar);
        imgVoltar= (ImageButton)findViewById(R.id.imgVoltar);
        
        lvProduto.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        
        TareaWSListarProduto tarea = new TareaWSListarProduto();
        tarea.execute();
        lvProduto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				chamaItemprevenda();
				String itemLista;
				itemLista=adapterProduto.getItem(position);
				String coddesc[];
				coddesc= itemLista.split(" ");
				etProduto.setText(coddesc[0]);
				TareaWSObtenerProduto tarea= new TareaWSObtenerProduto();
				tarea.execute(etProduto.getText().toString());
				//enter=0;
				
				
			}
		});
        
        imgVoltar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chamaItemprevenda();
				
			}
		});
    }
    public void chamaItemprevenda()
    {
    	setContentView(R.layout.activity_itemprevenda);
    	
    	msg="Pressione o botão Retornar para voltar ao menu principal";
    	btnRetornar=(Button)findViewById(R.id.botRetornar);
        btnRetornar.setOnClickListener(this);
        btnConfirmar=(Button)findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(this);
        btnSumar=(Button)findViewById(R.id.btnSumar);
        btnSumar.setOnClickListener(this);
        btnRestar=(Button)findViewById(R.id.btnRestar);
        btnRestar.setOnClickListener(this);
        
        
        etNro=(EditText)findViewById(R.id.etProduto);
        tvDescricao=(TextView)findViewById(R.id.tvDescricao);
        etProduto=(EditText)findViewById(R.id.etProduto);
        etProduto.setOnClickListener(this);
        etQuant=(EditText)findViewById(R.id.etQuantidade);
        etAdicionar=(EditText)findViewById(R.id.etAdicionar);
        etRemover=(EditText)findViewById(R.id.etRemover);
		botPrevenda= new 	BotonPrevenda[12];
		botPrevenda[0]= new 	BotonPrevenda((Button)findViewById(R.id.bot0));
		botPrevenda[1]= new 	BotonPrevenda((Button)findViewById(R.id.bot1));
		botPrevenda[2]= new 	BotonPrevenda((Button)findViewById(R.id.bot2));
		botPrevenda[3]= new 	BotonPrevenda((Button)findViewById(R.id.bot3));
		botPrevenda[4]= new 	BotonPrevenda((Button)findViewById(R.id.bot4));
		botPrevenda[5]= new 	BotonPrevenda((Button)findViewById(R.id.bot5));
		botPrevenda[6]= new 	BotonPrevenda((Button)findViewById(R.id.bot6));
		botPrevenda[7]= new 	BotonPrevenda((Button)findViewById(R.id.bot7));
		botPrevenda[8]= new 	BotonPrevenda((Button)findViewById(R.id.bot8));
		botPrevenda[9]= new 	BotonPrevenda((Button)findViewById(R.id.bot9));
		botPrevenda[10]= new 	BotonPrevenda((Button)findViewById(R.id.botEnter));
		botPrevenda[11]= new 	BotonPrevenda((Button)findViewById(R.id.botApagar));
		
		etProduto.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				enter=0;
			}
		});
		
		etQuant.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				enter=1;
			}
		});
		
    }
    public void alertDialog(String titulo, String texto)
    {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Menu_principal.this);
		// Setting Dialog Title
		alertDialog.setTitle(titulo);

		// Setting Dialog Message
		alertDialog.setMessage(texto);

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int which) {
          //resdialog= true;
        	TareaWSCancelarConsumo tarea= new TareaWSCancelarConsumo();
			tarea.execute();
        }
		});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        
        	dialog.cancel();
        }
		});
		
		// Showing Alert Message
		alertDialog.show();
		
    }
	public void mensagemExibir(String titulo, String texto)
    {
    	AlertDialog.Builder mensagem = new AlertDialog.Builder(Menu_principal.this);
    	mensagem.setTitle(titulo);
    	mensagem.setMessage(texto);
    	mensagem.setNeutralButton("OK", null);
    	mensagem.show();
    	
    }
	public  void updateTextView ()  { 
	    noteTS =  Calendar . getInstance (). getTime ();

	    tempo =  "dd/MM/yyyy hh:mm:ss" ;  // 00:00 
	    tvData . setText ( DateFormat . format ( tempo , noteTS ));

	    //data =  "aaaa dd MMMMM" ;  // 01 de janeiro de 2013 
	    //tvDate . setText ( DateFormat . formato ( data , noteTS )); 
	}
	@SuppressLint("SimpleDateFormat") 
	public void chamaPrincipal()
	{
		
			setContentView(R.layout.activity_menu_principal);
			msg="Pressione o botão Sair para fechar o aplicativo";
			
			Intent extras= getIntent();
			if(extras != null)
			{
				Bundle recibirIDNombre= extras.getExtras();
				if(recibirIDNombre!=null)
				{
					idVendedor= recibirIDNombre.getInt("idKey");
					nomeVendedor=recibirIDNombre.getString("nombreKey");
					Servidor= recibirIDNombre.getString("serverKey");
				}
			}
			
			tvCartao=(TextView)findViewById(R.id.tvCartao);
			tvCliente=(TextView)findViewById(R.id.tvCliente);
			tvCliente.setText(nomeCliente);
			tvCartao.setText(nroCartao);
			tvVendedor=(TextView)findViewById(R.id.tvVendedor);
			tvVendedor.setText(nomeVendedor);
			tvtotal=(TextView)findViewById(R.id.tvTot);
			tvData=(TextView)findViewById(R.id.tvData);
			lvConsumo=(ListView)findViewById(R.id.lvConsumo);
			lvConsumo.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
	
			btnCartao=(Button)findViewById(R.id.btncartao);
			btnCartao.setOnClickListener(this);
			btnConsumo=(Button)findViewById(R.id.btnConsumo);
			btnConsumo.setOnClickListener(this);
			btnCancelar=(Button)findViewById(R.id.btnCancelar);
			btnCancelar.setOnClickListener(this);
			
			btnClose=(Button)findViewById(R.id.btnClose);
			btnClose.setOnClickListener(this);
			if(nroCartao !="Não Definido")
			{
				TareaWSEstado tarea= new TareaWSEstado();
				tarea.execute(nroCartao);
			}	
			
			lvConsumo.setOnItemClickListener(new OnItemClickListener() {
			    @Override
			    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
			        //Alternativa 1:
			        itemEliminar =((Consumo)a.getAdapter().getItem(position)).getProdutoOrdem();
			        //tvItem.setText(itemEliminar);
			        //tvDesc.setText(((Consumo)a.getAdapter().getItem(position)).getDescricao());
			        codigo= Integer.parseInt(((Consumo)a.getAdapter().getItem(position)).getProdutoCodigo());
			        codBarras=((Consumo)a.getAdapter().getItem(position)).getProdutoBarra();
			        quantidad=((Consumo)a.getAdapter().getItem(position)).getProdutoQuantidade();
			       
			    }

				
			});
			
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
	/*
	* O sistema pega a data e formata automaticamente para o formato
	* do banco de dados. E retorna a formatação.
	* O método funciona como campo Date.
	  */ 
	public Date DataBD()
	{
		java.util.Date d1 = new java.util.Date();  
		java.sql.Date d2 = new java.sql.Date(d1.getTime());
		return d2;
	}
    //##FIN METODOS PUBLICOS
	
	//##TAREAS ASINCRONICAS QUE SE ENCARGA DE LAS REQUISICIONES DE DATOS DEL BANCO ATRAVES DEL WEB SERVICE
	//Tarea Asíncrona para llamar al WS de consulta en segundo plano
  	private class TareaWSObtenerProduto extends AsyncTask<String,Integer,Boolean>
  	{
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	boolean resul = true;
  	    	HttpClient httpClient = new DefaultHttpClient();
  			String id = params[0];
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Produtos/Produto/" + id);
  			del.setHeader("content-type", "application/json");
  			try
  	        {			
  	        	HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	JSONObject respJSON = new JSONObject(respStr);	
  	        	codigo = respJSON.getInt("idproduto");
  	        	descricao = respJSON.getString("descricao");
  	        	unitario = respJSON.getString("preco_venda");
  	        	codBarras= respJSON.getString("codigobarras");
  	        	produtoPermiteAlterar= respJSON.getString("prodReqProdPermAlterar");
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        }  	 
  	        return resul;
  	    }
  	    protected void onPostExecute(Boolean result)
  	    {	
  	    	if (result)
  	    	{
  	    		tvDescricao.setText(descricao);
  	    		enter=1;
  	    		btnConfirmar.setEnabled(true);
  	    		if(produtoPermiteAlterar == "true")
  	    		{
  	    			etAdicionar.setEnabled(true);
  	    			etRemover.setEnabled(true);
  	    			etAdicionar.requestFocus();
  	    		}else{etQuant.requestFocus();}  	    		  	    		
  	    	}
  	    	if(!result)
  	    	{
  	    		mensagemExibir("Produto", "Produto não cadastrado, por favor, verifique.");
  	    	}
  	    }
  	}
	//Tarea Asíncrona para llamar al WS de consulta en segundo plano
  	private class TareaWSEstado extends AsyncTask<String,Void,Boolean>
  	{
  		//private boolean aberto;	  		 
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	boolean resul = true;
  	    	HttpClient httpClient = new DefaultHttpClient();
  			String cartao = params[0];
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevenda/Estado/" + cartao);
  			del.setHeader("content-type", "application/json");
  			try
  	        {			
  				HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	if(!respStr.equals("true"))
	        		resul = false;
  	        	//aberto=Boolean.getBoolean(respStr)	
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        }
  	        return resul;
  	    }
  	    protected void onPostExecute(Boolean result)
  	    {
  	    	if (result)
  	    	{ 	    		
  	    		TareaWSRetornaId tarea= new TareaWSRetornaId();
  				tarea.execute(nroCartao); 				
  	    	}
  	    	if(!result)
  	    	{
  	    		chamaNomeCliente();
  	    		
  	    	} 	    	
  	    }
  	}
  	//Tarea Asíncrona para llamar al WS de consulta en segundo plano
  	private class TareaWSTotal extends AsyncTask<String,Void,Boolean>
  	{
  		private String total;	  		 
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	boolean resul = true;
  	    	HttpClient httpClient = new DefaultHttpClient();
  			String id = params[0]; 			
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevenda/TotalConsumo/" + id); 			
  			del.setHeader("content-type", "application/json");
  			try
  	        {			  				
  				HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	total=respStr;	        		
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        } 	 
  	        return resul;
  	    }
  	    protected void onPostExecute(Boolean result)
  	    {	
  	    	if (result)
  	    	{
  	    		total=total.replace(".", ",");
  	    		tvtotal.setText("R$ " + total);
  	    		TareaWSUltimoNroOrdem ordem= new TareaWSUltimoNroOrdem();
	        	ordem.execute(idConsumo); 				
  	    	}    	
  	    }
  	}
  	//Tarea Asíncrona para llamar al WS de consulta en segundo plano
  	private class TareaWSRetornaId extends AsyncTask<String,Void,Boolean> 
  	{
  		private String idMesa;	  		 
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	boolean resul = true;
  	    	HttpClient httpClient = new DefaultHttpClient();
  			String id = params[0];
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevenda/IdConsumo/" + id);
  			del.setHeader("content-type", "application/json");
  			try
  	        {			
  				HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	idMesa=respStr;		
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        }
  	        return resul;
  	    }
  	    protected void onPostExecute(Boolean result)
  	    {   	
  	    	if (result)
  	    	{
  	    		idConsumo=idMesa;
  	    		TareaWSListarConsumo tareaConsumo= new TareaWSListarConsumo();
  				tareaConsumo.execute(nroCartao, idConsumo);	
  	    	}    	
  	    }
  	}
  	//Tarea Asíncrona para llamar al WS de consulta en segundo plano
  	private class TareaWSRetornaNome extends AsyncTask<String,Void,Boolean> 
  	{
  		private String Nome;	  		 
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	boolean resul = true;
  	    	HttpClient httpClient = new DefaultHttpClient();
  			String id = params[0];
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevenda/NomeCliente/" + id);
  			del.setHeader("content-type", "application/json");
  			try
  	        {			
  				HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	Nome=respStr.toString();		
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        }
  	        return resul;
  	    }
  	    protected void onPostExecute(Boolean result)
  	    {
  	    	if (result)
  	    	{
  	    		String xnome= Nome.substring(1, Nome.length());
  	    		String xnome2= xnome.substring(0,xnome.length()-1);
  	    		tvCliente.setText(xnome2);
  	    	}	
  	    }
  	}
  	//Tarea Asíncrona para llamar al WS de consulta en segundo plano
  	private class TareaWSUltimaLinha extends AsyncTask<String,Void,Boolean> 
  	{
  		private String adicionar;
  		private String remover;	  		 
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	boolean resul = true;
  	    	HttpClient httpClient = new DefaultHttpClient();
  			//String id = params[0];
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevenda/UltimoLinha");
  			del.setHeader("content-type", "application/json");
  			try
  	        {			
  				HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	ultimaLinha=respStr;		
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        }
  	        return resul;
  	    }
  	    protected void onPostExecute(Boolean result)
  	    {	
  	    	if (result)
  	    	{ 	  		
  	    		adicionar=etAdicionar.getText().toString();
  	    		remover=etRemover.getText().toString();
  	    		if(adicionar.length()>0 && remover.length()>0)
  	    		{ 	    			
  	    				tipoAR="+";
	  	    			descComplemento=etAdicionar.getText().toString();
	  	    			TareaWSInsertarComplementoA tarea= new TareaWSInsertarComplementoA();
	  	    			tarea.execute(); 
	  	    			return;
  	    		}else
  	    		{   		
  	    			if(adicionar.length()>0)
  	    			{ 	    			
  	    				tipoAR="+";
	    				descComplemento=etAdicionar.getText().toString();
	    				TareaWSInsertarComplementoR tarea= new TareaWSInsertarComplementoR();
	    				tarea.execute();
	    				return;
  	    			}  			
  	    			if(remover.length()>0)
  	    			{ 	  	    			
  	  	    			tipoAR="-";
  	  	    			descComplemento=etRemover.getText().toString();
  	  	    			TareaWSInsertarComplementoR tarea= new TareaWSInsertarComplementoR();
  	  	    			tarea.execute();
  	  	    			return;
  	    			}	
  	    		}
  	    	}  	    	
  	    }
  	}
  	//Tarea Asíncrona para llamar al WS de consulta en segundo plano
  	private class TareaWSUltimoNroOrdem extends AsyncTask<String,Void,Boolean> 
  	{
  		private String nroOrdem;
  	    protected Boolean doInBackground(String... params)
  	    {
  	    	boolean resul = true;	
  	    	HttpClient httpClient = new DefaultHttpClient();
  			String id = params[0];
  			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevenda/OrdemItem/" + id);
  			del.setHeader("content-type", "application/json");
  			try
  	        {			
  				HttpResponse resp = httpClient.execute(del);
  	        	String respStr = EntityUtils.toString(resp.getEntity());
  	        	nroOrdem=respStr;		
  	        }
  	        catch(Exception ex)
  	        {
  	        	Log.e("ServicioRest","Error!", ex);
  	        	resul = false;
  	        }
  	        return resul;
  	    }
  	    protected void onPostExecute(Boolean result)
  	    {	
  	    	if (result)
  	    	{
  	    		itemOrdem= Integer.parseInt(nroOrdem);
  	    		TareaWSRetornaNome tarea1 = new TareaWSRetornaNome();
	    		tarea1.execute(nroCartao);  	    		
  	    	}	
  	    }
  	}
	//Tarea Asíncrona para llamar al WS de listado en segundo plano
	private class TareaWSListarConsumo extends AsyncTask<String,Integer,Boolean> 
	{				
	    protected Boolean doInBackground(String... params) 
		{    	
			boolean resul = true;    	
			HttpClient httpClient = new DefaultHttpClient();    	
			String cartao = params[0];
			String id = params[1];	
			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevendas/Prevenda/" + cartao +"/" + id);		
			del.setHeader("content-type", "application/json");		
			try
			{			
			    HttpResponse resp = httpClient.execute(del);
			    String respStr = EntityUtils.toString(resp.getEntity());    	
			    JSONArray respJSON = new JSONArray(respStr);    	
			    //consumo = new String[respJSON.length()];
			    alConsumo= new Consumo[respJSON.length()];
			    for(int i=0; i<respJSON.length(); i++)
			    {
			        JSONObject obj = respJSON.getJSONObject(i);
			        String codigo= obj.getString("produtoCodigo");
			        String barra=obj.getString("produtoCodigobarra");
			        String ordem = obj.getString("produtoOrdem");
				    String descricao = obj.getString("descricao");
				    String quantidade = obj.getString("produtoQuantidade");
				    String unitario = obj.getString("produtoUnitario");
				    String total= obj.getString("total");
				    String situacao= obj.getString("produtoSituacao");    		
				  	alConsumo[i]=new Consumo(codigo, barra, ordem, descricao, quantidade, unitario, total, situacao);
			    }        	
			}
			catch(Exception ex)
			{
			     Log.e("ServicioRest","Error!", ex);
			     resul = false;
			} 
			return resul;
		}	    
		protected void onPostExecute(Boolean result) 
		{    	
			if (result)
			{
				//Rellenamos la lista con los nombres de los clientes
			    //Rellenamos la lista con los resultados		
			   	adapterConsumo adaptador= new adapterConsumo(Menu_principal.this);        	
				lvConsumo.setAdapter(adaptador);        	
				TareaWSTotal tarea= new TareaWSTotal();
				tarea.execute(idConsumo);    		
			}	    	
		}
	}
	//Tarea Asíncrona para llamar al WS de inserción en segundo plano
	private class TareaWSInsertarPrevenda extends AsyncTask<String,Integer,Boolean>
	{		
		protected Boolean doInBackground(String... params)
		{    	
			boolean resul = true;    	
			HttpClient httpClient = new DefaultHttpClient();
			        
			HttpPost post = new HttpPost("http://"+Servidor+"/RestService/Api/Prevendas/Insertar");
			post.setHeader("content-type", "application/json");		
			try
			{
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();		
				//dato.put("Id", Integer.parseInt(txtId.getText().toString()));
				//dato.put("prevendaCaixa", null);
				dato.put("cartao", params[0]);
				//dato.put("prevendaCpFiscal", null);
				//dato.put("prevendaSituacao", 0);
				//dato.put("prevendaFechada", 0);
				dato.put("data", DataBD());
				dato.put("nomeCliente", nomeCliente);
						
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
						
			   	HttpResponse resp = httpClient.execute(post);
			   	String respStr = EntityUtils.toString(resp.getEntity());
			        	
			   	if(!respStr.equals("true"))
				resul = false;
			}
			catch(Exception ex)
			{
			   	Log.e("ServicioRest","Error!", ex);
			   	resul = false;
			} 
			return resul;
			}	    
		    protected void onPostExecute(Boolean result) 
			{	
			    if (result)
			    {
			    	itemOrdem= 0;
			    	//TareaWSEstado tarea= new TareaWSEstado();
					//tarea.execute(nroCartao);
			    	chamaPrincipal();
			    }
			    /*if (!result)
			    {
			    	mensagemExibir("Prevenda", "No paso nada!!");
			  		chamaPrincipal();
		   		}*/
			}
	}	
	//Tarea Asíncrona para llamar al WS de inserción en segundo plano
	private class TareaWSInsertarItemConsumo extends AsyncTask<String,Integer,Boolean> 
	{				
		protected Boolean doInBackground(String... params)
		{    	
			boolean resul = true;	    	
			HttpClient httpClient = new DefaultHttpClient();	        
			HttpPost post = new HttpPost("http://"+Servidor+"/RestService/Api/Prevendas/InsertarPrevendaDetalhes");
			post.setHeader("content-type", "application/json");
						
			try
			{
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();
						
				dato.put("prevendaid", idConsumo);
				dato.put("produtoOrdem", itemOrdem);
				dato.put("produtoCodigo", codigo);
				dato.put("produtoQuantidade", params[0]);
				dato.put("produtoUnitario", unitario);
				dato.put("produtoSituacao", 0);
				dato.put("produtoCodigobarra", codBarras);
				dato.put("idatendente", idVendedor);
					
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
						
			   	HttpResponse resp = httpClient.execute(post);
			   	String respStr = EntityUtils.toString(resp.getEntity());
				        	
			   	if(!respStr.equals("true"))
				resul = false;
			}
			catch(Exception ex)
			{
			   	Log.e("ServicioRest","Error!", ex);
			   	resul = false;
			}		 
			return resul;
		}		    
		protected void onPostExecute(Boolean result)
		{	    	
			if (result)
			{
				//Toast.makeText(Menu_principal.this, "Salvando operacões....", Toast.LENGTH_SHORT).show();
				if(produtoPermiteAlterar == "true")
				{
				    TareaWSUltimaLinha tarea= new TareaWSUltimaLinha();
				    tarea.execute();  	    				
				}    		
				enter=0;
				etProduto.setText("");
				tvDescricao.setText("");
				etQuant.setText("1");
				btnConfirmar.setEnabled(false);
				etProduto.requestFocus();
				//itemOrdem= 0;
			}		    	
		}
	}
	//Tarea Asíncrona para llamar al WS de inserción en segundo plano
	private class TareaWSInsertarComplementoA extends AsyncTask<String,Integer,Boolean>
	{			
		protected Boolean doInBackground(String... params)
		{		    	
			boolean resul = true;
			HttpClient httpClient = new DefaultHttpClient();	        
			HttpPost post = new HttpPost("http://"+Servidor+"/RestService/Api/Prevendas/InsertarComplemento");
			post.setHeader("content-type", "application/json");			
			try
			{
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();
							
				//dato.put("Id", Integer.parseInt(txtId.getText().toString()));
				dato.put("prevendaLinhaId", Integer.parseInt(ultimaLinha ));
				dato.put("prevendaDetalheTipo", tipoAR);
				dato.put("prevendaDetalheDescricao", descComplemento);
						
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
							
				HttpResponse resp = httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());        	
				if(!respStr.equals("true"))
				resul = false;
			}
	        catch(Exception ex)
	        {
	        	Log.e("ServicioRest","Error!", ex);
	        	resul = false;
			}				 
			return resul;
		}    
		protected void onPostExecute(Boolean result)
		{		    	
	    	if (result)
	    	{				    		
				tipoAR="-";
				descComplemento=etRemover.getText().toString();
				TareaWSInsertarComplementoR tarea= new TareaWSInsertarComplementoR();
				tarea.execute();				    		
	   			//etAdicionar.requestFocus();
	    	}
	    	/*if (!result)
	    	{
	    		mensagemExibir("Prevenda", "No paso nada!!");
	    	}*/
	    }
	}			
	//Tarea Asíncrona para llamar al WS de Insertar Complemento en segundo plano
	private class TareaWSInsertarComplementoR extends AsyncTask<String,Integer,Boolean>
	{			
		protected Boolean doInBackground(String... params)
		{	    	
			boolean resul = true;	    	
			HttpClient httpClient = new DefaultHttpClient();
				        
			HttpPost post = new HttpPost("http://"+Servidor+"/RestService/Api/Prevendas/InsertarComplemento");
			post.setHeader("content-type", "application/json");	
			try
			{
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();
							
				//dato.put("Id", Integer.parseInt(txtId.getText().toString()));
				dato.put("prevendaLinhaId", Integer.parseInt(ultimaLinha));
				dato.put("prevendaDetalheTipo", tipoAR);
				dato.put("prevendaDetalheDescricao", descComplemento);
							
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
						
				HttpResponse resp = httpClient.execute(post);
				String respStr = EntityUtils.toString(resp.getEntity());	        	
			   	if(!respStr.equals("true"))
				resul = false;
			}
			catch(Exception ex)
			{
				Log.e("ServicioRest","Error!", ex);
	        	resul = false;
			}	 
			return resul;
		}	    
		protected void onPostExecute(Boolean result)
		{		    	
			if (result)
			{
				etAdicionar.setText("");
				etRemover.setText("");
				etAdicionar.setEnabled(false);
		  		etRemover.setEnabled(false);
		  		//etAdicionar.requestFocus();
			}
			/*if (!result)
			{
	   			mensagemExibir("Prevenda", "No paso nada!!");
	    	}*/
	    }
	}
	//Tarea Asíncrona para llamar al WS de Insertar Prevenda Detalhes en segundo plano
	private class TareaWSCancelar extends AsyncTask<String,Integer,Boolean>
	{			
		protected Boolean doInBackground(String... params)
		{	    	
		  	boolean resul = true;		    	
		  	HttpClient httpClient = new DefaultHttpClient();	        
			HttpPut put = new HttpPut("http://"+Servidor+"/RestService/Api/Prevendas/InsertarPrevendaDetalhes");
			put.setHeader("content-type", "application/json");			
			try
			{
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();			
				dato.put("prevendaid", idConsumo);
				dato.put("produtoOrdem", itemEliminar);
				dato.put("produtoCodigo", codigo);
				dato.put("produtoQuantidade", quantidad);
				dato.put("produtoUnitario", unitario);
				dato.put("produtoSituacao", 1);
				dato.put("produtoCodigobarra", codBarras);
				dato.put("idatendente", idVendedor);
							
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
						
				HttpResponse resp = httpClient.execute(put);
				String respStr = EntityUtils.toString(resp.getEntity());
				        	
				if(!respStr.equals("true"))
					resul = false;
			}
			catch(Exception ex)
			{
			 	Log.e("ServicioRest","Error!", ex);
			   	resul = false;
			}
			return resul;
		}    
		protected void onPostExecute(Boolean result)
		{    	
			if (result)
			{
			  	totItem=0;
			   	TareaWSEstado tarea= new TareaWSEstado();
				tarea.execute(nroCartao);
			   	Toast.makeText(Menu_principal.this, "Item cancelado", Toast.LENGTH_SHORT).show();    		
			 }
		}    
	}
	//Tarea Asíncrona para llamar al WS de Insertar consumo en segundo plano
	private class TareaWSCancelarConsumo extends AsyncTask<String,Integer,Boolean>
	{	
	    protected Boolean doInBackground(String... params) 
	    {
			    	
			boolean resul = true;
			HttpClient httpClient = new DefaultHttpClient();        
			HttpPut put = new HttpPut("http://"+Servidor+"/RestService/Api/Prevendas/Insertar");
			put.setHeader("content-type", "application/json");		
			try
			  {
				//Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();
					
				dato.put("prevendaid", idConsumo);
				//dato.put("prevendaCaixa", null);
				dato.put("cartao", nroCartao);
				//dato.put("prevendaCpFiscal", null);
				dato.put("prevendaSituacao", 1);
				dato.put("prevendaFechada", 1);
				dato.put("data", DataBD());
						
				StringEntity entity = new StringEntity(dato.toString());
				put.setEntity(entity);
						
			    HttpResponse resp = httpClient.execute(put);
			    String respStr = EntityUtils.toString(resp.getEntity());
			        	
			    if(!respStr.equals("true"))
			    	resul = false;
			  }
			  catch(Exception ex)
			  {
			   	Log.e("ServicioRest","Error!", ex);
			   	resul = false;
			  }
			  return resul;
		}  
		protected void onPostExecute(Boolean result) 
		{	    	
		   	if (result)
		   	{
		   		nroCartao="Não Definido";
		   		idConsumo="0";
		   		//TareaWSEstado tarea= new TareaWSEstado();
				//tarea.execute("0");
			    		
		   		Toast.makeText(Menu_principal.this, "Consumo cancelado", Toast.LENGTH_SHORT).show();
		   		chamaPrincipal();
		   	}
		   	if (!result)
		   	{
				mensagemExibir("Cancelar Consumo", "No paso nada!!");
			}
		}		    
	}
	//Tarea Asíncrona para llamar al WS de listado de produto en segundo plano
	private class TareaWSListarProduto extends AsyncTask<String,Integer,Boolean>
	{
	   protected Boolean doInBackground(String... params)
	   {   	
		   boolean resul = true;   	
		   HttpClient httpClient = new DefaultHttpClient();    	
		   HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Produtos");
		   del.setHeader("content-type", "application/json");		
		   try
		   {			
			   HttpResponse resp = httpClient.execute(del);
			   String respStr = EntityUtils.toString(resp.getEntity());
			        	
			   JSONArray respJSON = new JSONArray(respStr);   	
			   //consumo = new String[respJSON.length()];
			   lstProdutos= new String[respJSON.length()];
			   for(int i=0; i<respJSON.length(); i++)
			   {
			   		JSONObject obj = respJSON.getJSONObject(i);
			        String codigo= obj.getString("idproduto");
				    String descricao = obj.getString("descricao");
				    lstProdutos[i]= new String(codigo +" "+ descricao);
			   	}
			}
			catch(Exception ex)
			{
			   	Log.e("ServicioRest","Error!", ex);
			   	resul = false;
			}
			    return resul;
		}
		protected void onPostExecute(Boolean result)
		{
			if (result)
			{		
				// Cria o Adapter
				adapterProduto = new ArrayAdapter<String>(Menu_principal.this,android.R.layout.simple_list_item_1,lstProdutos);	    		    
				lvProduto.setAdapter(adapterProduto);
				//lvProduto.setAdapter(new ArrayAdapter<String>(PesquisaProduto.this, android.R.layout.simple_list_item_1, lstProdutos));
				//Carrega o listview com todos os itens           
				//CarregarEncontrados();            
				//Adicion um TextWatcher ao TextView cujos métodos são chamados sempre 
				//que este TextView sofra alterações.
				etProcurar.addTextChangedListener(new TextWatcher() 
				{
				  	//Evento acionado quando o usuário teclar algo
				    //na caixa de texto "Procurar"
				   	@Override
				   	public void onTextChanged(CharSequence s, int start, int before, int count)
				   	{
				   		//CarregarEncontrados();
				        //Carrega o listview com os itens encontrados
				   		adapterProduto.getFilter().filter(s.toString());
				   	}
				   		@Override
				   	public void beforeTextChanged(CharSequence s, int start, int count,	int after)
				   	{
				   		// TODO Auto-generated method stub
				   	}		
				   	@Override
				   	public void afterTextChanged(Editable s)
				   	{
				   		// TODO Auto-generated method stub		
				   	}
				 });
			}   	
			/*if (!result)
			{
				mensagemExibir("Lista Produto", "No paso nada!!");
			}*/    	
		 }
	}
	//Tarea Asíncrona para llamar al WS de listado en segundo plano
	private class TareaWSListarMesasOcupadas extends AsyncTask<String,Integer,Boolean> 
	{				
	    protected Boolean doInBackground(String... params) 
		{    	
			boolean resul = true;    	
			HttpClient httpClient = new DefaultHttpClient();    	
			HttpGet del = new HttpGet("http://"+Servidor+"/RestService/Api/Prevenda/MesasOcupadas");		
			del.setHeader("content-type", "application/json");		
			try
			{			
				HttpResponse resp = httpClient.execute(del);
			    String respStr = EntityUtils.toString(resp.getEntity());    	
			    JSONArray respJSON = new JSONArray(respStr);  
			    mesaOcupadas= new String[respJSON.length()];
			    for(int i=0; i<respJSON.length(); i++)
			    {
			        //JSONObject obj = respJSON.getJSONObject(i);
			        //String mesa=respJSON.getJSONObject(i).toString(); //respJSON.getString(i);	
				  	mesaOcupadas[i]=respJSON.getString(i);
			    }          	
			}
			catch(Exception ex)
			{
			     Log.e("ServicioRest","Error!", ex);
			     resul = false;
			} 
			return resul;
		}	    
		protected void onPostExecute(Boolean result) 
		{    	
			if (result)
			{
				for(int i=0;i<mesaOcupadas.length;i++)
				{
					Boton[] bot=  findButtons();
					
					 for(int b=0;b<=52;b++)
					 {
						 if(mesaOcupadas[i].toString().equals(bot[b].getBot().getText()))
						 {
							 bot[b].getBot().setBackgroundColor(Color.RED); 
						 } 
					 }
				}  		
			}
			if (!result)
			{
	   			mensagemExibir("Prevenda", "No paso nada!!");
	    	}
		}
	}
	
	//FIN TAREAS ASINCRONICAS
}
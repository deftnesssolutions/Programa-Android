package br.com.crudsqlliteandroid.UI;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.crudsqlliteandroid.UI.R.id;

public class Main extends Activity implements OnClickListener {

    EditText txtNome;
	Button btnSair, btnVendedor,btnProduto,btnPrevenda;
	public String msgb="Pressione o botão Sair para fechar o aplicativo";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		chamaPrincipal();
	}

	@Override 
    public  void onBackPressed ()  {// Bloqueo el boton back do tablet 
    // fazer algo nas costas. 
    	Toast.makeText(this, msgb, Toast.LENGTH_SHORT).show();
    	return ; 
    }
	
	//escucha del evento click de los botones, gracias a que la clase implementa el onClickListener
	public void onClick(View v) {
    	
		if (v.getId() == id.btnVendedor)
		{
	        	registraVendedor();
		}
		if (v.getId() == id.btnProduto)
		{
	        	cadastroProduto();
		}
		if (v.getId()== id.btnExit)
		{
				System.exit(0);	
        }		
	}
	//##Medoto para llamar a la tela de Vendedor
	private void registraVendedor() {
		try
        {
            Intent it = new Intent(this, VendedorUI.class);
            startActivity(it);//chama a tela e incusão
        }
        catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }  		
	}
	
	//##Medoto para llamar a la tela de Cadastro de Produto
	private void cadastroProduto() {
		try
		{
	            Intent it = new Intent(this, ProdutoUI.class);
	            startActivity(it);//chama a tela e incusão
	    }
	    catch (Exception e) {
	       trace("Erro : " + e.getMessage());
	    }  		
	}

    public void toast (String msg)
    {
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
    } 
     
    private void trace (String msg) 
    {
        toast (msg);
    }    
 
	//##Metodo para inflar la tela principal
	private void chamaPrincipal() {
		setContentView(R.layout.main);
		
		btnVendedor = (Button)findViewById(R.id.btnVendedor);
    	btnVendedor.setOnClickListener(this);
		btnProduto = (Button)findViewById(R.id.btnProduto);
    	btnProduto.setOnClickListener(this);
		btnPrevenda = (Button)findViewById(R.id.btnPrevenda);
    	btnPrevenda.setOnClickListener(this);  	
		btnVendedor = (Button)findViewById(R.id.btnVendedor);
    	btnVendedor.setOnClickListener(this);
    	btnSair = (Button)findViewById(id.btnExit);
    	btnSair.setOnClickListener((OnClickListener) this);
	}
}


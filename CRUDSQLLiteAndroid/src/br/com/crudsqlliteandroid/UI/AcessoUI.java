package br.com.crudsqlliteandroid.UI;

import java.util.List;

import br.com.crudsqlliteandroid.DAO.VendedorDAO;
import br.com.crudsqlliteandroid.POJO.VendedorVO;
import br.com.crudsqlliteandroid.UI.R.id;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AcessoUI extends Activity implements OnClickListener{
	Button btnSair,btnIngresar;
	VendedorDAO vendedor;
	EditText txtCodigo;
	List<VendedorVO> lstVendedores;  //lista de contatos cadastrados no BD
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acessoprevenda);
		
		vendedor = new VendedorDAO(this);
		vendedor.open();
		btnSair = (Button)findViewById(id.btnExit);
		btnSair.setOnClickListener((OnClickListener) this);
		btnIngresar = (Button)findViewById(id.btnIngresar);
		btnIngresar.setOnClickListener(this);
		txtCodigo=(EditText)findViewById(id.txtCodigo);
	}

	public void onClick(View v) {
		if(v.getId()==id.btnExit)
		{
			finish();
		}
		if(v.getId()==id.btnIngresar)
		{
			validarCodigo();
		}
	}

	private void validarCodigo() {
		String nomeVendedor=null;
		try {
			String id= txtCodigo.getText().toString();
			nomeVendedor=vendedor.ConsultarId(id);
			  
               Intent it = new Intent(this, prevendaUI.class);
               it.putExtra("idVendedor", id );
               it.putExtra("nome", nomeVendedor);
               startActivityForResult(it,1); //chama a tela de alteração  
            
		} catch (Exception e) {
			//trace("Erro : " + e.getMessage());
			mensagemExibir("Prevenda", "O codigo de vendedor não existe.");
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
  //Cuadro de mensaje personalizado para utilizar en toda la aplicación
    public void mensagemExibir(String titulo, String texto)
    {
    	AlertDialog.Builder mensagem = new AlertDialog.Builder(AcessoUI.this);
    	mensagem.setTitle(titulo);
    	mensagem.setMessage(texto);
    	mensagem.setNeutralButton("OK", null);
    	mensagem.show();
    	
    }
}

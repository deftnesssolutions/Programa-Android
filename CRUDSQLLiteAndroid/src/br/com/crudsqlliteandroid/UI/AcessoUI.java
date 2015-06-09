package br.com.crudsqlliteandroid.UI;

import java.util.List;

import br.com.crudsqlliteandroid.DAO.VendedorDAO;
import br.com.crudsqlliteandroid.POJO.ProdutoVO;
import br.com.crudsqlliteandroid.POJO.VendedorVO;
import br.com.crudsqlliteandroid.UI.R.id;
import br.com.crudsqlliteandroid.UTIL.VendedorAdapter;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AcessoUI extends Activity implements OnClickListener{
	Button btnSair;
	VendedorDAO vendedor;
	EditText txtCodigo;
	List<VendedorVO> lstVendedores;  //lista de contatos cadastrados no BD
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acessoprevenda);
		vendedor = new VendedorDAO(this);
		btnSair = (Button)findViewById(id.btnExit);
		btnSair.setOnClickListener((OnClickListener) this);
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
		try {
			
    		lstVendedores =vendedor.ConsultarId(Integer.parseInt(txtCodigo.getText().toString()));
            if(lstVendedores != null)
            {                        
               Intent it = new Intent(this, VendedorUI.class);
               it.putExtra("vendedor", lstVendedores);
               startActivityForResult(it, ALTERAR); //chama a tela de alteração  
            }
		} catch (Exception e) {
			trace("Erro : " + e.getMessage());
		}
	}
	
	public void chamaPrevenda()
    {
		VendedorVO lVededorVO = null;
    	
        return true; 
    }
	public void toast (String msg)
    {
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
    } 
     
    private void trace (String msg) 
    {
        toast (msg);
    }    
 
}

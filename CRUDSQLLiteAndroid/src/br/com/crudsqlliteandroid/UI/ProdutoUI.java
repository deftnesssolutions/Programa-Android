package br.com.crudsqlliteandroid.UI;

import br.com.crudsqlliteandroid.POJO.ProdutoVO;
import br.com.crudsqlliteandroid.UI.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProdutoUI extends Activity {
	private static final int INCLUIR = 0;
	
    public String msgb="Pressione o botão Sair para voltar ao menu principal";
    EditText txtId,txtDescricao,txtUnidade,txtPreco,txtCodBarra;
    
    Spinner txtRp,txtPa;
    private static final String[] opcion = new String[]{"Sim","Não"}; 
    ProdutoVO lProdutoVO;
    ArrayAdapter adp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_produto);
        Spinner combo = (Spinner) findViewById(R.id.opciones);
    	Spinner combo2 = (Spinner)findViewById(id.opciones2);
    	
        
		 adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opcion);
		 adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
		 combo.setAdapter(adp);      
         combo2.setAdapter(adp);
         salvar();
    }
    
  //##METODOS PUBLICOS 
    @Override 
    public  void onBackPressed ()  {// Bloqueo el boton back do tablet 
    // fazer algo nas costas. 
    	Toast.makeText(this, msgb, Toast.LENGTH_SHORT).show();
    	return ; 
    }
    
    
  //##Metodo para guardar registro novo ou update
    public void salvar()
    {
    	try
        {    
    		final Bundle data = (Bundle) getIntent().getExtras();
            int lint = data.getInt("tipo");
           if (lint == INCLUIR)
           {
        	   //quando for incluir um contato criamos uma nova instância
        	   lProdutoVO = new ProdutoVO(); 
            }else{
            	//quando for alterar o contato carregamos a classe que veio por Bundle
                lProdutoVO = (ProdutoVO)data.getSerializable("estoque");
            }
            //Criação dos objetos da Activity
           	txtDescricao = (EditText)findViewById(id.edtDescricao);
       		txtUnidade = (EditText)findViewById(id.edtUnidade);
       		txtPreco = (EditText)findViewById(id.edtPreco);
       		txtRp = (Spinner)findViewById(id.opciones);
       		txtPa = (Spinner)findViewById(id.opciones2);
       		
       		//Carregando os objetos com os dados do Contato
            //caso seja uma inclusão ele virá carregado com os atributos text
            //definido no arquivo main.xml
       		txtDescricao.setText(lProdutoVO.getDescricao());
       		txtUnidade.setText(lProdutoVO.getUnidade());
       		if(String.valueOf(lProdutoVO.getPreco())!="null")
       			txtPreco.setText( String.valueOf(lProdutoVO.getPreco()));
       		else
       			txtPreco.setText("0,0");
       		if(lProdutoVO.getReqproducao()=="Sim")
       			txtRp.setSelection(0);
       		else
       			txtRp.setSelection(1);
       		
       		if(lProdutoVO.getPeralterar()=="Sim")
       			txtPa.setSelection(0);
       		else
       			txtPa.setSelection(1);
       		
       }catch (Exception e) {
           trace("Erro : " + e.getMessage());
       }  
    }
   
    public void spinner_click()
    {
    	
    }
  //Click do boton Sair
    public void btnSalvar_click(View view)
    {
    	try
        {
            //Quando confirmar a inclusão ou alteração deve-se devolver
            //o registro com os dados preenchidos na tela e informar
            //o RESULT_OK e em seguida finalizar a Activity
             
             
            Intent data = new Intent();
            lProdutoVO.setDescricao(txtDescricao.getText().toString());
            lProdutoVO.setUnidade(txtUnidade.getText().toString());
            lProdutoVO.setPreco(Double.valueOf(txtPreco.getText().toString()));
            lProdutoVO.setReqproducao(txtRp.getSelectedItem().toString());
            lProdutoVO.setPeralterar(txtPa.getSelectedItem().toString());
            data.putExtra("estoque", lProdutoVO);
            setResult(Activity.RESULT_OK, data);    
            finish();
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }    
    }
  //Click do boton Sair
    public void btnCancel_click(View view)
    {
    	finish();
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


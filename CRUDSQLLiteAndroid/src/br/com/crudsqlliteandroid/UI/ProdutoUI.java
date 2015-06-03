package br.com.crudsqlliteandroid.UI;

import java.util.List;
import br.com.crudsqlliteandroid.DAO.ProdutoDAO;
import br.com.crudsqlliteandroid.POJO.ProdutoVO;
import br.com.crudsqlliteandroid.UI.R.id;
import br.com.crudsqlliteandroid.UTIL.ProdutoAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ProdutoUI extends ListActivity {

	private  int INCLUIR = 0;
    private  int ALTERAR = 1;
    //private static final int ALTERAR = 1;
	private ProdutoDAO lProdutoDAO; //instância responsável pela persistência dos dados
	List<ProdutoVO> lstProdutos;  //lista de contatos cadastrados no BD
    ProdutoAdapter adapter;   //Adapter responsável por apresentar os contatos na tela
    ProdutoVO lProdutoVO; 
    EditText txtId,txtDescricao,txtUnidade,txtPreco,txtReqProducao,txtPerAlterar,txtCodBarra;
    boolean blnShort = false;
    int Posicao = 0;    //determinar a posição do contato dentro da lista lstContatos
    public String msgb="Pressione o botão Sair para voltar ao menu principal";
    
    private static final String[] opcion = new String[]{"Sim","Não"}; 
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       inicializar();
      
       llenarLista();
                   
    }
    
  //##METODOS PUBLICOS 
    @Override 
    public  void onBackPressed ()  {// Bloqueo el boton back do tablet 
    // fazer algo nas costas. 
    	Toast.makeText(this, msgb, Toast.LENGTH_SHORT).show();
    	return ; 
    }
    
    public void inicializar()
    {
    	setContentView(R.layout.produto);
        lProdutoDAO = new ProdutoDAO(this);
        lProdutoDAO.open();
    	//Criação dos objetos da Activity
        txtDescricao = (EditText)findViewById(R.id.txtDescricao);
        txtUnidade = (EditText)findViewById(R.id.txtUnidade);
        txtPreco = (EditText)findViewById(R.id.txtPreco);
        txtReqProducao = (EditText)findViewById(R.id.txtReqProducao);
        txtPerAlterar = (EditText)findViewById(R.id.txtPerAlterar);
        txtCodBarra = (EditText)findViewById(R.id.txtCodBarra);
        txtDescricao.requestFocus();
    }
    //##Metodo para guardar registro novo ou update
    public void inserir()
    {
    	try
        {         
            if (INCLUIR==0)
            {
                //quando for incluir um contato criamos uma nova instância
                lProdutoVO = new ProdutoVO(); 
                lProdutoVO.setDescricao(txtDescricao.getText().toString());
                lProdutoVO.setUnidade(txtUnidade.getText().toString());
                lProdutoVO.setPreco(Double.parseDouble(txtPreco.getText().toString()));
                lProdutoVO.setReqproducao(txtReqProducao.getText().toString());
                lProdutoVO.setPeralterar(txtPerAlterar.getText().toString());
                lProdutoVO.setCodbarra(txtCodBarra.getText().toString());
                
                lProdutoDAO.Inserir(lProdutoVO); 
            }else{
                //quando for alterar o contato carregamos os dados desde o menu contextual
            	//Carregar a instância POJO com a posição selecionada na tela
            	lProdutoVO = (ProdutoVO) getListAdapter().getItem(Posicao);
            	lProdutoVO.setDescricao(txtDescricao.getText().toString());
                lProdutoVO.setUnidade(txtUnidade.getText().toString());
                lProdutoVO.setPreco(Double.parseDouble(txtPreco.getText().toString()));
                lProdutoVO.setReqproducao(txtReqProducao.getText().toString());
                lProdutoVO.setPeralterar(txtPerAlterar.getText().toString());
                lProdutoVO.setCodbarra(txtCodBarra.getText().toString());
                lProdutoDAO.Alterar(lProdutoVO);
            }
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }  
    }
    
    public void chamaCadastro()
    {
    	setContentView(R.layout.cadastro_produto);	
    	
    	Spinner combo = (Spinner) findViewById(R.id.opciones);
    	Spinner combo2 = (Spinner)findViewById(id.opciones2);
    	
        ArrayAdapter adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opcion);
        ArrayAdapter adp2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opcion);
        
        adp.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        combo.setAdapter(adp);
        combo2.setAdapter(adp2);
    }
    
    //##Metodo para preencher a lista
    public void llenarLista()
    {
    	try {
    		lstProdutos =lProdutoDAO.Consultar();
            adapter=new ProdutoAdapter(this, lstProdutos);
            setListAdapter(adapter);
            registerForContextMenu(getListView());
		} catch (Exception e) {
			trace("Erro : " + e.getMessage());
		}
    	 
    }
    
    //##Click do boton Cadastrar 
    public void btnAdd_click(View view)
    {
    	chamaCadastro();
    }
     
    //Click do boton Sair
    public void btnVoltar_click(View view)
    {
    	finish();
    }
   /* 
    //##Metodo que cria o menu popup
    @Override   
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {        
        try
        {
            //Criação do popup menu com as opções que termos sobre
            //nossos Contatos
             
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            if (!blnShort)
            {
                Posicao = info.position;
            }
            blnShort = false;
             
            menu.setHeaderTitle("Selecione:");            
            //a origem dos dados do menu está definido no arquivo arrays.xml 
            String[] menuItems = getResources().getStringArray(R.array.menu);             
            for (int i = 0; i<menuItems.length; i++) {                
                menu.add(Menu.NONE, i, i, menuItems[i]);            
            }        
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }            
    }  
  //Este método é disparado quando o usuário clicar em um item do ContextMenu
    @Override   
    public boolean onContextItemSelected(MenuItem item) {       
    	ProdutoVO lProdutoVO = null;
        try
        {
            int menuItemIndex = item.getItemId();        
 
            //Carregar a instância POJO com a posição selecionada na tela
            lProdutoVO = (ProdutoVO) getListAdapter().getItem(Posicao);
             
            if (menuItemIndex == 0){
                //Carregar a Activity ContatoUI com o registro selecionado na tela
                 
               //txtNome.setText(lVendeVO.getNome());
               INCLUIR=1;
            }else if (menuItemIndex == 1){
                //Excluir do Banco de Dados e da tela o registro selecionado
                 
                //lProdutoDAO.Excluir(lVendeVO);
                //lstProdutos.remove(lVendeVO);
                //adapter.notifyDataSetChanged(); //atualiza a tela
            }
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }   
        return true;   
         
    }    
 
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //por padrão o ContextMenu, só é executado através de LongClick, mas
        //nesse caso toda vez que executar um ShortClick, abriremos o menu
        //e também guardaremos qual a posição do itm selecionado
         
        Posicao = position;
        blnShort = true;
        this.openContextMenu(l);
    }*/
    public void toast (String msg)
    {
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
    } 
     
    private void trace (String msg) 
    {
        toast (msg);
    }    
 
}


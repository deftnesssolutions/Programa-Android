package br.com.crudsqlliteandroid.UI;

import java.util.List;

import br.com.crudsqlliteandroid.DAO.ProdutoDAO;
import br.com.crudsqlliteandroid.POJO.ProdutoVO;
import br.com.crudsqlliteandroid.UI.R.array;
import br.com.crudsqlliteandroid.UI.R.id;
import br.com.crudsqlliteandroid.UI.R.layout;
import br.com.crudsqlliteandroid.UTIL.ProdutoAdapter;
import android.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class CadastroProduto extends ListActivity {
	 private static final int INCLUIR = 0;
	 private static final int ALTERAR = 1;
    
	private ProdutoDAO lProdutoDAO; //instância responsável pela persistência dos dados
	List<ProdutoVO> lstProdutos;  //lista de contatos cadastrados no BD
    ProdutoAdapter adapter;   //Adapter responsável por apresentar os contatos na tela
     
    
    boolean blnShort = false;
    int Posicao = 0;    //determinar a posição do contato dentro da lista lstContatos
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inicializar();      
	    llenarLista();
	}
	
	//escucha del evento click de los botones, gracias a que la clase implementa el onClickListener
		public void onClick(View v) {
	    	
			if (v.getId() == id.btnAdd)
			{
		        	chamaCadastro();
			}
			if (v.getId()== id.btnVoltar)
			{
					finish();	
	        }		
		}
    public void chamaCadastro()
    {
    	try
        {
            //a variável "tipo" tem a função de definir o comportamento da Activity
            //ContatoUI, agora a variável tipo está definida com o valor "0" para
            //informar que será uma inclusão de Contato
             
            Intent it = new Intent(this, ProdutoUI.class);
            it.putExtra("tipo", INCLUIR);
            startActivityForResult(it, INCLUIR);//chama a tela e incusão
        }
        catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }  
    }
    
	public void inicializar()
	    {
	    	setContentView(layout.produto);
	        lProdutoDAO = new ProdutoDAO(this);
	        lProdutoDAO.open();
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
	    
	//Rotina executada quando finalizar a Activity ProdutoUI
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        ProdutoVO lProdutoVO = null;
	         
	        try
	        {
	            super.onActivityResult(requestCode, resultCode, data);
	            if (resultCode == Activity.RESULT_OK)
	            {
	                //obtem dados inseridos/alterados na Activity ProdutoUI
	                lProdutoVO = (ProdutoVO)data.getExtras().getSerializable("estoque");
	                 
	                //o valor do requestCode foi definido na função startActivityForResult
	                if (requestCode == INCLUIR)
	                {
	                    //verifica se digitou algo no nome do contato
	                    if (!lProdutoVO.getDescricao().equals("")) 
	                    {
	                        //necessário abrir novamente o BD pois ele foi fechado no método onPause()
	                        lProdutoDAO.open();
	                         
	                        //insere o contato no Banco de Dados SQLite
	                        lProdutoDAO.Inserir(lProdutoVO);
	                         
	                        //insere o contato na lista de contatos em memória
	                        lstProdutos.add(lProdutoVO);
	                    }
	                }else if (requestCode == ALTERAR){
	                    lProdutoDAO.open();
	                    //atualiza o contato no Banco de Dados SQLite
	                    lProdutoDAO.Alterar(lProdutoVO);
	                     
	                    //atualiza o contato na lista de contatos em memória
	                    lstProdutos.set(Posicao, lProdutoVO);
	                }
	                 
	                //método responsável pela atualiza da lista de dados na tela
	                adapter.notifyDataSetChanged();
	            }
	        }
	        catch (Exception e) {
	            trace("Erro : " + e.getMessage());
	        }        
	    }    
	  
	@Override
    protected void onResume() {
        //quando a Activity cadastro receber o foco novamente abre-se novamente a conexão
        lProdutoDAO.open();
        super.onResume();
    }

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
            String[] menuItems = getResources().getStringArray(array.menu);             
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
                //Carregar a Activity ProdutoUI com o registro selecionado na tela
                 
                Intent it = new Intent(this, ProdutoUI.class);
                it.putExtra("tipo", ALTERAR);
                it.putExtra("produto", lProdutoVO);
                startActivityForResult(it, ALTERAR); //chama a tela de alteração
            }else if (menuItemIndex == 1){
                //Excluir do Banco de Dados e da tela o registro selecionado
                 
                lProdutoDAO.Excluir(lProdutoVO);
                lstProdutos.remove(lProdutoVO);
                adapter.notifyDataSetChanged(); //atualiza a tela
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
        //e também guardaremos qual a posição do item selecionado
         
        Posicao = position;
        blnShort = true;
        this.openContextMenu(l);
    }
    
	@Override
    protected void onPause() {
        //toda vez que o programa peder o foco fecha-se a conexão com o BD
        lProdutoDAO.close();
        super.onPause();
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

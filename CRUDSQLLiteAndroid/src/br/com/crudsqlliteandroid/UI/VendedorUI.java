package br.com.crudsqlliteandroid.UI;

import java.util.List;

import br.com.crudsqlliteandroid.DAO.VendedorDAO;
import br.com.crudsqlliteandroid.POJO.VendedorVO;
import br.com.crudsqlliteandroid.UTIL.VendedorAdapter;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
public class VendedorUI extends ListActivity {

	public int INCLUIR = 0;
    //private static final int ALTERAR = 1;
	private VendedorDAO lVendeDAO; //instância responsável pela persistência dos dados
	List<VendedorVO> lstVendedores;  //lista de contatos cadastrados no BD
    VendedorAdapter adapter;   //Adapter responsável por apresentar os contatos na tela
	VendedorVO lVendedorVO; 
    EditText txtNome;
    boolean blnShort = false;
    int Posicao = 0;    //determinar a posição do contato dentro da lista lstContatos
    public String msgb="Pressione o botão Sair para voltar ao menu principal";
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendedor);
        lVendeDAO = new VendedorDAO(this);
        lVendeDAO.open();
      //Criação dos objetos da Activity
        txtNome = (EditText)findViewById(R.id.edtNome); 
       llenarLista();
       txtNome.requestFocus();
                   
    }
    
  //##METODOS PUBLICOS 
    @Override 
    public  void onBackPressed ()  {// Bloqueo el boton back do tablet 
    // fazer algo nas costas. 
    	Toast.makeText(this, msgb, Toast.LENGTH_SHORT).show();
    	return ; 
    }
    //##Metodo para guardar registro novo ou update
    public void inserir()
    {
    	try
        {         
            if (INCLUIR==0)
            {
                //quando for incluir um contato criamos uma nova instância
                lVendedorVO = new VendedorVO(); 
                lVendedorVO.setNome(txtNome.getText().toString());
                lVendeDAO.Inserir(lVendedorVO); 
            }else{
                //quando for alterar o contato carregamos os dados desde o menu contextual
            	//Carregar a instância POJO com a posição selecionada na tela
            	lVendedorVO = (VendedorVO) getListAdapter().getItem(Posicao);
                lVendedorVO.setNome(txtNome.getText().toString());
                lVendeDAO.Alterar(lVendedorVO);
            }
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }  
    }
    
    //##Metodo para preencher a lista
    public void llenarLista()
    {
    	 lstVendedores =lVendeDAO.Consultar();
         adapter=new VendedorAdapter(this, lstVendedores);
         setListAdapter(adapter);
         registerForContextMenu(getListView());
    }
    
    //##Click do boton Confirmar 
    public void btnConfirmar_click(View view)
    {
        inserir(); 
        INCLUIR=0;
        txtNome.setText("");
        txtNome.requestFocus();
        llenarLista();
    }
     
    //##Click do boton Canelar
    public void btnCancelar_click(View view)
    {
        txtNome.setText("");
        txtNome.requestFocus();
    }
    
    //Click do boton Sair
    public void btnVoltar_click(View view)
    {
    	finish();
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
        VendedorVO lVendeVO = null;
        try
        {
            int menuItemIndex = item.getItemId();        
 
            //Carregar a instância POJO com a posição selecionada na tela
            lVendeVO = (VendedorVO) getListAdapter().getItem(Posicao);
             
            if (menuItemIndex == 0){
                //Carregar a Activity ContatoUI com o registro selecionado na tela
                 
               txtNome.setText(lVendeVO.getNome());
               INCLUIR=1;
            }else if (menuItemIndex == 1){
                //Excluir do Banco de Dados e da tela o registro selecionado
                 
                lVendeDAO.Excluir(lVendeVO);
                lstVendedores.remove(lVendeVO);
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
        //e também guardaremos qual a posição do itm selecionado
         
        Posicao = position;
        blnShort = true;
        this.openContextMenu(l);
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

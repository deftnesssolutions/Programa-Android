package br.com.crudsqlliteandroid.UTIL;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import br.com.crudsqlliteandroid.POJO.ProdutoVO;
import br.com.crudsqlliteandroid.UI.R.id;
import br.com.crudsqlliteandroid.UI.R.layout;

public class ProdutoAdapter extends BaseAdapter  {
    private Context context;
 
    private List<ProdutoVO> lstProduto;
    private LayoutInflater inflater;
 
   
    
    public ProdutoAdapter(Context context, List<ProdutoVO> list) {
        this.context = context;
        this.lstProduto = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
     
    //Atualizar ListView de acordo com o lstProduto
    @Override
    public void notifyDataSetChanged() {   
        try{
            super.notifyDataSetChanged();
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }
    } 
         
    public int getCount() {
        return lstProduto.size();
    }
 
    //Remover item da lista
    public void remove(final ProdutoVO item) {
        this.lstProduto.remove(item);
    } 
     
    //Adicionar item na lista
    public void add(final ProdutoVO item) {
        this.lstProduto.add(item);
    }    
     
    public Object getItem(int position) {
        return lstProduto.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        try
        {
             
        	ProdutoVO produto = lstProduto.get(position);
 
            //O ViewHolder irá guardar a instâncias dos objetos do estado_row
            ViewHolder holder;
             
            //Quando o objeto convertView não for nulo nós não precisaremos inflar
            //os objetos do XML, ele será nulo quando for a primeira vez que for carregado
            if (convertView == null) {
                convertView = inflater.inflate(layout.produto_rows, null);
                 
                //Cria o Viewholder e guarda a instância dos objetos
                holder = new ViewHolder();
                holder.tvId = (TextView) convertView.findViewById(id.txtId);
                holder.tvDescricao = (TextView) convertView.findViewById(id.txtDescricao);
                holder.tvUnidade  = (TextView) convertView.findViewById(id.txtUnidade);
                holder.tvPreco  = (TextView) convertView.findViewById(id.txtPreco);
                holder.tvReqProducao  = (TextView) convertView.findViewById(id.txtReqProducao);
                holder.tvPerAlterar  = (TextView) convertView.findViewById(id.txtPerAlterar);
                holder.tvCodBarra = (TextView) convertView.findViewById(id.txtCodBarra);
                
                convertView.setTag(holder);
            } else {
                //pega o ViewHolder para ter um acesso rápido aos objetos do XML
                //ele sempre passará por aqui quando,por exemplo, for efetuado uma rolagem na tela 
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvId.setText((String.valueOf(produto.getId())));
            holder.tvDescricao.setText(produto.getDescricao());
            holder.tvUnidade.setText(produto.getUnidade());
            holder.tvPreco.setText((String.valueOf(produto.getPreco())));
            holder.tvReqProducao.setText(produto.getReqproducao());
            holder.tvPerAlterar.setText(produto.getPeralterar());
            holder.tvCodBarra.setText(produto.getCodbarra());
            return convertView;            
             
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }
        return convertView;
    }
 
 
    public void toast (String msg)
    {
        Toast.makeText (context, msg, Toast.LENGTH_SHORT).show ();
    } 
     
    private void trace (String msg) 
    {
        toast (msg);
    } 
     
    //Criada esta classe estática para guardar a referência dos objetos abaixo
    static class ViewHolder {
    	public TextView tvId;
        public TextView tvDescricao;
        public TextView tvUnidade;
        public TextView tvPreco;
        public TextView tvReqProducao;
        public TextView tvPerAlterar;
        public TextView tvCodBarra;
       
    }    
}

package br.com.crudsqlliteandroid.UTIL;

import java.util.List;

import br.com.crudsqlliteandroid.POJO.VendedorVO;
import br.com.crudsqlliteandroid.UI.R.id;
import br.com.crudsqlliteandroid.UI.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class VendedorAdapter extends BaseAdapter  {
    private Context context;
 
    private List<VendedorVO> lstVendedor;
    private LayoutInflater inflater;
 
    public VendedorAdapter(Context context, List<VendedorVO> list) {
        this.context = context;
        this.lstVendedor = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
     
    //Atualizar ListView de acordo com o lstContato
    @Override
    public void notifyDataSetChanged() {   
        try{
            super.notifyDataSetChanged();
        }catch (Exception e) {
            trace("Erro : " + e.getMessage());
        }
    } 
         
    public int getCount() {
        return lstVendedor.size();
    }
 
    //Remover item da lista
    public void remove(final VendedorVO item) {
        this.lstVendedor.remove(item);
    } 
     
    //Adicionar item na lista
    public void add(final VendedorVO item) {
        this.lstVendedor.add(item);
    }    
     
    public Object getItem(int position) {
        return lstVendedor.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        try
        {
             
        	VendedorVO vendedor = lstVendedor.get(position);
 
            //O ViewHolder ir� guardar a inst�ncias dos objetos do estado_row
            ViewHolder holder;
             
            //Quando o objeto convertView n�o for nulo n�s n�o precisaremos inflar
            //os objetos do XML, ele ser� nulo quando for a primeira vez que for carregado
            if (convertView == null) {
                convertView = inflater.inflate(layout.vendedor_row, null);
                 
                //Cria o Viewholder e guarda a inst�ncia dos objetos
                holder = new ViewHolder();
                holder.tvId = (TextView) convertView.findViewById(id.txtIdV);
                holder.tvNome = (TextView) convertView.findViewById(id.txtNomeV);
                convertView.setTag(holder);
            } else {
                //pega o ViewHolder para ter um acesso r�pido aos objetos do XML
                //ele sempre passar� por aqui quando,por exemplo, for efetuado uma rolagem na tela 
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvId.setText((String.valueOf(vendedor.getId())));
            holder.tvNome.setText(vendedor.getNome());
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
     
    //Criada esta classe est�tica para guardar a refer�ncia dos objetos abaixo
    static class ViewHolder {
    	public TextView tvId;
        public TextView tvNome;
       
    }    
}

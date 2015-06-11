package br.com.crudsqlliteandroid.DAO;

import java.util.ArrayList;
import java.util.List;

import br.com.crudsqlliteandroid.POJO.VendedorVO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class VendedorDAO {
	
	 private SQLiteDatabase database;
	 private BaseDAO dbHelper;

	    //Campos da tabela Agenda
	    private String[] colunas = {BaseDAO.VENDEDOR_ID, 
	                                BaseDAO.VENDEDOR_NOME};
	 
	    public VendedorDAO(Context context) {
	        dbHelper = new BaseDAO(context);
	    }
	 
	    public void open() throws SQLException {
	        database = dbHelper.getWritableDatabase();
	    }
	 
	    public void close() {
	        dbHelper.close();
	    }
	 
	    public long Inserir(VendedorVO pValue) {
	        ContentValues values = new ContentValues();
	 
	        //Carregar os valores nos campos do Contato que será incluído
	        values.put(BaseDAO.VENDEDOR_NOME, pValue.getNome());
	        return database.insert(BaseDAO.TBL_VENDEDOR, null, values);
	    }
	     
	     
	    public int Alterar(VendedorVO pValue) {
	        long id = pValue.getId();
	        ContentValues values = new ContentValues();
	         
	        //Carregar os novos valores nos campos que serão alterados
	        values.put(BaseDAO.VENDEDOR_NOME, pValue.getNome());
	        //Alterar o registro com base no ID
	        return database.update(BaseDAO.TBL_VENDEDOR, values, BaseDAO.VENDEDOR_ID + " = " + id, null);
	    }
	 
	    public void Excluir(VendedorVO pValue) {
	        long id = pValue.getId();
	         
	        //Exclui o registro com base no ID
	        database.delete(BaseDAO.TBL_VENDEDOR, BaseDAO.VENDEDOR_ID + " = " + id, null);
	    }
	 
	    public List<VendedorVO> Consultar() {
	        List<VendedorVO> lstVendedor = new ArrayList<VendedorVO>();
	 
	        //Consulta para trazer todos os dados da tabela vendedor ordenados pela coluna Nome
	        Cursor cursor = database.query(BaseDAO.TBL_VENDEDOR, colunas, 
	                null, null, null, null, BaseDAO.VENDEDOR_ID);
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	        	VendedorVO lVendedorVO = cursorToVendedor(cursor);
	        	lstVendedor.add(lVendedorVO);
	            cursor.moveToNext();
	        }
	         
	        //Tenha certeza que você fechou o cursor
	        cursor.close();
	        return lstVendedor;
	    }
	    
	   /* public List<VendedorVO> ConsultarId(String id) {
	    	
	    	String[] args = new String[]{id};
	    	List<VendedorVO> lstVendedor = new ArrayList<VendedorVO>();
	   	 
	        //Consulta para trazer todos os dados da tabela vendedor ordenados pela coluna Nome
	    	Cursor cursor = database.query(BaseDAO.TBL_VENDEDOR, colunas,BaseDAO.VENDEDOR_ID + "=?", args, null, null, null);
	        
	        if (cursor.getColumnCount()>0) {
	        	cursor.moveToFirst();
	        	VendedorVO lVendedorVO = cursorToVendedor(cursor);
	        	lstVendedor.add(lVendedorVO);
	        }
	         
	        //Tenha certeza que você fechou o cursor
	        cursor.close();
	        return lstVendedor;
	    }*/
	    public String ConsultarId(String id) {
	    	String nome=null;
	    	String[] args = new String[]{id};
	        //Consulta para trazer todos os dados da tabela vendedor ordenados pela coluna Nome
	        Cursor cursor = database.query(BaseDAO.TBL_VENDEDOR, colunas,BaseDAO.VENDEDOR_ID + "=?",args, null, null, null);
	        
	        if (cursor.getColumnCount()>0) {
	        	cursor.moveToFirst();
	        	VendedorVO lVendedorVO = cursorToVendedor(cursor);
	        	nome=lVendedorVO.getNome();
	        }
	         
	        //Tenha certeza que você fechou o cursor
	        cursor.close();
	        return nome;
	    }
	    
	    //Converter o Cursor de dados no objeto POJO VendedorVO
	    private VendedorVO cursorToVendedor(Cursor cursor) {
	    	VendedorVO lVendedorVO = new VendedorVO();
	        lVendedorVO.setId(cursor.getLong(0));
	        lVendedorVO.setNome(cursor.getString(1));
	        return lVendedorVO;
	    }
	}

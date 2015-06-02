package br.com.crudsqlliteandroid.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.crudsqlliteandroid.POJO.ProdutoVO;

public class ProdutoDAO {
	 private SQLiteDatabase database;
	 private BaseDAO dbHelper;

	    //Campos da tabela Agenda
	    private String[] colunas = {BaseDAO.ESTOQUE_ID, 
	                                BaseDAO.ESTOQUE_DESCRICAO,
	                                BaseDAO.ESTOQUE_PRECO,
	                                BaseDAO.ESTOQUE_UNIDADE,
	                                BaseDAO.ESTOQUE_REQPRODUCAO,
	                                BaseDAO.ESTOQUE_PERALTERAR,
	                                BaseDAO.ESTOQUE_CODIGOBARRA,};
	 
	    public ProdutoDAO(Context context) {
	        dbHelper = new BaseDAO(context);
	    }
	 
	    public void open() throws SQLException {
	        database = dbHelper.getWritableDatabase();
	    }
	 
	    public void close() {
	        dbHelper.close();
	    }
	 
	    public long Inserir(ProdutoVO pValue) {
	        ContentValues values = new ContentValues();
	 
	        //Carregar os valores nos campos do Contato que será incluído
	        values.put(BaseDAO.ESTOQUE_DESCRICAO, pValue.getDescricao());
	        values.put(BaseDAO.ESTOQUE_PRECO, pValue.getPreco());
	        values.put(BaseDAO.ESTOQUE_UNIDADE, pValue.getUnidade());
	        values.put(BaseDAO.ESTOQUE_REQPRODUCAO, pValue.getReqproducao());
	        values.put(BaseDAO.ESTOQUE_PERALTERAR, pValue.getPeralterar());
	        values.put(BaseDAO.ESTOQUE_CODIGOBARRA, pValue.getCodbarra());
	        
	        return database.insert(BaseDAO.TBL_ESTOQUE, null, values);
	    }
	     
	     
	    public int Alterar(ProdutoVO pValue) {
	        long id = pValue.getId();
	        ContentValues values = new ContentValues();
	         
	        //Carregar os novos valores nos campos que serão alterados
	        values.put(BaseDAO.ESTOQUE_DESCRICAO, pValue.getDescricao());
	        values.put(BaseDAO.ESTOQUE_PRECO, pValue.getPreco());
	        values.put(BaseDAO.ESTOQUE_UNIDADE, pValue.getUnidade());
	        values.put(BaseDAO.ESTOQUE_REQPRODUCAO, pValue.getReqproducao());
	        values.put(BaseDAO.ESTOQUE_PERALTERAR, pValue.getPeralterar());
	        values.put(BaseDAO.ESTOQUE_CODIGOBARRA, pValue.getCodbarra());
	        //Alterar o registro com base no ID
	        return database.update(BaseDAO.TBL_ESTOQUE, values, BaseDAO.ESTOQUE_ID + " = " + id, null);
	    }
	 
	    public void Excluir(ProdutoVO pValue) {
	        long id = pValue.getId();
	         
	        //Exclui o registro com base no ID
	        database.delete(BaseDAO.TBL_ESTOQUE, BaseDAO.ESTOQUE_ID + " = " + id, null);
	    }
	 
	    public List<ProdutoVO> Consultar() {
	        List<ProdutoVO> lstProduto = new ArrayList<ProdutoVO>();
	 
	        //Consulta para trazer todos os dados da tabela vendedor ordenados pela coluna Nome
	        Cursor cursor = database.query(BaseDAO.TBL_ESTOQUE, colunas, 
	                null, null, null, null, BaseDAO.ESTOQUE_ID);
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	        	ProdutoVO lProdutoVO = cursorToProduto(cursor);
	        	lstProduto.add(lProdutoVO);
	            cursor.moveToNext();
	        }
	         
	        //Tenha certeza que você fechou o cursor
	        cursor.close();
	        return lstProduto;
	    }
	 
	    //Converter o Cursor de dados no objeto POJO VendedorVO
	    private ProdutoVO cursorToProduto(Cursor cursor) {
	    	ProdutoVO lProdutoVO = new ProdutoVO();
	        lProdutoVO.setId(cursor.getLong(0));
	        lProdutoVO.setDescricao(cursor.getString(1));
	        lProdutoVO.setPreco(cursor.getDouble(2));
	        lProdutoVO.setUnidade(cursor.getString(3));
	        lProdutoVO.setReqproducao(cursor.getString(4));
	        lProdutoVO.setPeralterar(cursor.getString(5));
	        lProdutoVO.setCodbarra(cursor.getString(6));
	        return lProdutoVO;
	    }
	}


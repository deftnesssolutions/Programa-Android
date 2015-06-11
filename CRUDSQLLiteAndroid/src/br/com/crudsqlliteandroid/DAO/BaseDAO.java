package br.com.crudsqlliteandroid.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//Classe responsável pela criação do Banco de Dados e tabelas
public class BaseDAO extends SQLiteOpenHelper {

public static final String TBL_VENDEDOR = "vendedor";
public static final String VENDEDOR_ID = "_id";
public static final String VENDEDOR_NOME = "nome";

public static final String TBL_ESTOQUE = "estoque";
public static final String ESTOQUE_ID = "_id";
public static final String ESTOQUE_DESCRICAO = "descricao";
public static final String ESTOQUE_PRECO = "preco";
public static final String ESTOQUE_UNIDADE = "unidade";
public static final String ESTOQUE_REQPRODUCAO = "reqproducao";
public static final String ESTOQUE_PERALTERAR = "peralterar";
public static final String ESTOQUE_CODIGOBARRA = "codigobarra";

public static final String TBL_PREVENDA = "prevenda";
public static final String PREVENDA_ID = "_id";
public static final String PREVENDA_MESA = "mesa";
public static final String PREVENDA_FECHADA = "fechada";
public static final String PREVENDA_DATA = "data";
public static final String PREVENDA_NOMECLIENTE = "nomecliente";

public static final String TBL_PREVENDADETALHE = "prevendadetalhe";
public static final String PREVENDADETALHE_PREVENDAID = "prevendaid";
public static final String PREVENDADETALHE_PRODORDEM = "prodordem";
public static final String PREVENDADETALHE_PRODUTOID = "produtoid";
public static final String PREVENDADETALHE_QUANTIDADE = "quantidade";
public static final String PREVENDADETALHE_UNITARIO = "unitario";
public static final String PREVENDADETALHE_SITUACAO = "situacao";
public static final String PREVENDADETALHE_BARRA = "barra";
public static final String PREVENDADETALHE_LINHAID = "linhaid";
public static final String PREVENDADETALHE_ATENDENTE = "atendente";

public static final String TBL_DETALHECOMPLEMENTO = "detalhecomplemento";
public static final String DETALHECOMPLEMENTO_LINHAID = "linhaid";
public static final String DETALHECOMPLEMENTO_DETALHETIPO = "detalhetipo";
public static final String DETALHECOMPLEMENTO_DETDESCRICAO = "detdescricao";

private static final String DATABASE_NAME = "prevenda.db";
private static final int DATABASE_VERSION = 1;

//Estrutura da tabela Vendedor (sql statement)
private static final String CREATE_VENDEDOR = "create table " +
    TBL_VENDEDOR + "( " + VENDEDOR_ID       + " integer primary key autoincrement, " + 
                          VENDEDOR_NOME     + " text not null);";

//Estrutura da tabela Estoque (sql statement)
private static final String CREATE_ESTOQUE = "create table " +
  TBL_ESTOQUE + "( " + ESTOQUE_ID             + " integer primary key autoincrement, " + 
					   ESTOQUE_DESCRICAO      + " text not null, " +
					   ESTOQUE_PRECO          + " real not null, " +
					   ESTOQUE_UNIDADE        + " text not null, " +
					   ESTOQUE_REQPRODUCAO + " text not null, " +
					   ESTOQUE_PERALTERAR + " text not null, " +
					   ESTOQUE_CODIGOBARRA    + " text not null);";

//Estrutura da tabela Prevenda (sql statement)
private static final String CREATE_PREVENDA = "create table " +
  TBL_PREVENDA + "( " + PREVENDA_ID        + " integer primary key autoincrement, " + 
					  PREVENDA_MESA        + " text not null, " +
					  PREVENDA_FECHADA     + " text not null, " +
					  PREVENDA_DATA        + " text not null, " +
					  PREVENDA_NOMECLIENTE + " text not null);";

//Estrutura da tabela PrevendaDetalhe (sql statement)
private static final String CREATE_PREVENDADETALHE = "create table " +
  TBL_PREVENDADETALHE + "( " + PREVENDADETALHE_PREVENDAID   + " integer not null, " + 
							   PREVENDADETALHE_PRODORDEM + " integer not null, " +
							   PREVENDADETALHE_PRODUTOID    + " integer not null, " +
							   PREVENDADETALHE_QUANTIDADE   + " real not null, " +
							   PREVENDADETALHE_UNITARIO     + " real not null, " +
							   PREVENDADETALHE_SITUACAO     + " text not null, " +
							   PREVENDADETALHE_BARRA        + " text not null, " +
							   PREVENDADETALHE_LINHAID      + " integer primary key autoincrement, " +
							   PREVENDADETALHE_ATENDENTE    + " integer not null);";

//Estrutura da tabela DetalheComplemento (sql statement)
private static final String CREATE_DETALHECOMPLEMENTO = "create table " +
  TBL_DETALHECOMPLEMENTO + "( " + DETALHECOMPLEMENTO_LINHAID       + " integer not null, " + 
  								  DETALHECOMPLEMENTO_DETALHETIPO        + " text not null, " +
  								  DETALHECOMPLEMENTO_DETDESCRICAO + " text not null);";

public BaseDAO(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
}


public void onCreate(SQLiteDatabase database) {
    //Criação das tabelas
    database.execSQL(CREATE_VENDEDOR);
    database.execSQL(CREATE_ESTOQUE);
    database.execSQL(CREATE_PREVENDA);
    database.execSQL(CREATE_PREVENDADETALHE);
    database.execSQL(CREATE_DETALHECOMPLEMENTO);
}

public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //Caso seja necessário mudar a estrutura da tabela
    //deverá primeiro excluir a tabela e depois recriá-la
    db.execSQL("DROP TABLE IF EXISTS " + TBL_VENDEDOR);
    db.execSQL("DROP TABLE IF EXISTS " + TBL_ESTOQUE);
    db.execSQL("DROP TABLE IF EXISTS " + TBL_PREVENDA);
    db.execSQL("DROP TABLE IF EXISTS " + TBL_PREVENDADETALHE);
    db.execSQL("DROP TABLE IF EXISTS " + TBL_DETALHECOMPLEMENTO);
    onCreate(db);
}

}

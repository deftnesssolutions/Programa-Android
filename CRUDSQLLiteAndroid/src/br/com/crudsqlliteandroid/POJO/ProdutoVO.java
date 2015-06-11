package br.com.crudsqlliteandroid.POJO;

import java.io.Serializable;

public class ProdutoVO implements Serializable{
	private static final long serialVersionUID = 1L;
	  private long id;
	  private String descricao;
	  private Double preco;
	  private String unidade;
	  private String reqproducao;
	  private String peralterar;
	  private String codbarra;
	  
	  
	  public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public Double getPreco() {
		return preco;
	}


	public void setPreco(Double preco) {
		this.preco = preco;
	}


	public String getUnidade() {
		return unidade;
	}


	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}


	public String getReqproducao() {
		return reqproducao;
	}


	public void setReqproducao(String reqproducao) {
		this.reqproducao = reqproducao;
	}


	public String getPeralterar() {
		return peralterar;
	}


	public void setPeralterar(String peralterar) {
		this.peralterar = peralterar;
	}


	public String getCodbarra() {
		return codbarra;
	}


	public void setCodbarra(String codbarra) {
		this.codbarra = codbarra;
	}


	// Will be used by the ArrayAdapter in the ListView
	  
	  public String toString() {
	      return descricao;
	  }
	}


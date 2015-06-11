package br.com.crudsqlliteandroid.POJO;

import java.io.Serializable;

public class VendedorVO implements Serializable{
	public VendedorVO(long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public VendedorVO() {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	  private long id;
	  private String nome;

	  public long getId() {
	      return id;
	  }

	  public void setId(long id) {
	      this.id = id;
	  }

	  public String getNome() {
	      return nome;
	  }

	  public void setNome(String value) {
	      this.nome = value;
	  }
	  
	  // Will be used by the ArrayAdapter in the ListView
	  public String toString() {
	      return nome;
	  }
	}

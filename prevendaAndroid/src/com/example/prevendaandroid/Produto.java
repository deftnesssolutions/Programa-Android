package com.example.prevendaandroid;

public class Produto 
{
	private String produtoCodigo;
    private String descricao; 
    
    public Produto(String produtoCodigo, String descricao)
    {
    	this.produtoCodigo= produtoCodigo;
    	this.descricao= descricao;
    }

	public String getProdutoCodigo() {
		return produtoCodigo;
	}

	public void setProdutoCodigo(String produtoCodigo) {
		this.produtoCodigo = produtoCodigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}

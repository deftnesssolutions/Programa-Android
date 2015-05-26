package com.example.prevendaandroid;

public class Consumo
{
	private String produtoCodigo;
	private String produtoBarra;
    private String produtoOrdem; 
    private String descricao; 
    private String produtoQuantidade; 
    private String produtoUnitario; 
    private String total;
    private String produtoSituacao; 
    
    public Consumo(String produtoCodigo,String produtoBarra,String produtoOrdem,String descricao,String produtoQuantidade,String produtoUnitario,
    		String total,String produtoSituacao	)
    {
    	this.produtoCodigo=produtoCodigo;
    	this.produtoBarra=produtoBarra;
    	this.produtoOrdem =produtoOrdem;
    	this.descricao=descricao;
    	this.produtoQuantidade=produtoQuantidade;
    	this.produtoUnitario=produtoUnitario;
    	this.total=total;
    	this.produtoSituacao=produtoSituacao;
    	
    }

	public String getProdutoCodigo() {
		return produtoCodigo;
	}

	public void setProdutoCodigo(String produtoCodigo) {
		this.produtoCodigo = produtoCodigo;
	}

	public String getProdutoBarra() {
		return produtoBarra;
	}

	public void setProdutoBarra(String produtoBarra) {
		this.produtoBarra = produtoBarra;
	}

	public String getProdutoOrdem() {
		return produtoOrdem;
	}

	public void setProdutoOrdem(String produtoOrdem) {
		this.produtoOrdem = produtoOrdem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getProdutoQuantidade() {
		return produtoQuantidade;
	}

	public void setProdutoQuantidade(String produtoQuantidade) {
		this.produtoQuantidade = produtoQuantidade;
	}

	public String getProdutoUnitario() {
		return produtoUnitario;
	}

	public void setProdutoUnitario(String produtoUnitario) {
		this.produtoUnitario = produtoUnitario;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
    
	public String getProdutoSituacao() {
		return produtoSituacao;
	}

	public void setProdutoSituacao(String produtoSituacao) {
		this.produtoSituacao = produtoSituacao;
	}
}


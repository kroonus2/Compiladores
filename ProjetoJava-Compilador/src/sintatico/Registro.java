package sintatico;

public class Registro {

	private String lexema;
	private Categoria categoria = Categoria.INDEFINIDA;
	private int offSet;
	private Tipo tipo = Tipo.INDEFINIDO; 

	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return 
		"\nlexema: " + lexema + 
		"\ncategoria: " + categoria.getDescricao() + 
		"\noffSet: " + offSet +
		"\ntipo: "+ tipo.getDescricao();
	}

}
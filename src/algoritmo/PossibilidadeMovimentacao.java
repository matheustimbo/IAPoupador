package algoritmo;

public class PossibilidadeMovimentacao {
	String direcao;
	int peso = Integer.MAX_VALUE;
	boolean viavel;
	public PossibilidadeMovimentacao(String direcao, boolean viavel){
		this.direcao = direcao;
		this.viavel = viavel;
	}

	public String getDirecao() {
		return direcao;
	}

	public void setDirecao(String direcao) {
		this.direcao = direcao;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	public boolean isViavel() {
		return viavel;
	}

	public void setViavel(boolean viavel) {
		this.viavel = viavel;
	}
}

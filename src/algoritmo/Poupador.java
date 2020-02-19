package algoritmo;

public class Poupador extends ProgramaPoupador {

	int[][] memoriaExploracao = new int[30][30];

	public int acao() {
		int x = sensor.getPosicao().x;
		int y = sensor.getPosicao().y;
		memoriaExploracao[x][y]++;
		return explorar(x, y);
	}

	//0 parado
	//1 cima
	//2 baixo
	//3 direita
	//4 esquerda
	// y -1 subir
	// x -1 esquerda
	// y +1 descer
	// x +1 direita

	int explorar(int x, int y){
		int menorPeso = Integer.MAX_VALUE;
		int direcaoMenorPeso = 0;

		for(int i =1; i< 5; i++){ //1 cima 2 baixo 3 direita 4esquerda
			int indiceVisao = 0;
			int xDirecao = x;
			int yDirecao = y;
			if(i == 1) {
				indiceVisao = 7;
				yDirecao--;
			}
			if(i == 2) {
				indiceVisao = 16;
				yDirecao++;
			}
			if(i == 3){
				indiceVisao = 12;
				xDirecao++;
			}
			if( i == 4) {
				indiceVisao = 11;
				xDirecao--;
			}
			if(isCaminhoViavel(sensor.getVisaoIdentificacao()[indiceVisao])){
				if(getPesoMemoria(xDirecao, yDirecao) < menorPeso){
					System.out.println("peso memoria ["+xDirecao+"]["+yDirecao+"] = " + getPesoMemoria(xDirecao, yDirecao));
					menorPeso = getPesoMemoria(x, y);
					direcaoMenorPeso = i;
				}
			}
		}
		System.out.println("direcao menor peso: " + direcaoMenorPeso);
		System.out.println("teste acima: " + sensor.getVisaoIdentificacao()[7]);
		System.out.println("teste abaixo: " + sensor.getVisaoIdentificacao()[16]);
		System.out.println("teste direita: " + sensor.getVisaoIdentificacao()[12]);
		System.out.println("teste esquerda: " + sensor.getVisaoIdentificacao()[11]);

		return direcaoMenorPeso;
	}

	boolean isCaminhoViavel(int indice){
		return indice == 0 || indice == 4;
	}

	int getPesoMemoria(int x, int y){
		return memoriaExploracao[x][y];
	}
}
package algoritmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Poupador extends ProgramaPoupador {

	int cont = 0;
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
		Map pesosCaminhos = new HashMap();
		for(int i =1; i< 5; i++){ //1 cima 2 baixo 3 direita 4esquerda
			int indiceVisao = -2;
			int xDirecao = x;
			int yDirecao = y;
			if(i == 1) {
				indiceVisao = 7;
				yDirecao--;
				if (isCaminhoViavel(sensor.getVisaoIdentificacao()[indiceVisao])) {
					int pesoCima = getPesoMemoria(xDirecao, yDirecao);
					System.out.println("Peso cima: " + pesoCima);
					pesosCaminhos.put(pesoCima, 1);
				}
			}
			if(i == 2) {
				indiceVisao = 16;
				yDirecao++;
				if (isCaminhoViavel(sensor.getVisaoIdentificacao()[indiceVisao])) {
					int pesoBaixo = getPesoMemoria(xDirecao, yDirecao);
					System.out.println("Peso baixo: " + pesoBaixo);
					pesosCaminhos.put(pesoBaixo, 2);
				}
			}
			if(i == 3){
				indiceVisao = 12;
				xDirecao++;
				if (isCaminhoViavel(sensor.getVisaoIdentificacao()[indiceVisao])) {
					int pesoDireita = getPesoMemoria(xDirecao, yDirecao);
					System.out.println("Peso direita: " + pesoDireita);
					pesosCaminhos.put(pesoDireita, 3);
				}
			}
			if( i == 4) {
				indiceVisao = 11;
				xDirecao--;
				if (isCaminhoViavel(sensor.getVisaoIdentificacao()[indiceVisao])) {
					int pesoEsquerda = getPesoMemoria(xDirecao, yDirecao);
					System.out.println("Peso esquerda: " + pesoEsquerda);
					pesosCaminhos.put(pesoEsquerda, 4);
				}
			}

			if (sensor.getVisaoIdentificacao()[indiceVisao] == 1) {
				memoriaExploracao[x][y] = Integer.MAX_VALUE;
			}else if(isCaminhoViavel(sensor.getVisaoIdentificacao()[indiceVisao])){
				if(getPesoMemoria(xDirecao, yDirecao) < menorPeso){
					System.out.println("peso memoria ["+xDirecao+"]["+yDirecao+"] = " + getPesoMemoria(xDirecao, yDirecao));
					menorPeso = getPesoMemoria(x, y);
					direcaoMenorPeso = i;
				}
			}
		}

		for (int j = 0; j < 30; j++) {
			for (int i = 0; i < 30; i++) {
				cont++;
				if (memoriaExploracao[i][j] < Integer.MAX_VALUE) {
					if((i==x) && (j == y)){
						System.out.print("@ ");
					}else{
						System.out.print(memoriaExploracao[i][j] + " ");
					}
				} else {
					System.out.print("x ");
				}
				if (cont % 30 == 0) {
					System.out.println();
				}

			}
		}

		System.out.println("posição atual: " + x + ", " + y);
		System.out.println("direcao menor peso: " + direcaoMenorPeso);
		System.out.println("teste acima: " + sensor.getVisaoIdentificacao()[7]);
		System.out.println("teste abaixo: " + sensor.getVisaoIdentificacao()[16]);
		System.out.println("teste direita: " + sensor.getVisaoIdentificacao()[12]);
		System.out.println("teste esquerda: " + sensor.getVisaoIdentificacao()[11]);
		System.out.println("== retorno: " + direcaoMenorPeso + " ==");
		System.out.println("*******************************************************");

		return direcaoMenorPeso;
	}

	int getDirecaoMenorPeso(int menorPeso) {



		return 0;
	}

	boolean isCaminhoViavel(int indice){
		return indice == 0 || indice == 4;
	}

	int getPesoMemoria(int x, int y){
		return memoriaExploracao[x][y];
	}
}
package algoritmo;

public class Poupador extends ProgramaPoupador {

	static int index = 0;

	int[][] memoriaExploracao = new int[30][30];

	public String legenda(int numero){
		if(numero == -2){
			return "Sem visão para o local";
		}
		if(numero == -1){
			return "Fora do ambiente";
		}
		if(numero == 0){
			return "Celula vazia";
		}
		if(numero == 1){
			return "Parede";
		}
		if(numero == 3){
			return "Banco";
		}
		if(numero == 4){
			return "Moeda";
		}
		if(numero == 5){
			return "Pastilha poder";
		}
		if(numero > 100 && numero < 200){
			return "Poupador";
		} else {
			return "Ladrão";
		}
	}

	public int acao() {
		index++;
		if(index % 2 != 0){
			System.out.println("posicao");
			System.out.println(sensor.getPosicao().x);
			System.out.println(sensor.getPosicao().y);
		}
		memoriaExploracao[sensor.getPosicao().x][sensor.getPosicao().y]++;
		int acao = localMenosExplorado();
		System.out.println("ação: "+ acao);
		return acao;
		//0 parado
		//1 cima
		//2 baixo
		//3 direita
		//4 esquerda
		// y -1 subir
		// x -1 esquerda
		// y +1 descer
		// x +1 direita
	}

	/*
		y>0 da p subir
		y<30 da p descer
		x>0 da p ir p esquerda
		x<30 da p ir p direita
	 */

	boolean isCaminhoViavel(int indice){
		return indice == 0 || indice == 4 || indice == 5 ;
	}

	int localMenosExplorado(){
		int x = sensor.getPosicao().x;
		int y = sensor.getPosicao().y;
		PossibilidadeMovimentacao[] possibilidadesMovimentacao = new PossibilidadeMovimentacao[4];
		possibilidadesMovimentacao[0] = new PossibilidadeMovimentacao("acima", y>1);
		possibilidadesMovimentacao[1] = new PossibilidadeMovimentacao("abaixo", y<29);
		possibilidadesMovimentacao[2] = new PossibilidadeMovimentacao("esquerda",  x>1);
		possibilidadesMovimentacao[3] = new PossibilidadeMovimentacao("direita", x<29);
		for(int i=0; i< 3; i++){
			int direcao = 0;
			if(possibilidadesMovimentacao[i].direcao == "acima")
				direcao = 7;
			if(possibilidadesMovimentacao[i].direcao == "esquerda")
				direcao = 11;
			if(possibilidadesMovimentacao[i].direcao == "descer")
				direcao = 16;
			if(possibilidadesMovimentacao[i].direcao == "direita")
				direcao = 12;
			possibilidadesMovimentacao[i].setViavel(isCaminhoViavel(sensor.getVisaoIdentificacao()[direcao]));

			if(possibilidadesMovimentacao[i].isViavel()){
				int peso = Integer.MAX_VALUE;
				int direcaoX = 0;
				int direcaoY = 0;
				if(possibilidadesMovimentacao[i].direcao == "acima"){
					direcaoX = x;
					direcaoY = y-1;
				}

				if(possibilidadesMovimentacao[i].direcao == "esquerda"){
					direcaoX = x;
					direcaoY = y-1;
				}

				if(possibilidadesMovimentacao[i].direcao == "descer"){
					direcaoX = x;
					direcaoY = y-1;
				}

				if(possibilidadesMovimentacao[i].direcao == "direita"){
					direcaoX = x;
					direcaoY = y-1;
				}
				if(direcaoX < 0 || direcaoX > 29 || direcaoY < 0 || direcaoY >29 ){
					possibilidadesMovimentacao[i].setPeso(Integer.MAX_VALUE);
				} else {
					possibilidadesMovimentacao[i].setPeso(memoriaExploracao[direcaoX][direcaoY]);
				}
			}
		}

		int indexDoMenorPeso = 0;
		int pesoDoMenorPeso = Integer.MAX_VALUE;
		for(int i=0; i<=3; i++){
			if(possibilidadesMovimentacao[i].getPeso() < pesoDoMenorPeso && possibilidadesMovimentacao[i].isViavel()){
				if(index % 2 != 0 ) {
					System.out.println("peso: " + possibilidadesMovimentacao[i].getPeso());
					System.out.println("index: " + i);
					System.out.println("direcao: " + possibilidadesMovimentacao[i].getDirecao());
				}
				pesoDoMenorPeso = possibilidadesMovimentacao[i].getPeso();
				indexDoMenorPeso = i;
			}
		}


		return indexDoMenorPeso + 1;
	}





	void decidirLugarExplorar(){
		System.out.println("acima");
		System.out.println(legenda(sensor.getVisaoIdentificacao()[7]));
		int[] possibilidades = {sensor.getVisaoIdentificacao()[7], sensor.getVisaoIdentificacao()[11], sensor.getVisaoIdentificacao()[12], sensor.getVisaoIdentificacao()[16]};
		int countPossibilidades = 0;
		for(int i= 0; i< possibilidades.length; i++){
			if(possibilidades[i] != 1 && //parede
				possibilidades[i] <200 && //ladrao
				possibilidades[i] != -1 //mundo exterior
			){
				countPossibilidades++;
			}
		}
		
	}

	public void printMemoria(){
		System.out.println("mapa");
		for (int i=0; i<memoriaExploracao.length; i++){
			for (int j=0; j<memoriaExploracao.length; j++){
				if(j == memoriaExploracao.length-1){
					System.out.println(memoriaExploracao[i][j]);
				}else{
					System.out.print(memoriaExploracao[i][j]);
				}

			}
		}
	}

}
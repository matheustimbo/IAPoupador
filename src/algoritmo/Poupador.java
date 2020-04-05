package algoritmo;
import java.util.ArrayList;
import java.util.Random;

public class Poupador extends ProgramaPoupador {

	PossibilidadeMovimento acima, esquerda, direita, baixo;

	public class PossibilidadeMovimento {
		boolean isViavel;
		int x;
		int y;
		Visao visao;
		int coordenadaVisao;
		int comandoMovimento;
		public PossibilidadeMovimento(boolean isViavel, int x, int y, Visao visao, int coordenadaVisao, int comandoMovimento){
			this.isViavel = isViavel;
			this.x = x;
			this.y = y;
			this.visao = visao;
			this.coordenadaVisao = coordenadaVisao;
			this.comandoMovimento = comandoMovimento;
		}
	}

	void mapearMovimentos(){
		int x = sensor.getPosicao().x;
		int y = sensor.getPosicao().y;
		System.out.println("x: " + x + " y: " + y);
		acima = new PossibilidadeMovimento(movimentoIsViavel(visaoacima), x, y-1, getResultVisao(sensor.getVisaoIdentificacao()[visaoacima]), visaoacima, movercima);
		esquerda = new PossibilidadeMovimento(movimentoIsViavel(visaoesquerda), x-1, y, getResultVisao(sensor.getVisaoIdentificacao()[visaoesquerda]), visaoesquerda, moveresquerda);
		direita = new PossibilidadeMovimento(movimentoIsViavel(visaodireita), x+1, y, getResultVisao(sensor.getVisaoIdentificacao()[visaodireita]), visaodireita, moverdireita);
		baixo = new PossibilidadeMovimento(movimentoIsViavel(visaobaixo), x, y+1, getResultVisao(sensor.getVisaoIdentificacao()[visaobaixo]), visaobaixo, moverbaixo);
	}

	public Objetivo objetivo;

	public enum Objetivo {
		FUGIR,
		EXPLORAR,
		GUARDAR_MOEDAS,
	}

	public enum Visao {
		SEMVISAO(-2),
		FORA(-1),
		VAZIO(0),
		PAREDE(1),
		BANCO(3),
		MOEDA(4),
		PASTINHA(5),
		POUPADOR(100),
		LADRAO(200);
		private final int valor;
		Visao(int i){
			valor = i;
			}
			public int get(){
				return valor;
			}
	}

	public Visao getResultVisao(int i){
		switch(i){
			case -2:
				return Visao.SEMVISAO;
			case -1:
				return Visao.FORA;
			case 0:
				return Visao.VAZIO;
			case 1:
				return Visao.PAREDE;
			case 3:
				return Visao.BANCO;
			case 4:
				return Visao.MOEDA;
			case 5:
				return Visao.PASTINHA;
			case 100:
				return Visao.POUPADOR;
			case 200:
				return Visao.LADRAO;
			default:
				return Visao.VAZIO;
		}
	}

	int visaoacima = 7;
	int visaoesquerda = 11;
	int visaodireita = 12;
	int visaobaixo = 16;

	int ficarparado = 0;
	int movercima = 1;
	int moverbaixo = 2;
	int moverdireita = 3;
	int moveresquerda = 4;

	int[][] memoria = new int[31][31];
	
	public int acao() {
		int x = sensor.getPosicao().x;
		int y = sensor.getPosicao().y;
		memoria[x][y]++;
		mapearMovimentos();
		this.objetivo = decidirObjetivo();


		return decidirAcaoProObjetivo(Objetivo.EXPLORAR);
	}

	int decidirAcaoProObjetivo(Objetivo objetivo){
		if(objetivo == Objetivo.EXPLORAR){
			return getLugarMenosExplorado();
		} else {
			return 0;
		}
	}

	int getLugarMenosExplorado(){
		int menorPeso = Integer.MAX_VALUE;
		if(acima.isViavel && memoria[acima.x][acima.y] < menorPeso){
			menorPeso = memoria[acima.x][acima.y];
		}
		if(esquerda.isViavel && memoria[esquerda.x][esquerda.y] < menorPeso){
			menorPeso = memoria[esquerda.x][esquerda.y];
		}
		if(direita.isViavel && memoria[direita.x][direita.y] < menorPeso){
			menorPeso = memoria[direita.x][direita.y];
		}
		if(baixo.isViavel && memoria[baixo.x][baixo.y] < menorPeso){
			menorPeso = memoria[baixo.x][baixo.y];
		}
		ArrayList<PossibilidadeMovimento> opcoesIguais = new ArrayList<>();
		if(acima.isViavel && memoria[acima.x][acima.y] == menorPeso){
			opcoesIguais.add(acima);
		}
		if(esquerda.isViavel && memoria[esquerda.x][esquerda.y] == menorPeso){
			opcoesIguais.add(esquerda);
		}
		if(direita.isViavel && memoria[direita.x][direita.y] == menorPeso){
			opcoesIguais.add(direita);
		}
		if(baixo.isViavel && memoria[baixo.x][baixo.y] == menorPeso){
			opcoesIguais.add(baixo);
		}
		Random random = new Random();
		if(opcoesIguais.size() == 0){
			return 0;
		} else {
			int indexrandom = random.nextInt(opcoesIguais.size());
			System.out.println("visoes das opcoes iguais");
			for(int i =0 ; i < opcoesIguais.size(); i++){
				System.out.println(opcoesIguais.get(i).visao);
			}
			return opcoesIguais.get(indexrandom).comandoMovimento;
		}

	}

	Objetivo decidirObjetivo(){
		//ArrayList<Visao> visao = getMovimentosPossiveis();

		return Objetivo.EXPLORAR;
	}
	
	boolean movimentoIsViavel(int coordenadaVisao){
		return getResultVisao(sensor.getVisaoIdentificacao()[coordenadaVisao]) == Visao.VAZIO || getResultVisao(sensor.getVisaoIdentificacao()[coordenadaVisao]) == Visao.MOEDA || getResultVisao(sensor.getVisaoIdentificacao()[coordenadaVisao]) == Visao.PASTINHA;
	}



}
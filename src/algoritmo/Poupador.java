package algoritmo;
import java.util.ArrayList;
import java.util.Random;

public class Poupador extends ProgramaPoupador {

    PossibilidadeMovimento acima, esquerda, direita, baixo, parado;
    static int teste = 0;
    static int xBanco = -1;
    static int yBanco = -1;
    public class PossibilidadeMovimento {
        boolean isViavel;
        int x;
        int y;
        Visao visao;
        int coordenadaVisao;
        int comandoMovimento;
        PossibilidadeMovimento oposto;

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
        acima = new PossibilidadeMovimento(movimentoIsViavel(visaoacima), x, y-1, getResultVisao(sensor.getVisaoIdentificacao()[visaoacima]), visaoacima, movercima);
        esquerda = new PossibilidadeMovimento(movimentoIsViavel(visaoesquerda), x-1, y, getResultVisao(sensor.getVisaoIdentificacao()[visaoesquerda]), visaoesquerda, moveresquerda);
        direita = new PossibilidadeMovimento(movimentoIsViavel(visaodireita), x+1, y, getResultVisao(sensor.getVisaoIdentificacao()[visaodireita]), visaodireita, moverdireita);
        baixo = new PossibilidadeMovimento(movimentoIsViavel(visaobaixo), x, y+1, getResultVisao(sensor.getVisaoIdentificacao()[visaobaixo]), visaobaixo, moverbaixo);
        parado = new PossibilidadeMovimento(true, x, y, Visao.VAZIO, 0, 0);
        acima.oposto = baixo;
        esquerda.oposto = direita;
        direita.oposto = esquerda;
        baixo.oposto = acima;
    }

    public Objetivo objetivo;

    public enum Objetivo {
        FUGIR,
        EXPLORAR,
        GUARDAR_MOEDAS,
        PARAR_DE_REPETIR
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

    int x, y, ultimox, ultimoy, penultimox, penultimoy;

    public int acao() {

        penultimox = ultimox;
        penultimoy = ultimoy;
        ultimox = x;
        ultimoy = y;
        x = sensor.getPosicao().x;
        y = sensor.getPosicao().y;
        memoria[x][y]++;
        mapearMovimentos();
        this.objetivo = decidirObjetivo();

        teste++;
        if(teste%2 != 0){
            System.out.println("Poupador 0 ------");
        }else{
            System.out.println("Poupador 1 ------");
        }
        System.out.println("objetivo: " + objetivo);
        System.out.println("ultimo xy" + ultimox + "," + ultimoy + " atual: " + x + "," + y );
        return decidirAcaoProObjetivo(Objetivo.EXPLORAR);
    }

    int decidirAcaoProObjetivo(Objetivo objetivo){
        Random random = new Random();
        if(objetivo == Objetivo.FUGIR){
            return tratarRepeticao(fugir());
        }
        if(objetivo == Objetivo.GUARDAR_MOEDAS){
            return tratarRepeticao(tentarBanco());
        } else
        if(objetivo == Objetivo.EXPLORAR){
            return tratarRepeticao(getLugarMenosExplorado());
        }
        return random.nextInt(5);
    }



    ArrayList<PossibilidadeMovimento> movimentosViaveisMenos(int removeindex){
        ArrayList<PossibilidadeMovimento> array = getMovimentosViaveis();
        array.remove(removeindex);
        return array;
    }

    PossibilidadeMovimento getPosicaoLadraoMovimentosPossiveis(){
        for(int i =0; i< getMovimentos().size(); i++){
            if(getMovimentos().get(i).visao == Visao.LADRAO){
                return getMovimentos().get(i);
            }
        }
        return null;
    }

    int tratarRepeticao(PossibilidadeMovimento movimento){
        if(penultimox == ultimox && penultimoy == ultimoy){
            System.out.println("ta repetindo");
            System.out.println("ultimo x " + ultimox + " x " + x);
            System.out.println("ultimo x " + ultimoy + " x " + y);
            return movimentoDiferente(movimento.x, movimento.y);
        } else {
            return movimento.comandoMovimento;
        }
    }

    int movimentoDiferente(int x, int y){
        ArrayList<PossibilidadeMovimento> movimentosViaveis = getMovimentosViaveis();
        if(movimentosViaveis.size() == 0){
            return 0;
        } else
        if(movimentosViaveis.size() == 1){
            return movimentosViaveis.get(0).comandoMovimento;
        } else {
            Random random = new Random();
            int randomNb = random.nextInt(movimentosViaveis.size());
            return movimentosViaveis.get(randomNb).comandoMovimento;
        }
    }

    ArrayList<PossibilidadeMovimento> getMovimentos(){
        ArrayList<PossibilidadeMovimento> movimentos = new ArrayList<>();
        movimentos.add(acima);
        movimentos.add(esquerda);
        movimentos.add(direita);
        movimentos.add(baixo);
        return movimentos;
    }

    ArrayList<PossibilidadeMovimento> getMovimentosViaveis(){
        ArrayList<PossibilidadeMovimento> movimentos = getMovimentos();
        for(int i =0; i< movimentos.size(); i++){
            if(!movimentos.get(i).isViavel){
                movimentos.remove(i);
            }
        }
        return movimentos;
    }

    boolean soTemVazioEMoeda(ArrayList<PossibilidadeMovimento> movimentos){
        int countvazios = 0;
        int countmoedas = 0;
        for(int i = 0; i< movimentos.size(); i++){
            if(movimentos.get(i).visao != Visao.MOEDA && movimentos.get(i).visao != Visao.VAZIO){
                return false;
            }
            if(movimentos.get(i).visao == Visao.MOEDA){
                countmoedas ++;
            }
            if(movimentos.get(i).visao == Visao.VAZIO){
                countvazios ++;
            }
        }
        if(countmoedas == 0 || countvazios == 0){
            return false;
        }
        return true;
    }

    PossibilidadeMovimento tentarBanco(){
        if(xBanco != -1 && yBanco != -1 || toVendoUmBanco()){ //ja vi um banco e lembro dele ou to vendo ele
            Double menorDistancia = Double.MAX_VALUE;
            for(int i = 0; i< getMovimentosViaveis().size(); i++){
                if(distanciaEntreDoisPontos(sensor.getPosicao().x, sensor.getPosicao().y, xBanco, yBanco) < menorDistancia){
                    menorDistancia = distanciaEntreDoisPontos(sensor.getPosicao().x, sensor.getPosicao().y, xBanco, yBanco);
                }
            }
            ArrayList<PossibilidadeMovimento> distanciasIguais = new ArrayList<>();
            for(int i =0; i< getMovimentosViaveis().size(); i++){
                if(distanciaEntreDoisPontos(sensor.getPosicao().x, sensor.getPosicao().y, xBanco, yBanco) == menorDistancia){
                    distanciasIguais.add(getMovimentosViaveis().get(i));
                }
            }
            Random random = new Random();
            int randomindex = random.nextInt(distanciasIguais.size());
            return distanciasIguais.get(randomindex);
        } else {
            return getLugarMenosExplorado();
        }

    }

    PossibilidadeMovimento fugir(){
        Random random = new Random();
        if(toVendoUmLadrao()){
            int xLadrao = sensor.getPosicao().x + diferencaXPraCoordenadaVisao(indexVisaoLadrao());
            int yLadrao = sensor.getPosicao().y + diferencaYPraCoordenadaVisao(indexVisaoLadrao());
            Double maiorDistancia = 0.0;
            for(int i = 0; i< getMovimentosViaveis().size(); i++){
                if(distanciaEntreDoisPontos(sensor.getPosicao().x, sensor.getPosicao().y, xLadrao, yLadrao) > maiorDistancia){
                    maiorDistancia = distanciaEntreDoisPontos(sensor.getPosicao().x, sensor.getPosicao().y, xLadrao, yLadrao);
                }
            }
            ArrayList<PossibilidadeMovimento> distanciasIguais = new ArrayList<>();
            for(int i =0; i< getMovimentosViaveis().size(); i++) {
                if (distanciaEntreDoisPontos(sensor.getPosicao().x, sensor.getPosicao().y, xLadrao, yLadrao) == maiorDistancia) {
                    distanciasIguais.add(getMovimentosViaveis().get(i));
                }
            }
            if(random.nextInt(5) < 5) {
                int randomindex = random.nextInt(distanciasIguais.size());
                return distanciasIguais.get(randomindex);
            } else {
                return getLugarMenosExplorado();
            }

        } else {
            return getLugarMenosExplorado();
        }
    }

    int diferencaXPraCoordenadaVisao(int i){
        if(i==2||i==7||i==16||i==21){
            return 0;
        }
        if(i==1||i==6||i==11||i==15||i==20){
            return -1;
        }
        if(i==0||i==5||i==10||i==14||i==19){
            return -2;
        }
        if(i==3||i==8||i==12||i==17||i==22){
            return 1;
        }
        if(i==4||i==9||i==13||i==18||i==23){
            return 2;
        }
        return 0;
    }

    int diferencaYPraCoordenadaVisao(int i){
        if(i<=4) {
            return -2;
        }
        if(i<=9){
            return -1;
        }
        if(i>=10 && i<=13){
            return 0;
        }
        if(i>=14 && i<=18){
            return 1;
        }
        if(i>18){
            return 2;
        }
        return 0;
    }

    int pesoCheiroLadroes(){
        int peso = 0;
        for(int i = 0; i < 7; i++){
            peso += sensor.getAmbienteOlfatoLadrao()[i];
        }
        return peso;
    }

    PossibilidadeMovimento getLugarMenosExplorado(){
        int menorPeso = Integer.MAX_VALUE;
        Random random = new Random();

        boolean aceitaPastilha = false;
        if(sensor.getNumeroDeMoedas() >= 5) {
            if (random.nextInt(10) + 4 < sensor.getNumeroDeMoedas() + pesoCheiroLadroes()) {
                aceitaPastilha = true;
            }
        }
        System.out.println("aceita pastilha: " + aceitaPastilha);

        if(acima.isViavel && memoria[acima.x][acima.y] < menorPeso){
            menorPeso = memoria[acima.x][acima.y];
            System.out.println("acima.visao " + acima.visao);
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
            if(acima.visao == Visao.PASTINHA){
                System.out.println("HMMMMMM PASTINHA");
            }
            if(aceitaPastilha){
                opcoesIguais.add(acima);
            } else {
                if(acima.visao != Visao.PASTINHA){
                    opcoesIguais.add(acima);
                }
            }
        }
        if(esquerda.isViavel && memoria[esquerda.x][esquerda.y] == menorPeso){
            if(aceitaPastilha){
                opcoesIguais.add(esquerda);
            } else {
                if(esquerda.visao != Visao.PASTINHA){
                    opcoesIguais.add(esquerda);
                }
            }
        }
        if(direita.isViavel && memoria[direita.x][direita.y] == menorPeso){
            if(aceitaPastilha){
                opcoesIguais.add(direita);
            } else {
                if(direita.visao != Visao.PASTINHA){
                    opcoesIguais.add(direita);
                }
            }
        }
        if(baixo.isViavel && memoria[baixo.x][baixo.y] == menorPeso){
            if(aceitaPastilha){
                opcoesIguais.add(baixo);
            }else{
                if(baixo.visao != Visao.PASTINHA){
                    opcoesIguais.add(baixo);
                }
            }
        }
        if(opcoesIguais.size() == 0){
            return parado;
        } else {
            if(soTemVazioEMoeda(opcoesIguais) && random.nextInt(20) > sensor.getNumeroDeMoedas()){
                ArrayList<PossibilidadeMovimento> moedas = new ArrayList<>();
                for(int i =0; i < opcoesIguais.size(); i++){
                    if(opcoesIguais.get(i).visao == Visao.MOEDA){
                        moedas.add(opcoesIguais.get(i));
                    }
                }
                int moedaRandom = random.nextInt(moedas.size());
                return moedas.get(moedaRandom);
            } else {
                int indexrandom = random.nextInt(opcoesIguais.size());
                for(int i =0 ; i < opcoesIguais.size(); i++){
                    System.out.println(opcoesIguais.get(i).visao);
                }
                return opcoesIguais.get(indexrandom);
            }
        }

    }

    boolean temBancoPossivel () {
        for(int i = 0; i < getMovimentosViaveis().size(); i++){
            if(getMovimentosViaveis().get(i).visao == Visao.BANCO){
                return true;
            }
        }
        return false;
    }

    boolean toVendoUmBanco(){
        boolean toVendo = false;
        for(int i =0; i< 24; i++){
            if(sensor.getVisaoIdentificacao()[i] == Visao.BANCO.get()){
                toVendo = true;
                xBanco = sensor.getPosicao().x + diferencaXPraCoordenadaVisao(i);
                yBanco = sensor.getPosicao().y + diferencaYPraCoordenadaVisao(i);
            }
        }
        return toVendo;
    }

    boolean toVendoUmLadrao(){
        boolean toVendo = false;
        for(int i =0; i< 24; i++){
            if(sensor.getVisaoIdentificacao()[i] == Visao.LADRAO.get()){
                toVendo = true;
            }
        }
        return toVendo;
    }

    int indexVisaoBanco(){
        for(int i =0; i< 24; i++){
            if(sensor.getVisaoIdentificacao()[i] == Visao.BANCO.get()){
                return i;
            }
        }
        return 0;
    }

    int indexVisaoLadrao(){
        for(int i =0; i< 24; i++){
            if(sensor.getVisaoIdentificacao()[i] == Visao.LADRAO.get()){
                return i;
            }
        }
        return 0;
    }

    Double distanciaEntreDoisPontos (int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    int pesoVisaoLadroes(){
        int peso = 0;
        for (int i =0; i< 24; i++){
            if(sensor.getVisaoIdentificacao()[i] == Visao.LADRAO.get()){
                peso += sensor.getVisaoIdentificacao()[i];
            }
        }
        return peso;
    }

    Objetivo decidirObjetivo(){
        Random random = new Random();
        toVendoUmBanco();
        if(toVendoUmLadrao() || random.nextInt(5) < sensor.getNumeroDeMoedas() && pesoVisaoLadroes() > 0 || pesoCheiroLadroes() > 0){
            if(temBancoPossivel() && sensor.getNumeroDeMoedas() > 0){
                return Objetivo.GUARDAR_MOEDAS;
            }
            return Objetivo.FUGIR;
        }
        if (sensor.getNumeroDeMoedas() > 0 && xBanco != -1 && yBanco != -1 && random.nextInt(5) < sensor.getNumeroDeMoedas() + 2*pesoCheiroLadroes()) {
            return Objetivo.GUARDAR_MOEDAS;
        }
        return Objetivo.EXPLORAR;
    }

    boolean movimentoIsViavel(int coordenadaVisao){
        return getResultVisao(sensor.getVisaoIdentificacao()[coordenadaVisao]) == Visao.VAZIO || getResultVisao(sensor.getVisaoIdentificacao()[coordenadaVisao]) == Visao.MOEDA  || getResultVisao(sensor.getVisaoIdentificacao()[coordenadaVisao]) == Visao.BANCO || getResultVisao(sensor.getVisaoIdentificacao()[coordenadaVisao]) == Visao.PASTINHA;
    }



}
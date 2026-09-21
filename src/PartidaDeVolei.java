/** 
 * MIT License
 *
 * Copyright(c) 2024-6 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Classe "PartidaDeVolei". 
 */
public class PartidaDeVolei {
    private  static final int MAX_SETS = 5;
    private List<Equipe> equipes;
    private List<Integer> pontuacaoEquipe1;
    private List<Integer> pontuacaoEquipe2;
    private int setsDisputados;

    /**
     * Cria uma partida de vôlei com duas equipes e placares zerados. As equipes não devem ser nulas 
     * (não está sendo verificado e causará erros.)
     * @param equipe1 Equipe 1 (mandante)
     * @param equipe2 Equipe 2 (visitante)
     */
    public PartidaDeVolei(Equipe equipe1, Equipe equipe2) {
        equipes = new ArrayList<>(2);

        equipes.add(equipe1);
        equipes.add(equipe2);

        pontuacaoEquipe1 = new ArrayList<>(MAX_SETS);
        pontuacaoEquipe2 = new ArrayList<>(MAX_SETS);
        setsDisputados = 0;
    }

    /**
     * Retorna uma string com placar formatado, contendo cabeçalho e duas linhas. Em cada linha o nome da equipe, placar de cada set
     * e quantidade de sets vendidos pela equipe.
     * @return String multilinhas
     * <pre>
     *  Formato:    
     *            1   2   3   4   5  FINAL
     *  Equipe 1  25  25  25  --  -- 3 
     *  Equipe 2  20  20  20  --  -- 0
     * </pre>
     */
    public String exibirPlacar() {
        StringBuilder placar = new StringBuilder(String.format("%16s\t1\t2\t3\t4\t5\tFINAL\n", " "));
        
        placar.append(linhaPlacar(0));
        placar.append(linhaPlacar(1));
        
        return placar.toString();
    }

    /**
     * Método auxiliar para formação das linhas do placar. Recebe uma posição (0 ou 1, mandante ou visitante),
     * para escolher qual equipe e qual lista de pontuações utilizar.
     * @param pos Posição da equipe. 0 para mandante, qualquer outro número será considerado visitante
     * @return Uma linha no formato 'Equipe 1  25  25  25  --  -- 3'
     */
    private String linhaPlacar(int pos){
        String time;
        List<Integer> pontos;
        int sets;
        String setZerado = "--";

        if(pos == 0 ){
            time = equipes.get(0).getNome();
            pontos = pontuacaoEquipe1;
            sets = setsVencidos(equipes.get(0));
        } else{
            time = equipes.get(1).getNome();
            pontos = pontuacaoEquipe2;
            sets = setsVencidos(equipes.get(1));
        }

        StringBuilder linha = new StringBuilder();
        linha.append(String.format("%16s\t", time));

        for (int j = 0; j < setsDisputados; j++) linha.append(pontos.get(j)+ "\t");
        for (int j = setsDisputados; j < MAX_SETS; j++) linha.append(setZerado + "\t");

        linha.append(sets + "\n");
        
        return  linha.toString();
    }
    /**
     * Faz a validação de placares válidos para um set: sets entre 1 e 4, pelo menos 25 pontos e 2 pontos de diferença.
     * Caso ultrapasse 25 pontos, exatamente 2 pontos de diferença. Para o set 5, mesma regra com 15 pontos.
     * @param pontos1 Pontuação da equipe 1
     * @param pontos2 Pontuação da equipe 2
     * @return TRUE/FALSE conforme o placar é válido ou não para aquele set.
     */
    private boolean validarPlacarSet(int pontos1, int pontos2){
        boolean resposta = false;

        int set = setsDisputados+1;
        int pontosMinimos = set==5 ? 15 : 25;
        int diferenca = pontos1 - pontos2;
        int pontosVencedor = diferenca > 0 ? pontos1 : pontos2;
        
        resposta = (pontosVencedor==pontosMinimos && Math.abs(diferenca)>=2) || (pontosVencedor>pontosMinimos && Math.abs(diferenca)==2);
        
        return resposta;
    }

    /**
     * Registra um placar para um set, caso seja válido. Além da validação da pontuação, registra automaticamente o próximo
     * set disponível (ou seja, registra o segundo após o primeiro etc). Também não deixa registrar sets se o jogo já terminou
     * (por exemplo, 4 sets se a mesma equipe já venceu os 3 primeiros)
     * @param pontosEquipe1 Pontuação da equipe 1
     * @param pontosEquipe2 Pontuação da equipe 2
     * @return TRUE/FALSE conforme foi possível registrar ou não o placar.
     */
    public boolean registrarPlacarSet(int pontosEquipe1, int pontosEquipe2) {
        boolean valoresValidos = false;

        if (setsDisputados < MAX_SETS && vencedorDoJogo().equals("Jogo em andamento")) {
            valoresValidos = validarPlacarSet(pontosEquipe1, pontosEquipe2);

            if(valoresValidos){
                pontuacaoEquipe1.add(pontosEquipe1);
                pontuacaoEquipe2.add(pontosEquipe2);
                setsDisputados++;
            }
        }   
            
        return valoresValidos;
    }

    /**
     * Retorna o nome da equipe vencedora do jogo, ou "Jogo em andamento" caso ninguém tenha vencido 3 sets ainda.
     * @return Nome da equipe vencedora ou "Jogo em andamento"
     */
    public String vencedorDoJogo() {
        String vencedor = "Jogo em andamento";

        Equipe mandante = equipes.get(0);
        Equipe visitante = equipes.get(1);

        if (setsVencidos(mandante) == 3) {
            vencedor = mandante.getNome();
        } else if (setsVencidos(visitante) == 3) {
                vencedor = visitante.getNome();
        }
        
        return vencedor;
    }

    /**
     * Retorna os pontos totais marcados por uma equipe até o momento. Em caso da equipe procurada não exista, 
     * retornará 0 pontos.
     * @param equipe Nome da equipe
     * @return Pontuação da equipe ou 0, se a equipe não existir.
     */
    public int pontosTotaisEquipe(String equipe) {
        int total = 0;
        List<Integer> pontuacao = null;

        pontuacao =  equipe.equals(equipes.get(0).getNome()) ? pontuacaoEquipe1 : 
                     equipe.equals(equipes.get(1).getNome()) ? pontuacaoEquipe2 : null;
      
        if (pontuacao != null){
            for (int i = 0; i < setsDisputados(); i++) {
               total += pontuacao.get(i);
             }
        }
        
        return total;
    }

    /**
     * Retorna sets vencidos por uma equipe até o momento. Em caso da equipe procurada não exista, 
     * retornará 0 sets.
     * @param nomeEquipe Nome da equipe
     * @return Sets vencidos pela equipe ou 0, se a equipe não existir.
     */
    public int setsVencidosEquipe(String nomeEquipe) {
        int vencidos = 0;

        Equipe mandante = equipes.get(0);
        Equipe visitante = equipes.get(1);
        Equipe equipe = nomeEquipe.equals(mandante.getNome()) ? mandante : 
                        nomeEquipe.equals(visitante.getNome()) ? visitante : null;
      
        if (equipe   !=  null) vencidos = setsVencidos(equipe);

        return vencidos;
    }

    /**
     * Retorna o total de sets disputados na partida até agora (entre 0 e 5)
     * @return Inteiro com o total de sets disputados até agora (0-5)
     */
    public int setsDisputados() {
        return setsDisputados;
    }

    /**
     * Verifica/retorna a quantidade de sets vencidos por um time, para verificação da 
     * condição de vitória no jogo
     * @param equipe Equipe para verificar os sets vencidos
     * @return Quantidade de sets vencidos pelo time (0-3)
     */
    private int setsVencidos(Equipe equipe) {
        int vencidos = 0;

        List<Integer> time = pontuacaoEquipe1;
        List<Integer> outro = pontuacaoEquipe2;

        if (equipe.equals(equipes.get(1))){
            time = pontuacaoEquipe2;
            outro = pontuacaoEquipe1;
        }

        for (int i = 0; i < setsDisputados; i++) {
            if(time.get(i) > outro.get(i))
                vencidos++;
        } 
        return vencidos;
    }
}

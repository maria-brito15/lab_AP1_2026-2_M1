
/** 
 * MIT License
 *
 * Copyright(c) 2024 João Caram <caram@pucminas.br>
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

import java.util.LinkedList;
import java.util.List;

/** Classe "Equipe" para sistema de Liga LPM de Vôlei */
public class Equipe {

    private final String nome;
    private List<PartidaDeVolei> partidas;
    private int quantPartidas;

    public int getQuantPartidas() {
        return quantPartidas;
    }

    /**
     * Cria uma equipe com nome recebido por parâmetro e 0 partidas disputadas.
     * 
     * @param nome Nome da equipe. Caso seja vazio, será criada com nome "Sem Nome"
     */
    public Equipe(String nome) {
        if (nome.length() == 0)
            nome = "Sem Nome";
        this.nome = nome;
        partidas = new LinkedList<>();
        quantPartidas = 0;
    }

    /**
     * Retorna o nome da equipe.
     * 
     * @return String com o nome da equipe.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Registra uma partida para a equipe. Não há obrigação da partida estar
     * terminada.
     * 
     * @param partida Partida a ser registrada.
     * @return Quantidade de partidas disputadas pela equipe.
     */
    public int registrarPartida(PartidaDeVolei partida) {
        if (partida != null && partida.exibirPlacar().contains(nome)) {
            partidas.add(partida);
            quantPartidas++;
        }

        return quantPartidas;
    }

    /**
     * A partir das partidas registradas, verifica e retorna a quantidade de
     * vitórias da equipe
     * 
     * @return Total de vitórias da equipe.
     */
    private int totalVitorias() {
        int total = 0;

        for (PartidaDeVolei p : partidas) {
            if (p.vencedorDoJogo().equals(nome)) {
                total++;
            }
        }

        return total;
    }

    /**
     * A partir das partidas registradas, verifica e retorna a quantidade de
     * derrotas da equipe
     * 
     * @return Total de derrotas da equipe.
     */
    private int totalDerrotas() {
        return quantPartidas - totalVitorias();
    }

    /**
     * Calcula o aproveitamento total da equipe (vitórias/derrotas). Em caso de 0
     * derrotas, o
     * aproveitamento é retornado como Double.MAX_VALUE.
     * 
     * @return Aproveitamento de equipe (vitórias/derrotas), podendo ser
     *         Double.MAX_VALUE em caso de 0 derrotas.
     */
    public double aproveitamentoTotal() {
        double resposta = Double.MAX_VALUE;
        int derrotas = totalDerrotas();

        if (derrotas != 0)
            resposta = (double) totalVitorias() / derrotas;

        return resposta;
    }

    /**
     * Calcula o aproveitamento em sets da equipe (vencidos/perdidos). Em caso de 0
     * sets perdidos, o
     * aproveitamento é retornado como Double.MAX_VALUE.
     * 
     * @return Aproveitamento em sets da equipe (vencidos/perdidos), podendo ser
     *         Double.MAX_VALUE em caso de 0 sets perdidos.
     */
    public double aproveitamentoSets() {
        int setsVencidos = totalSetsVencidos(nome);
        int setsPerdidos = totalSetsPerdidos(nome);

        double aprov = Double.MIN_VALUE;

        if (setsPerdidos != 0 && setsVencidos > setsPerdidos) {
            aprov = ((double) setsVencidos / setsPerdidos);
        } else if (setsPerdidos > setsVencidos) {
            aprov = 0;
        } else if (setsPerdidos == 0 && setsVencidos > 0) {
            aprov = Double.MAX_VALUE;
        }

        return aprov;
    }

    /**
     * 
     * @param nome
     * @return quantidade total de sets vencidos, soma de todas as partidas
     */
    public int totalSetsVencidos(String nome) {
        int sets = 0;

        for (PartidaDeVolei p : partidas) {
            sets += p.setsVencidosEquipe(nome);
        }

        return sets;
    }

    /**
     * @param equipe
     * @return quantidade total de sets perdidos (total de sets - total de sets
     *         ganhos), contando todas as partidas
     */
    public int totalSetsPerdidos(String equipe) {
        int setsTotais = 0;

        for (PartidaDeVolei p : partidas) {
            setsTotais += p.setsDisputados();
        }

        return (setsTotais - totalSetsVencidos(equipe));
    }

    /**
     * Cria um resumo da campanha da equipe. Uma única linha contendo seu nome,
     * total de vitórias, total de derrotas,
     * aproveitamento total e aproveitamento em sets.
     * 
     * @return String com o formato descrito acima.
     */
    public String resumo() {
        return String.format("%16s\t\t%d\t\t%d\t\t%1.3f\t\t%1.3f", nome, totalVitorias(), totalDerrotas(),
                aproveitamentoTotal(), aproveitamentoSets());
    }
}

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EquipeTest {

    @Test
    void registraPartidaCorretamente() {
        int qntdPartidasEsperadasE1 = 1;

        Equipe e1 = new Equipe("A");
        Equipe e2 = new Equipe("B");

        PartidaDeVolei partida = new PartidaDeVolei(e1, e2);
        int qntdPartidas = e1.registrarPartida(partida);

        assertEquals(qntdPartidasEsperadasE1, qntdPartidas);
    }

    @Test
    void calculaAproveitamentoTotalCorretamente() {

        Equipe e1 = new Equipe("A");
        Equipe e2 = new Equipe("B");

        PartidaDeVolei p1 = new PartidaDeVolei(e1, e2);
        int qntdPartidas = e1.registrarPartida(p1);
        p1.registrarPlacarSet(25, 0);
        p1.registrarPlacarSet(25, 0);
        p1.registrarPlacarSet(25, 0);

        assertEquals(Double.MAX_VALUE, e1.aproveitamentoTotal(), 0.01);
    }

    @Test
    void calculaAproveitamentoSetsCorretamente() {
        Equipe e1 = new Equipe("A");
        Equipe e2 = new Equipe("B");

        PartidaDeVolei p1 = new PartidaDeVolei(e1, e2);
        int qntdPartidas = e1.registrarPartida(p1);
        p1.registrarPlacarSet(25, 0);
        p1.registrarPlacarSet(25, 0);
        p1.registrarPlacarSet(25, 0);

        assertEquals(Double.MAX_VALUE, e1.aproveitamentoSets(), 0.01);
    }
}
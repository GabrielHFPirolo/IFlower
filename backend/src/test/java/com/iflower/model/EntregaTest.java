package com.iflower.model;

import com.iflower.model.enums.TipoRetirada;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Comportamento da atividade de SRP: a entrega só controla o próprio estado. */
class EntregaTest {

    private Entrega entrega() {
        return new Entrega(null, LocalDate.now().plusDays(1), LocalTime.NOON, TipoRetirada.ENTREGA, null);
    }

    @Test
    void nasceNaoSelecionadaENaoCancelada() {
        Entrega e = entrega();
        assertFalse(e.isSelecionada());
        assertFalse(e.isCancelada());
    }

    @Test
    void selecionarMarcaComoSelecionada() {
        Entrega e = entrega();
        e.selecionarEntrega();
        assertTrue(e.isSelecionada());
    }

    @Test
    void cancelarDesmarcaSelecaoEImpedeNovaSelecaoOuNovoCancelamento() {
        Entrega e = entrega();
        e.selecionarEntrega();
        e.cancelar();
        assertTrue(e.isCancelada());
        assertFalse(e.isSelecionada());
        assertThrows(IllegalStateException.class, e::selecionarEntrega);
        assertThrows(IllegalStateException.class, e::cancelar);
    }
}

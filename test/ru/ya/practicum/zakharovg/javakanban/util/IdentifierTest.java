package ru.ya.practicum.zakharovg.javakanban.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdentifierTest {
    @Test
    public void next_ReturnAddedIs1() {
        Identifier identifier = new Identifier();
        int nextId = identifier.next();
        assertEquals(1, nextId, "Сгенерированный ID должен быть 1");
    }

    @Test
    public void setMaxId_ReturnRestoredId4_IfSetMaxIsId3() {
        Identifier identifier = new Identifier();
        identifier.setMaxId(3);
        int nextId = identifier.next();
        assertEquals(4, nextId, "Сгенерированный ID должен быть 4");
    }
}
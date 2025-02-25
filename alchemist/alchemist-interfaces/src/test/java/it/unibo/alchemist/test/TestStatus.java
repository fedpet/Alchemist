/*
 * Copyright (C) 2010-2019, Danilo Pianini and contributors listed in the main project's alchemist/build.gradle file.
 *
 * This file is part of Alchemist, and is distributed under the terms of the
 * GNU General Public License, with a linking exception,
 * as described in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import it.unibo.alchemist.core.interfaces.Status;

import org.junit.jupiter.api.Test;


/**
 */
public class TestStatus {

    /**
     * 
     */
    @Test
    public void test() {
        assertNotNull(Status.PAUSED);
        assertNotNull(Status.RUNNING);
        assertNotNull(Status.TERMINATED);
    }

}

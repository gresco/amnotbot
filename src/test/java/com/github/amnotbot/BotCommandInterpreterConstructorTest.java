/*
 * Copyright (c) 2011 Geronimo Poppino <gresco@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.amnotbot;

import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.amnotbot.BotCommandInterpreter;
import com.github.amnotbot.BotCommandInterpreterBuilderFile;
import com.github.amnotbot.BotCommandInterpreterConstructor;
import com.github.amnotbot.BotConnection;

import static org.junit.Assert.*;

/**
 *
 * @author gpoppino
 */
public class BotCommandInterpreterConstructorTest
{
    private final String configFile = "commands.config";

    public BotCommandInterpreterConstructorTest()
    {
    }

    @Before
    public void setUp()
    {
        this.createCommandsFile();
    }

    @After
    public void tearDown()
    {
        this.deleteCommandsFile();
    }

    @Test
    public void testConstruct()
    {
        System.out.println("construct");
        BotConnection conn = new DummyConnection();
        BotCommandInterpreterConstructor c =
                new BotCommandInterpreterConstructor(
                    new BotCommandInterpreterBuilderFile()
                    );        
        BotCommandInterpreter result = c.construct(conn);
        assertTrue(result != null);
    }

    private void createCommandsFile()
    {
        PropertiesConfiguration p;
        p = new PropertiesConfiguration();

        p.addProperty("GoogleWebSearchCommand", "g");
        p.addProperty("QurlRequestCommand", "URL");

        try {
            p.save(this.configFile);
        } catch (ConfigurationException e) {
            fail("Could not create commands.config file");
        }
    }

    private void deleteCommandsFile()
    {
        File file = new File(this.configFile);
        file.delete();
    }
}

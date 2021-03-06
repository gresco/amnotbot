/*
 * Copyright (c) 2008 Jimmy Mitchener <jcm@packetpan.org>
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

import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.appender.FileAppender;

/**
 * @author Jimmy Mitchener
 */
public class BotLogger
{

    public static final File BOT_HOME =
            new File(SystemUtils.getUserHome(), ".amnotbot");
    private File LOG_HOME;

    static {
        BOT_HOME.mkdirs();
    }

    public BotLogger(String server)
    {
        LOG_HOME = new File(BOT_HOME, "log" + File.separator + server);
        LOG_HOME.mkdirs();
    }

    public static Logger getDebugLogger()
    {
        return LogManager.getLogger("debugLogger");
    }

    public String getLoggingPath()
    {
        return this.LOG_HOME.getAbsolutePath();
    }

    public void log(String msg)
    {
        if (msg.isEmpty()) return;

        LogManager.getRootLogger().info(msg);
    }

    public void log(String target, String msg)
    {
        if (msg.isEmpty()) return;

        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

        FileAppender appender = (FileAppender) config.getAppender(target);

        if (appender == null) {
            PatternLayout layout = PatternLayout.newBuilder().withPattern("[%d] %m%n").build();

            appender = FileAppender.newBuilder().withFileName(this.LOG_HOME.getAbsolutePath() + "/" + target).setLayout(layout)
                            .setName(target).build();
            appender.start();

            config.addAppender(appender);
            ctx.getRootLogger().addAppender(appender);
            ctx.updateLoggers();
        }
        LogManager.getLogger(target).info(msg);
    }
}


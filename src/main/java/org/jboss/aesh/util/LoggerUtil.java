/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aesh.util;

import org.jboss.aesh.console.Config;
import org.jboss.aesh.console.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.*;

/**
 * butt ugly logger util, but its simple and gets the job done (hopefully not too dangerous)
 * warning: made it even uglier when Settings was changed to not be a Singleton... gah!
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 * @author <a href="mailto:danielsoro@gmail.com">Daniel Cunha (soro)</a>
 */
public class LoggerUtil {

    private static Settings settings = null;

    public static synchronized Logger getLogger(String name) {
        if (settings == null)
            removeAllLogHandlers(LogManager.getLogManager().getLogger(""));

        return Logger.getLogger(name);
    }

    public static synchronized void updateLogSettings(Settings newSettings) {
        updateLogSettings(newSettings, "aesh.log");
    }

    private static void updateLogSettings(Settings newSettings, String logName) {
        settings = newSettings;
        if (settings.isLogging()) {
            Handler logHandler;
            final String logPath = settings.getLogFile() == null
                    ? Config.getTmpDir() + Config.getPathSeparator() + logName
                    : settings.getLogFile();

            File logFile = new File(logPath);
            // If parentFile exists & its not a dir and path of parent can be created, then proceed to create FileHandler
            if (logFile.getParentFile() != null && (logFile.getParentFile().isDirectory() || logFile.getParentFile().mkdir())) {
                // If specified log file is a directory, then create log file within that dir
                if (logFile.isDirectory())
                    logFile = new File(logFile.getAbsolutePath() + Config.getPathSeparator() + logName);

                try {
                    logHandler = new FileHandler(logFile.getAbsolutePath());
                } catch (IOException e) {
                    logHandler = new ConsoleHandler();
                }
            } else {
                logHandler = new ConsoleHandler();
            }
            logHandler.setFormatter(new SimpleFormatter());

            // Apply to base log as some log instances may not have been created. Hence, when they are created they will have this logHandler
            Logger log = LogManager.getLogManager().getLogger("");
            log.addHandler(logHandler);
            updateAllLogHandlers(logHandler);
        }
    }

    private static void updateAllLogHandlers(Handler logHandler) {
        for(Enumeration<String> loggerEnum = LogManager.getLogManager().getLoggerNames(); loggerEnum.hasMoreElements();){
            String logName = loggerEnum.nextElement();
            Logger log = getLogger(logName);
            if (log != null)
                log.addHandler(logHandler);
        }
    }

    private static void removeAllLogHandlers(Logger log) {
        for (Handler h : log.getHandlers())
            log.removeHandler(h);
    }
}
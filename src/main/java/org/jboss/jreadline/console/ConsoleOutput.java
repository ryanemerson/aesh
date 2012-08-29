/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jreadline.console;

import org.jboss.jreadline.console.redirection.Redirection;

/**
 * Value object returned by Console when newline is pressed
 * If the command is part of a pipeline sequence the stdOut and stdErr is populated accordingly
 *
 * @author <a href="mailto:stale.pedersen@jboss.org">Ståle W. Pedersen</a>
 */
public class ConsoleOutput {

    private String buffer;
    private Redirection redirection;
    private String stdOut;
    private String stdErr;

    public ConsoleOutput(String buffer, Redirection redirection) {
        this.buffer = buffer;
        this.redirection = redirection;
    }

    public ConsoleOutput(String buffer, String stdOut, String stdErr, Redirection redirection) {
        this.buffer = buffer;
        this.redirection = redirection;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    public String getBuffer() {
        return buffer;
    }

    public Redirection getRedirection() {
        return redirection;
    }

    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Buffer: ").append(getBuffer())
                .append("\nRedirection: ").append(getRedirection())
                .append("\nStdOut: ").append(getStdOut())
                .append("\nStdErr: ").append(getStdErr()).toString();
    }
}

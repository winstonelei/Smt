/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.spi;

/**
 * MythException.
 *
 */
public class MythException extends RuntimeException {
    private static final long serialVersionUID = -948934144333391208L;

    /**
     * Instantiates a new Myth exception.
     */
    public MythException() {
    }

    /**
     * Instantiates a new Myth exception.
     *
     * @param message the message
     */
    public MythException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Myth exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public MythException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Myth exception.
     *
     * @param cause the cause
     */
    public MythException(final Throwable cause) {
        super(cause);
    }
}

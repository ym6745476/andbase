/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ab.network.toolbox;

/**
 * Exception style class encapsulating Volley errors
 */
@SuppressWarnings("serial")
public class ResponseError extends Exception {
    public final NetworkResponse networkResponse;

    public ResponseError() {
        networkResponse = null;
    }

    public ResponseError(NetworkResponse response) {
        networkResponse = response;
    }

    public ResponseError(String exceptionMessage) {
       super(exceptionMessage);
       networkResponse = null;
    }

    public ResponseError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        networkResponse = null;
    }

    public ResponseError(Throwable cause) {
        super(cause);
        networkResponse = null;
    }
}

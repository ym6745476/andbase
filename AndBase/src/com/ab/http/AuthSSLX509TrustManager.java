package com.ab.http;

/***
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//package org.apache.commons.httpclient.contrib.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import android.util.Log;


/***
 * <p>
 * AuthSSLX509TrustManager can be used to extend the default
 * {@link X509TrustManager} with additional trust decisions.
 * </p>
 * 
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 *         <p>
 *         DISCLAIMER: HttpClient developers DO NOT actively support this
 *         component. The component is provided as a reference material, which
 *         may be inappropriate for use without additional customization.
 *         </p>
 */

public class AuthSSLX509TrustManager implements X509TrustManager {
    private X509TrustManager defaultTrustManager = null;

    /** 日志标记. */
    private static final String TAG = "AuthSSLX509TrustManager";
    
    /***
     * Constructor for AuthSSLX509TrustManager.
     */
    public AuthSSLX509TrustManager(final X509TrustManager defaultTrustManager) {
        super();
        if (defaultTrustManager == null) {
            throw new IllegalArgumentException("Trust manager may not be null");
        }
        this.defaultTrustManager = defaultTrustManager;
    }

    /***
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String
     *      authType)
     */
    public void checkClientTrusted(X509Certificate[] certificates, String authType)
        throws CertificateException {
        if (certificates != null) {
            for (int c = 0; c < certificates.length; c++) {
                X509Certificate cert = certificates[c];
                Log.i(TAG,"  Client certificate " + (c + 1) + ":");
                Log.i(TAG,"  Subject DN: " + cert.getSubjectDN());
                Log.i(TAG,"  Signature Algorithm: " + cert.getSigAlgName());
                Log.i(TAG,"  Valid from: " + cert.getNotBefore());
                Log.i(TAG,"  Valid until: " + cert.getNotAfter());
                Log.i(TAG,"  Issuer: " + cert.getIssuerDN());
            }
        }
        defaultTrustManager.checkClientTrusted(certificates, authType);
    }

    /***
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String
     *      authType)
     */
    public void checkServerTrusted(X509Certificate[] certificates, String authType)
        throws CertificateException {
        if (certificates != null) {
            for (int c = 0; c < certificates.length; c++) {
                X509Certificate cert = certificates[c];
                Log.i(TAG,"  Server certificate " + (c + 1) + ":");
                Log.i(TAG,"  Subject DN: " + cert.getSubjectDN());
                Log.i(TAG,"  Signature Algorithm: " + cert.getSigAlgName());
                Log.i(TAG,"  Valid from: " + cert.getNotBefore());
                Log.i(TAG,"  Valid until: " + cert.getNotAfter());
                Log.i(TAG,"  Issuer: " + cert.getIssuerDN());
            }
        }
        defaultTrustManager.checkServerTrusted(certificates, authType);
    }

    /***
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        return this.defaultTrustManager.getAcceptedIssuers();
    }
}


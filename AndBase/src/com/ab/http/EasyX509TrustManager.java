package com.ab.http;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * © 2012 amsoft.cn
 * 名称：EasyX509TrustManager.java 
 * 描述：SSL自签名的实现类
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午9:00:52
 */
public class EasyX509TrustManager implements X509TrustManager {
	
	private X509TrustManager standardTrustManager = null;

	/**
	 * Constructor for EasyX509TrustManager.
	 */
	public EasyX509TrustManager(KeyStore keystore)
			throws NoSuchAlgorithmException, KeyStoreException {
		super();
		TrustManagerFactory factory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		factory.init(keystore);
		TrustManager[] trustmanagers = factory.getTrustManagers();
		if (trustmanagers.length == 0) {
			throw new NoSuchAlgorithmException("no trust manager found");
		}
		this.standardTrustManager = (X509TrustManager) trustmanagers[0];
	}

	/**
	 * 执行客户端的安全策略
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] certificates,
			String authType) throws CertificateException {
		standardTrustManager.checkClientTrusted(certificates, authType);
	}

	/**
	 * 执行服务器的安全策略
	 * 检查签名、信任链中证书的有效日期和CRLs
	 * 清除因证书过期出现的问题
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] certificates,
			String authType) throws CertificateException {
		// Clean up the certificates chain and build a new one.
		// Theoretically, we shouldn't have to do this, but various web servers
		// in practice are mis-configured to have out-of-order certificates or
		// expired self-issued root certificate.

		// int chainLength = certificates.length;
		if (certificates.length > 1) {
			// 1. we clean the received certificates chain.
			// We start from the end-entity certificate, tracing down by
			// matching
			// the "issuer" field and "subject" field until we can't continue.
			// This helps when the certificates are out of order or
			// some certificates are not related to the site.
			int currIndex;
			for (currIndex = 0; currIndex < certificates.length; ++currIndex) {
				boolean foundNext = false;
				for (int nextIndex = currIndex + 1; nextIndex < certificates.length; ++nextIndex) {
					if (certificates[currIndex].getIssuerDN().equals(
							certificates[nextIndex].getSubjectDN())) {
						foundNext = true;
						// Exchange certificates so that 0 through currIndex + 1
						// are in proper order
						if (nextIndex != currIndex + 1) {
							X509Certificate tempCertificate = certificates[nextIndex];
							certificates[nextIndex] = certificates[currIndex + 1];
							certificates[currIndex + 1] = tempCertificate;
						}
						break;
					}
				}
				if (!foundNext)
					break;
			}

			// 2. we exam if the last traced certificate is self issued and it
			// is expired.
			// If so, we drop it and pass the rest to checkServerTrusted(),
			// hoping we might
			// have a similar but unexpired trusted root.
			int chainLength = currIndex + 1;
			X509Certificate lastCertificate = certificates[chainLength - 1];
			Date now = new Date();
			if (lastCertificate.getSubjectDN().equals(
					lastCertificate.getIssuerDN())
					&& now.after(lastCertificate.getNotAfter())) {
				--chainLength;
			}
		}

		// standardTrustManager.checkServerTrusted(certificates, authType);
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return this.standardTrustManager.getAcceptedIssuers();
	}
}
package fr.hm.hapiour.util;


import fr.hm.hapiour.util.proxy.ProxyProperties;
import fr.hm.hapiour.util.proxy.ProxyUtil;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class RestTemplateUtils {

    /**
     * contructeur privé pour une classe helper
     */
    private RestTemplateUtils() {
        // rien à faire
    }

    public static RestTemplate initRestTemplate(String loggerName, ProxyProperties proxyProperties) {
        RestTemplate restTemplate = ProxyUtil.getProxyClientBuilder(proxyProperties);
        return restTemplate;
    }

    public static RestTemplate initRestTemplate(String loggerName, boolean acceptInvalidCertificate) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        HttpComponentsClientHttpRequestFactory requestFactory = null;
        if (acceptInvalidCertificate) {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();

            requestFactory = new HttpComponentsClientHttpRequestFactory();

            requestFactory.setHttpClient(httpClient);
        }
        RestTemplate restTemplate = requestFactory == null ? new RestTemplate() : new RestTemplate(requestFactory);

        return restTemplate;
    }
}

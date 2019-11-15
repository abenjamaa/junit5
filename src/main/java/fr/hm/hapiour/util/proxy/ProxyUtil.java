package fr.hm.hapiour.util.proxy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * Classe utilitaire pour permettre de faire des requètes via le proxy
 */
public class ProxyUtil {

    private ProxyUtil() {
        //No instance
    }

    /**
     * Création d'un RestTemplate passant par le proxy en paramètre
     * @param proxyProperties Les propriétés du proxy
     * @return RestTemplate initialisé
     */
    public static RestTemplate getProxyClientBuilder(ProxyProperties proxyProperties) {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(
                new AuthScope(proxyProperties.getProxyHost(), proxyProperties.getProxyPort()),
                new UsernamePasswordCredentials(proxyProperties.getProxyAuth(), proxyProperties.getProxyPassword()));

        HttpHost myProxy = new HttpHost(proxyProperties.getProxyHost(), proxyProperties.getProxyPort());
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();

        HttpClient httpClient = clientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        return new RestTemplate(factory);
    }

    /**
     * Création de l'objet Proxy à partir des propriétés du proxy
     * @param proxyProperties Les propriétés du proxy
     * @return L'objet Proxy créé
     */
    public static Proxy getProxy(ProxyProperties proxyProperties) {

        SocketAddress socketAddress = new InetSocketAddress(proxyProperties.getProxyHost(), proxyProperties.getProxyPort());
        return new Proxy(Proxy.Type.HTTP, socketAddress);
    }

}

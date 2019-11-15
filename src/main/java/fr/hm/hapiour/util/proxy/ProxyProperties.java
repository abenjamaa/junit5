package fr.hm.hapiour.util.proxy;

/**
 * Propriétés nécessaire pour se connecter à un proxy authentifié
 */
public class ProxyProperties {

    private String proxyHost;
    private int proxyPort;
    private String proxyAuth;
    private String proxyPassword;

    /**
     * proxyHost getter
     * @return proxyHost
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * proxyHost setter
     * @param proxyHost the new proxyHost value
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * proxyPort getter
     * @return proxyPort
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * proxyPort setter
     * @param proxyPort the new proxyPort value
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * proxyAuth getter
     * @return proxyAuth
     */
    public String getProxyAuth() {
        return proxyAuth;
    }

    /**
     * proxyAuth setter
     * @param proxyAuth the new proxyAuth value
     */
    public void setProxyAuth(String proxyAuth) {
        this.proxyAuth = proxyAuth;
    }

    /**
     * proxyPassword getter
     * @return proxyPassword
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * proxyPassword setter
     * @param proxyPassword the new proxyPassword value
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }
}

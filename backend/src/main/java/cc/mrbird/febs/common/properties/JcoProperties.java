package cc.mrbird.febs.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jco")
public class JcoProperties {
    private static String ashost;

    public static String getAshost() {
        return ashost;
    }

    public void setAshost(String ashost) {
        JcoProperties.ashost = ashost;
    }

    private static String sysnr;

    public static String getSysnr() {
        return sysnr;
    }

    public void setSysnr(String sysnr) {
        JcoProperties.sysnr = sysnr;
    }

    private static String client;

    public static String getClient() {
        return client;
    }

    public void setClient(String client) {
        JcoProperties.client = client;
    }


    private static String user;

    public static String getUser() {
        return user;
    }

    public void setUser(String user) {
        JcoProperties.user = user;
    }

    private static String passw;

    public static String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        JcoProperties.passw = passw;
    }

    private static String lang;

    public static String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        JcoProperties.lang = lang;
    }

}

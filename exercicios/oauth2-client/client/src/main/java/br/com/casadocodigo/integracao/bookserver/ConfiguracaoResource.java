package br.com.casadocodigo.integracao.bookserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class ConfiguracaoResource {

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private ClientTokenServices clientTokenServices;

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate() {

        OAuth2ProtectedResourceDetails resourceDetails = bookserver();
        OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, oAuth2ClientContext);
        AccessTokenProviderChain provider = new AccessTokenProviderChain(Arrays.asList(new AuthorizationCodeAccessTokenProvider()));
        provider.setClientTokenServices(clientTokenServices);
        template.setAccessTokenProvider(provider);
        return template;
    }

    @Bean
    public OAuth2ProtectedResourceDetails bookserver() {
        AuthorizationCodeResourceDetails detailsForBookServer = new AuthorizationCodeResourceDetails();

        detailsForBookServer.setId("bookserver");
        detailsForBookServer.setTokenName("oauth_token");
        detailsForBookServer.setClientId("cliente-app");
        detailsForBookServer.setClientSecret("123456");
        detailsForBookServer.setAccessTokenUri("http://localhost:8080/oauth/token");
        detailsForBookServer.setUserAuthorizationUri("http://localhost:8080/oauth/authorize");
        detailsForBookServer.setScope(Arrays.asList("read", "write"));

        detailsForBookServer.setPreEstablishedRedirectUri(("http://localhost:9000/integracao/callback"));
        detailsForBookServer.setUseCurrentUri(false);

        detailsForBookServer.setClientAuthenticationScheme(AuthenticationScheme.header);
        return detailsForBookServer;
    }

}

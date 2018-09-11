package br.com.casadocodigo.integracao.bookserver;

import br.com.casadocodigo.configuracao.seguranca.UsuarioLogado;
import br.com.casadocodigo.usuarios.AcessoBookserver;
import br.com.casadocodigo.usuarios.Usuario;
import br.com.casadocodigo.usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class BookserverClientTokenServices implements ClientTokenServices {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, Authentication authentication) {

        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        Usuario usuario = usuariosRepository.findById(usuarioLogado.getId());

        String acessoToken = usuario.getAcessoBookserver().getAcessoToken();

        Calendar dataDeExpiracao = usuario.getAcessoBookserver().getDataDeExpiracao();

        if (acessoToken == null) return null;

        DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(acessoToken);
        oAuth2AccessToken.setExpiration(dataDeExpiracao != null ? dataDeExpiracao.getTime() : null);

        return oAuth2AccessToken;
    }

    @Override
    public void saveAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, Authentication authentication, OAuth2AccessToken oAuth2AccessToken) {
        AcessoBookserver acessoBookserver = new AcessoBookserver();
        acessoBookserver.setAcessoToken(oAuth2AccessToken.getValue());

        Calendar expirationDate = Calendar.getInstance();
        if(oAuth2AccessToken.getExpiration() != null)
            expirationDate.setTime(oAuth2AccessToken.getExpiration());
        acessoBookserver.setDataDeExpiracao(expirationDate);

        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        Usuario usuario = usuariosRepository.findById(usuarioLogado.getId());
        usuario.setAcessoBookserver(acessoBookserver);
        usuariosRepository.save(usuario);
    }

    @Override
    public void removeAccessToken(OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails, Authentication authentication) {
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        Usuario usuario = usuariosRepository.findById(usuarioLogado.getId());

        usuario.setAcessoBookserver(null);
        usuariosRepository.save(usuario);
    }
}

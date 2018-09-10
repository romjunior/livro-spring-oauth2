package br.com.casadocodigo.minhaconta;

import br.com.casadocodigo.configuracao.seguranca.UsuarioLogado;
import br.com.casadocodigo.integracao.bookserver.AuthorizationCodeTokenService;
import br.com.casadocodigo.integracao.bookserver.ImplicitTokenService;
import br.com.casadocodigo.integracao.bookserver.OAuth2Token;
import br.com.casadocodigo.integracao.bookserver.PasswordTokenService;
import br.com.casadocodigo.usuarios.AcessoBookserver;
import br.com.casadocodigo.usuarios.Usuario;
import br.com.casadocodigo.usuarios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/integracao")
public class IntegracaoController {

    @Autowired
    private UsuariosRepository usuarios;

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private AuthorizationCodeTokenService authorizationCodeTokenService;

    @Autowired
    private ImplicitTokenService implicitTokenService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView integracao() {
        String endpointDeAutorizacao = implicitTokenService.getAuthorizationEndpoint();
        return new ModelAndView("redirect:" + endpointDeAutorizacao);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public ModelAndView callback(String code, String state) {

        OAuth2Token token = authorizationCodeTokenService.getToken(code);

        AcessoBookserver acessoBookserver = new AcessoBookserver();
        acessoBookserver.setAcessoToken(token.getAccessToken());

        Usuario usuario = usuarioLogado();
        usuario.setAcessoBookserver(acessoBookserver);

        usuarios.save(usuario);

        return new ModelAndView("redirect:/minhaconta/principal");
    }

    @RequestMapping(value = "implicit", method = RequestMethod.GET)
    public ModelAndView implicit() {
        return new ModelAndView("minhaconta/bookserver");
    }

    private Usuario usuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
        return usuarios.findById(usuarioLogado.getId());
    }

}

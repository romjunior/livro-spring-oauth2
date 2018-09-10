package br.com.casadocodigo.integracao.bookserver;

import br.com.casadocodigo.configuracao.seguranca.BasicAuthentication;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

@Service
public class PasswordTokenService {

    public OAuth2Token getToken(String login, String senha) {

        RestTemplate restTemplate = new RestTemplate();

        BasicAuthentication clientAuth = new BasicAuthentication("cliente-app", "123456");

        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(
                getBody(login, senha),
                getHeader(clientAuth),
                HttpMethod.POST,
                URI.create("http://localhost:8080/oauth/token")
        );

        try {
            ResponseEntity<OAuth2Token> responseEntity = restTemplate.exchange(requestEntity, OAuth2Token.class);

            if(responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("sem sucesso");
            }
        }catch (Exception ex) {
            throw new RuntimeException("Erro ao retirar o token de acesso");
        }

    }

    private MultiValueMap<String, String> getBody(String login, String senha) {
        MultiValueMap<String, String> dadosFormulario = new LinkedMultiValueMap<>();

        dadosFormulario.add("grant_type", "password");
        dadosFormulario.add("username", login);
        dadosFormulario.add("password", senha);
        dadosFormulario.add("scope", "read write");

        return dadosFormulario;
    }

    private HttpHeaders getHeader(BasicAuthentication client) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.add("Authorization", "Basic " + client.getCredenciaisBase64());
        return httpHeaders;
    }

}

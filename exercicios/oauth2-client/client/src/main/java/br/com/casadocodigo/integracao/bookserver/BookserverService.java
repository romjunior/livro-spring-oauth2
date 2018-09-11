package br.com.casadocodigo.integracao.bookserver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import br.com.casadocodigo.integracao.model.Livro;

@Service
public class BookserverService {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    public List<Livro> livros(String token) throws UsuarioSemAutorizacaoException {

        String endpoint = "http://localhost:8080/api/v2/livros";

        try {
            Livro[] livros = oAuth2RestTemplate.getForObject(endpoint, Livro[].class);
            return listaFromArray(livros);
        } catch (HttpClientErrorException e) {
            throw new UsuarioSemAutorizacaoException("Não foi obter os livros do usuário");
        }
    }

    private List<Livro> listaFromArray(Livro[] livros) {
        List<Livro> lista = new ArrayList<>();

        for (Livro livro : livros) {
            lista.add(livro);
        }

        return lista;
    }

}

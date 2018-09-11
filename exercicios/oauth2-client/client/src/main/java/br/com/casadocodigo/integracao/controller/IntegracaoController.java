package br.com.casadocodigo.integracao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/integracao")
public class IntegracaoController {

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public ModelAndView callback() {
        return new ModelAndView("forward:/minhaconta/principal");
    }
}

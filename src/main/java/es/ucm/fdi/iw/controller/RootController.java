package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);

    @GetMapping("/restaurante")
    public String restaurante(Model model) {
        return "restaurante";
    }

    @GetMapping("/platos")
    public String platos(Model model) {
        return "platos";
    }

	@GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/restaurante")
    public String restaurante(Model model) {
        return "login";
    }

    @GetMapping("/listaPedidos")
    public String listaPedidos(Model model) {
        return "login";
    }

	@GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}

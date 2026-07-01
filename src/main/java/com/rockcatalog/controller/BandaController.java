package com.rockcatalog.controller;

import com.rockcatalog.model.Banda;
import com.rockcatalog.repository.BandaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller = a camada que recebe as requisições HTTP (GET/POST) e decide
 * o que fazer: buscar dados no repository, colocar no Model, e escolher
 * qual template do Thymeleaf vai renderizar a resposta.
 *
 * Esse aqui cuida de tudo relacionado a Banda: listar, cadastrar, ver
 * detalhe e excluir.
 */
@Controller
public class BandaController {

    // Injeção de dependência via construtor: o Spring cria o
    // BandaRepository pra mim e "encaixa" aqui automaticamente. Não preciso
    // fazer "new BandaRepository()" em lugar nenhum.
    private final BandaRepository bandaRepository;

    @Autowired
    public BandaController(BandaRepository bandaRepository) {
        this.bandaRepository = bandaRepository;
    }

    // Pagina inicial: lista todas as bandas do catalogo.
    // model.addAttribute deixa a lista disponível pro template index.html
    // usar em th:each="banda : ${bandas}".
    @GetMapping("/")
    public String listar(Model model) {
        List<Banda> bandas = bandaRepository.findAll();
        model.addAttribute("bandas", bandas);
        return "index"; // procura templates/index.html
    }

    // Formulario para cadastrar uma nova banda. Passo um objeto Banda vazio
    // pro template porque o formulário usa th:object pra saber em quais
    // campos escrever (th:field="*{nome}", etc).
    @GetMapping("/bandas/novo")
    public String formNovaBanda(Model model) {
        model.addAttribute("banda", new Banda());
        return "nova-banda";
    }

    // Recebe o POST do formulário acima. @Valid ativa as validações que
    // coloquei na classe Banda (@NotBlank); se algo estiver errado,
    // BindingResult guarda os erros e eu volto pro mesmo formulário
    // mostrando a mensagem, em vez de salvar dado inválido.
    @PostMapping("/bandas")
    public String salvarBanda(@Valid @ModelAttribute("banda") Banda banda,
                               BindingResult resultado,
                               RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "nova-banda";
        }
        bandaRepository.save(banda);
        // addFlashAttribute sobrevive a um redirect (diferente do
        // addAttribute normal), então dá pra mostrar essa mensagem na
        // próxima página mesmo depois do redirect abaixo.
        redirectAttributes.addFlashAttribute("mensagem", "Banda \"" + banda.getNome() + "\" adicionada ao catalogo.");
        return "redirect:/";
    }

    // Pagina de detalhe: mostra a banda e a lista de albuns dela (o próprio
    // objeto banda já vem com os albuns carregados por causa do @OneToMany).
    @GetMapping("/bandas/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        Banda banda = bandaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banda nao encontrada: " + id));
        model.addAttribute("banda", banda);
        return "banda";
    }

    // Exclui a banda e, por causa do cascade = ALL configurado na entidade,
    // todos os albuns (e avaliações desses albuns) somem junto.
    @PostMapping("/bandas/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bandaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem", "Banda removida do catalogo.");
        return "redirect:/";
    }
}

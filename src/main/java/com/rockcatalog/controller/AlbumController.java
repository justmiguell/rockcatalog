package com.rockcatalog.controller;

import com.rockcatalog.model.Album;
import com.rockcatalog.model.Avaliacao;
import com.rockcatalog.model.Banda;
import com.rockcatalog.repository.AlbumRepository;
import com.rockcatalog.repository.AvaliacaoRepository;
import com.rockcatalog.repository.BandaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller de Album e Avaliacao. As rotas de criação de álbum são
 * "aninhadas" dentro da banda (/bandas/{bandaId}/albuns) porque um álbum
 * só faz sentido existindo dentro de uma banda — não tem como criar um
 * álbum "solto".
 */
@Controller
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final BandaRepository bandaRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    @Autowired
    public AlbumController(AlbumRepository albumRepository,
                            BandaRepository bandaRepository,
                            AvaliacaoRepository avaliacaoRepository) {
        this.albumRepository = albumRepository;
        this.bandaRepository = bandaRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    // Formulario para cadastrar um novo album dentro de uma banda.
    // Preciso buscar a Banda primeiro pra saber a qual banda esse album
    // novo vai pertencer, e já deixo ela pré-setada no objeto Album.
    @GetMapping("/bandas/{bandaId}/albuns/novo")
    public String formNovoAlbum(@PathVariable Long bandaId, Model model) {
        Banda banda = bandaRepository.findById(bandaId)
                .orElseThrow(() -> new IllegalArgumentException("Banda nao encontrada: " + bandaId));
        Album album = new Album();
        album.setBanda(banda);
        model.addAttribute("banda", banda);
        model.addAttribute("album", album);
        return "novo-album";
    }

    @PostMapping("/bandas/{bandaId}/albuns")
    public String salvarAlbum(@PathVariable Long bandaId,
                               @Valid @ModelAttribute("album") Album album,
                               BindingResult resultado,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Banda banda = bandaRepository.findById(bandaId)
                .orElseThrow(() -> new IllegalArgumentException("Banda nao encontrada: " + bandaId));

        if (resultado.hasErrors()) {
            model.addAttribute("banda", banda);
            return "novo-album";
        }

        album.setBanda(banda);
        albumRepository.save(album);
        redirectAttributes.addFlashAttribute("mensagem", "Album \"" + album.getTitulo() + "\" adicionado.");
        return "redirect:/bandas/" + bandaId;
    }

    // Pagina de detalhe do album: mostra info, nota media (calculada no
    // getNotaMedia() da entidade) e todas as avaliacoes publicas.
    // Também já mando um objeto Avaliacao vazio, que é o que o formulário
    // de "deixar avaliação" dessa mesma página vai preencher.
    @GetMapping("/albuns/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album nao encontrado: " + id));
        model.addAttribute("album", album);
        model.addAttribute("novaAvaliacao", new Avaliacao());
        return "album";
    }

    // Aqui é o coração da parte "social" do projeto: não tem verificação de
    // login nenhuma, qualquer pessoa que estiver na página pode mandar uma
    // avaliação. Assim que salva, redireciono de volta pra mesma página do
    // álbum, que agora vai mostrar a nova avaliação e a média atualizada.
    @PostMapping("/albuns/{id}/avaliacoes")
    public String avaliar(@PathVariable Long id,
                           @Valid @ModelAttribute("novaAvaliacao") Avaliacao novaAvaliacao,
                           BindingResult resultado,
                           Model model) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album nao encontrado: " + id));

        if (resultado.hasErrors()) {
            model.addAttribute("album", album);
            return "album";
        }

        novaAvaliacao.setAlbum(album);
        avaliacaoRepository.save(novaAvaliacao);
        return "redirect:/albuns/" + id;
    }

    @PostMapping("/albuns/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album nao encontrado: " + id));
        Long bandaId = album.getBanda().getId();
        albumRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem", "Album removido.");
        return "redirect:/bandas/" + bandaId;
    }
}

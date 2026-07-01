package com.rockcatalog.config;

import com.rockcatalog.model.Album;
import com.rockcatalog.model.Avaliacao;
import com.rockcatalog.model.Banda;
import com.rockcatalog.repository.AlbumRepository;
import com.rockcatalog.repository.AvaliacaoRepository;
import com.rockcatalog.repository.BandaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// CommandLineRunner é uma interface do Spring: qualquer classe que a
// implementa tem seu método run() executado automaticamente assim que a
// aplicação termina de subir. Uso isso pra popular o banco com alguns dados
// de exemplo, só pra o catálogo não começar vazio na primeira vez que
// alguém roda o projeto. Dá pra editar ou apagar essa classe à vontade.
@Component
public class DataLoader implements CommandLineRunner {

    private final BandaRepository bandaRepository;
    private final AlbumRepository albumRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    public DataLoader(BandaRepository bandaRepository,
                       AlbumRepository albumRepository,
                       AvaliacaoRepository avaliacaoRepository) {
        this.bandaRepository = bandaRepository;
        this.albumRepository = albumRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @Override
    public void run(String... args) {
        // Sem esse "if" aqui, toda vez que eu reiniciasse o servidor essas
        // bandas de exemplo seriam inseridas de novo e duplicadas. Como já
        // uso banco em arquivo (os dados persistem), só populo se o banco
        // ainda estiver realmente vazio.
        if (bandaRepository.count() > 0) {
            return;
        }

        Banda mcr = bandaRepository.save(new Banda("My Chemical Romance", "Emo / Rock Alternativo", null));
        Album black = albumRepository.save(new Album("The Black Parade", 2006, null, mcr));
        avaliacaoRepository.save(new Avaliacao("visitante_do_site", 10.0,
                "Album perfeito do inicio ao fim, marcou uma geracao inteira.", black));

        Banda paramore = bandaRepository.save(new Banda("Paramore", "Pop Punk / Emo", null));
        albumRepository.save(new Album("Riot!", 2007, null, paramore));

        Banda taking = bandaRepository.save(new Banda("Taking Back Sunday", "Emo / Post-Hardcore", null));
        albumRepository.save(new Album("Tell All Your Friends", 2002, null, taking));
    }
}

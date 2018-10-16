package pl.jkiakumbo.cinema.service.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jkiakumbo.cinema.domain.Cinema;
import pl.jkiakumbo.cinema.repository.CinemaRepository;

@Service
public class CinemaService extends CRUDService<Cinema> {

    private CinemaRepository cinemaRepository;

    @Autowired
    public CinemaService(CinemaRepository cinemaRepository) {
        super(cinemaRepository);
        this.cinemaRepository = cinemaRepository;
    }

    public Cinema findByName(String name) {
        return cinemaRepository.findByName(name);
    }

    public boolean canSave(Cinema cinema) {
        return cinemaRepository.findByName(cinema.getName()) == null;
    }
}

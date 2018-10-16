package pl.jkiakumbo.cinema.controller.api;


import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jkiakumbo.cinema.domain.Ticket;
import pl.jkiakumbo.cinema.repository.TicketRepository;
import pl.jkiakumbo.cinema.utils.PDFGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;

@RestController
@RequestMapping("/api/ticket")
public class TicketsController {

    private TicketRepository ticketRepository;

    @Autowired
    public TicketsController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/{ticketId}")
    public void getTicket(@PathVariable Long ticketId,
                          Principal principal,
                          HttpServletResponse response) throws IOException, URISyntaxException, DocumentException {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Cannot find ticket with id - " + ticketId));

        response.setContentType("application/pdf");

        PDFGenerator.generatePdf(response.getOutputStream(), ticket);

        response.flushBuffer();
    }}

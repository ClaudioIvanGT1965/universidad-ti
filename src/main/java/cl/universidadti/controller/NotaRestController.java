package cl.universidadti.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.universidadti.dto.NotaLoteDTO;
import cl.universidadti.model.Nota;
import cl.universidadti.repository.NotaRepository;
import cl.universidadti.service.NotaService;

@RestController
@RequestMapping("/api/notas")
public class NotaRestController {

    private final NotaRepository notaRepository;
    private final NotaService notaService;

    public NotaRestController(
            NotaRepository notaRepository,
            NotaService notaService) {

        this.notaRepository = notaRepository;
        this.notaService = notaService;
    }

    @GetMapping
    public List<Nota> listar() {

        return notaRepository.findAll();
    }

    @GetMapping("/inscripcion/{id}")
    public List<Nota> notasPorInscripcion(
            @PathVariable Long id) {

        return notaRepository
                .findByEstudianteSeccionIdOrderByNumeroNotaAsc(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nota registrar(
            @RequestBody Nota nota) {

        return notaService.registrarNota(nota);
    }

    @PostMapping("/seccion/{idSeccion}")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrarNotasSeccion(
            @PathVariable Long idSeccion,
            @RequestBody NotaLoteDTO lote) {

        notaService.registrarNotasSeccion(
                idSeccion,
                lote);
    }
}

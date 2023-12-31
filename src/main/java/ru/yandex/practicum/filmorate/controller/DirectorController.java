package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping()
    public List<Director> findAll() {
        return directorService.getAllDirectors();
    }

    @PostMapping()
    public Director create(@RequestBody Director director) {
        return directorService.addDirector(director);
    }

    @PutMapping()
    public Director update(@RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id) {
        directorService.deleteDirector(id);
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable("id") int id) {
        return directorService.getDirectorById(id);
    }

}


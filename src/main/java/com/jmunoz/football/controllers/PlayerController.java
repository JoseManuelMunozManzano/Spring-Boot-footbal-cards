package com.jmunoz.football.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @GetMapping
    public List<String> listPlayers() {
        return List.of("Kylian MBAPPÉ", "Vinicius JÚNIOR");
    }

    @GetMapping("/{name}")
    public String readPlayer(@PathVariable String name) {
        return name;
    }

    @PostMapping
    public String createPlayer(@RequestBody String name) {
        return "Player " + name + " created";
    }

    @PutMapping("/{name}")
    public String updatePlayer(@PathVariable String name, @RequestBody String newName) {
        return "Player " + name + " updated to " + newName;
    }

    @DeleteMapping("/{name}")
    public String deletePlayer(@PathVariable String name) {
        return "Player " + name + " deleted";
    }
}

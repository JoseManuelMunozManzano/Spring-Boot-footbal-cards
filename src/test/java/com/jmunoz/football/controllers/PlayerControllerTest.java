package com.jmunoz.football.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jmunoz.football.exceptions.AlreadyExistsException;
import com.jmunoz.football.exceptions.NotFoundException;
import com.jmunoz.football.model.Player;
import com.jmunoz.football.services.FootballService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FootballService footballService;

    @Test
    void testListPlayers() throws Exception {
        // ARRANGE
        Player player1 = new Player("1884823", 5, "Ivana ANDRES", "Defender", LocalDate.of(1994, 7, 13));
        Player player2 = new Player("325636", 11, "Alexia PUTELLAS", "Midfielder", LocalDate.of(1994, 2, 4));
        List<Player> players = List.of(player1, player2);
        given(footballService.listPlayers()).willReturn(players);

        // ACT & ASSERT
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/players").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<Player> returnedPlayers = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Player.class));
        // Nos permite comparar los elementos de dos arrays.
        assertArrayEquals(players.toArray(), returnedPlayers.toArray());
    }

    @Test
    void testReadPlayer_exists() throws Exception {
        // ARRANGE
        Player player = new Player("1884823", 5, "Ivana ANDRES", "Defender", LocalDate.of(1994, 7, 13));
        given(footballService.getPlayer("1884823")).willReturn(player);
        String id = "1884823";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // ACT & ASSERT
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/players/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Player returnedPlayer = mapper.readValue(result.getResponse().getContentAsString(), Player.class);
        assertEquals(player, returnedPlayer);
    }

    @Test
    void testReadPlayer_doesnt_exist() throws Exception {
        // ARRANGE
        String id = "1884823";
        given(footballService.getPlayer(id))
                .willThrow(new NotFoundException(("Player not found")));

        // ACT & ASSERT
        mvc.perform(MockMvcRequestBuilders.get("/players/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePlayer() throws Exception {
        // ARRANGE
        String id = "1884823";

        // ACT & ASSERT
        mvc.perform(MockMvcRequestBuilders.delete("/players/" + id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePlayer_exists() throws Exception {
        // ARRANGE
        Player player = new Player("1884823", 5, "Ivana ANDRES", "Defender", LocalDate.of(1994, 7, 13));
        given(footballService.updatePlayer(player)).willReturn(player);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // ACT & ASSERT
        mvc.perform(MockMvcRequestBuilders.put("/players/" + player.id())
                .content(mapper.writeValueAsString(player))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePlayer_doesnt_exist() throws Exception {
        // ARRANGE
        Player player = new Player("1884823", 5, "Ivana ANDRES", "Defender", LocalDate.of(1994, 7, 13));
        given(footballService.updatePlayer(player)).willThrow(new NotFoundException("Player not found"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // ACT & ASSERT
        mvc.perform(MockMvcRequestBuilders.put("/players/" + player.id())
                .content(mapper.writeValueAsString(player))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePlayer_doesnt_exist() throws Exception {
        // ARRANGE
        Player player = new Player("1884823", 5, "Ivana ANDRES", "Defender", LocalDate.of(1994, 7, 13));
        given(footballService.addPlayer(player)).willReturn(player);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // ACT & ASSERT
        mvc.perform(MockMvcRequestBuilders.post("/players")
                .content(mapper.writeValueAsString(player))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreatePlayer_already_exists() throws Exception {
        // ARRANGE
        Player player = new Player("1884823", 5, "Ivana ANDRES", "Defender", LocalDate.of(1994, 7, 13));
        given(footballService.addPlayer(player)).willThrow(new AlreadyExistsException("The player already exists"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // ACT & ASSERT
        mvc.perform(MockMvcRequestBuilders.post("/players")
                .content(mapper.writeValueAsString(player))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

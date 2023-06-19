package com.example.It_info_pass_master.Controller;

import com.example.It_info_pass_master.Entity.GameQuestionRecord;
import com.example.It_info_pass_master.Entity.GameSelectRecord;
import com.example.It_info_pass_master.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class GameRestController {
    @Autowired
    private GameService gameService;

    @GetMapping("/game_start")
    public GameQuestionRecord question(@RequestParam(name = "userGameId")int userGameId, @RequestParam(name = "i")int i) {
        System.out.println("test2");
        System.out.println("4");
        var gameQuestion = gameService.gameAgeSelect(userGameId,i);
        System.out.println("test3");
        return gameQuestion;
//        try {
//            System.out.println("4");
//            var gameQuestion = gameService.gameAgeSelect(userGameId,i);
//            System.out.println("test3");
//            return gameQuestion;
//        } catch(Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
    }
    @GetMapping("/game_start/select")
    public List<GameSelectRecord> select(@RequestParam(name = "userGameId")int userGameId, @RequestParam(name = "i")int i) {
        var gameSelect = gameService.gameChoiceSelect(userGameId, i);
        System.out.println(gameSelect);
        return gameSelect;
//        try {
//            var gameSelect = gameService.gameChoiceSelect(userGameId, i);
//            System.out.println(gameSelect);
//            return gameSelect;
//        } catch(Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
    }
}

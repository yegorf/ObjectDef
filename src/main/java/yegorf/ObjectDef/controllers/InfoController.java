package yegorf.ObjectDef.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yegorf.ObjectDef.entities.Animal;
import yegorf.ObjectDef.entities.Sign;
import yegorf.ObjectDef.repos.AnimalRepo;
import yegorf.ObjectDef.repos.MatchesRepo;
import yegorf.ObjectDef.repos.SignRepo;
import yegorf.ObjectDef.tools.Analyzer;
import yegorf.ObjectDef.tools.DbHandler;

import java.util.HashSet;

@RestController
@RequestMapping("/info")
public class InfoController {
    private final SignRepo signRepo;
    private final AnimalRepo animalRepo;
    private final MatchesRepo matchesRepo;
    private DbHandler handler;

    @Autowired
    public InfoController(SignRepo signRepo, AnimalRepo animalRepo, MatchesRepo matchesRepo) {
        this.signRepo = signRepo;
        this.animalRepo = animalRepo;
        this.matchesRepo = matchesRepo;
    }

    @GetMapping("/animals")
    public HashSet<Animal> animals() {
        return animalRepo.findAll();
    }

    @PostMapping("/addAnimal")
    public String addAnimal(
            @RequestParam String animal,
            @RequestParam Integer id1,
            @RequestParam Integer id2
    ) {
        System.out.println(animal + " " + id1 + " " + id2);
        Analyzer analyzer = new Analyzer(signRepo, animalRepo, matchesRepo);

        String result = "Добавлено успешно";
        boolean success = true;
        if (analyzer.checkAnimalName(animal)) {
            result = "Животное с таким именем уже есть!";
            success = false;
        } else if (analyzer.checkUnique(id1, id2)) {
            result = "Животное с такими признаками уже есть!";
            success = false;
        }

        if (success) {
            handler = new DbHandler(signRepo, animalRepo, matchesRepo);
            handler.addAnimal(animal, id1, id2);
        }

        System.out.println(result);
        return result;
    }

    @PostMapping("/deleteAnimal")
    public void deleteAnimal(@RequestParam String animal) {
        handler = new DbHandler(signRepo, animalRepo, matchesRepo);
        for (Animal a : animalRepo.findAll()) {
            if (a.getAnimal().equals(animal)) {
                handler.deleteAnimal(a);
            }
        }
    }

    @GetMapping("/signs")
    public HashSet<Sign> signs() {
        return signRepo.findAll();
    }

    @PostMapping("/addSign")
    public String addSign(@RequestParam String sign) {
        handler = new DbHandler(signRepo, animalRepo, matchesRepo);
        return handler.addSign(new Sign(sign));
    }

    @PostMapping("/deleteSign")
    public void deleteSign(@RequestParam String sign) {
        DbHandler handler = new DbHandler(signRepo, animalRepo, matchesRepo);
        for (Sign s : signRepo.findAll()) {
            if (s.getSign().equals(sign)) {
                handler.deleteSign(s);
            }
        }
    }
}

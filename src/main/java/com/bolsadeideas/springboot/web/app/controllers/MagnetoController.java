package com.bolsadeideas.springboot.web.app.controllers;
import com.bolsadeideas.springboot.web.app.models.Adn;
import com.bolsadeideas.springboot.web.app.models.Candidate;
import com.bolsadeideas.springboot.web.app.models.Statistics;
import com.bolsadeideas.springboot.web.app.service.IadnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.bolsadeideas.springboot.web.app.commos.Validation.isMutant;

@RestController
@RequestMapping("/api")
public class MagnetoController {

    @Autowired
    private IadnService adnService;



    @PostMapping("/mutant")
    public ResponseEntity<String> mutant(@RequestBody Candidate dna) {
        if(isMutant(dna.getDna())){
            adnService.save(new Adn( Arrays.toString(dna.getDna()),"YES"));
            return ResponseEntity.ok().build();
        }
        adnService.save(new Adn(Arrays.toString(dna.getDna()),"NO"));
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }


    @GetMapping("/stats")
    public ResponseEntity<Statistics> stats() {
        Statistics statistics = new Statistics();

        Integer cant_muntante = 0;
        Integer cant_humano = 0;
        Integer cant_total = adnService.findAll().size();
        List<Adn> listAll  = adnService.findAll();
        for (int i = 0; i < cant_total ; i++) {
            if (listAll.get(i).isMutante().equals("YES")){
                cant_muntante++;
            }else{
                cant_humano++;
            }
        }
        statistics.setCount_human_dna(cant_humano);
        statistics.setCount_mutant_dna(cant_muntante);
        if (cant_humano != 0) {
            statistics.setRatio((float) (cant_muntante/ (float )cant_humano));
        }

        return new ResponseEntity<Statistics>(statistics, HttpStatus.OK);
    }
}

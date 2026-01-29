package com.multilang.memo.controller;
import  com.multilang.memo.entity.Concept;
import com.multilang.memo.repository.ConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.List;

@RestController
@RequestMapping("/api/concepts")

public class ConceptController {
    @Autowired
    private  ConceptRepository conceptRepository;
    public  ConceptController(ConceptRepository conceptRepository ){
        this.conceptRepository=conceptRepository;
    }
    @PostMapping
    public  Concept add(@CookieValue("user_id") String userId, @RequestBody Concept concept){
        concept.setUserId(userId);
        return conceptRepository.save(concept);
    }
    @GetMapping  // 全件取得（ユーザー別）
    public List<Concept> getAll(@CookieValue("user_id") String userId, @RequestParam(required = false) String query) {
        long startTime = System.currentTimeMillis();

        List<Concept> concepts;
        if(query!=null&&!query.isEmpty()){
            concepts=conceptRepository.searchByKeyword(userId, query);
        }else{
            concepts=conceptRepository.findAllWithWordsEagerly(userId);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("⏱️ Concept取得時間: " + (endTime - startTime) + "ms(query="+query+")");

        return concepts;
    }
    @GetMapping("/{id}")
    public  Concept getById(@CookieValue("user_id") String userId, @PathVariable Long id){
        long startTime=System.currentTimeMillis();
        Concept concept=conceptRepository.findByIdWithWords(id, userId)
                .orElseThrow(()-> new RuntimeException("Concept not found"));
        long endTime =System.currentTimeMillis();
        System.out.println("Concept詳細："+(endTime-startTime)+"ms");
        return  concept;
    }
    @GetMapping("/search")
    public List<Concept> search(@CookieValue("user_id") String userId, @RequestParam String keyword){
        return  conceptRepository.searchByKeyword(userId, keyword);
    }
    @PutMapping("/{id}")
    public Concept update(@CookieValue("user_id") String userId, @PathVariable Long id, @RequestBody Concept concept){
        Concept existing =conceptRepository.findByIdWithWords(id, userId)
                .orElseThrow(()-> new RuntimeException("Concept not found"));
        existing.setName(concept.getName());
        existing.setNotes(concept.getNotes());
        return  conceptRepository.save(existing);
    }
    @DeleteMapping("/{id}")
    public  void delete(@CookieValue("user_id") String userId, @PathVariable Long id){
        Concept existing = conceptRepository.findByIdWithWords(id, userId)
                .orElseThrow(() -> new RuntimeException("Concept not found"));
        conceptRepository.delete(existing);
    }

}

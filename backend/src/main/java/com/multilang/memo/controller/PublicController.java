package com.multilang.memo.controller;

import com.multilang.memo.entity.Concept;  // ← 修正1: Repositoryじゃなくてエンティティ
import com.multilang.memo.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicController {

    private final SearchService searchService;  // ← 修正2: 変数名を小文字に
    private static final String DEMO_USERNAME = "demo-user";

    public PublicController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/demo-concepts/search")
    public ResponseEntity<List<Concept>> searchDemoConcepts(  // ← 修正3: List<Concept>に
        @RequestParam String keyword
    ) {
        List<Concept> results = searchService.search(DEMO_USERNAME, keyword);  // ← 修正4: メソッド名
        return ResponseEntity.ok(results);
    }
}
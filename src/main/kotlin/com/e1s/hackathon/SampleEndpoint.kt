package com.e1s.hackathon

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleEndpoint {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> = ResponseEntity.ok("Hello hackathon!")
}
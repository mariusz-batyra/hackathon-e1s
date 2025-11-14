package com.e1s.hackathon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HackathonApplication

fun main(args: Array<String>) {
	runApplication<HackathonApplication>(*args)
}

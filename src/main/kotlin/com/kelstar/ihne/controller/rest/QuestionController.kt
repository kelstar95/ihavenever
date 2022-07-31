package com.kelstar.ihne.controller.rest

import com.kelstar.ihne.model.Question
import com.kelstar.ihne.model.dto.QuestionDto
import com.kelstar.ihne.service.QuestionService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/questions")
class QuestionController(
    private val questionService: QuestionService
) {
    @GetMapping
    fun getAll() = questionService.findAllByAdded().toFlux()

    @GetMapping("/random")
    fun getRandom(): Mono<Question> {
        val randomQuestion = questionService.getRandomNotShown()
        return if (randomQuestion != null) {
            randomQuestion
                .toMono()
                .subscribeOn(Schedulers.elastic())
        } else {
            Mono.empty()
        }
    }

    @PostMapping
    fun add(@RequestBody question: QuestionDto) {
        questionService.addQuestion(question)
    }

    @GetMapping("/secretLink/refresh")
    fun refresh() : Flux<Question> = questionService.refreshAll().toFlux()
    
}
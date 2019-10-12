package com.donaldolog.blog.controllers

import com.donaldolog.blog.models.Article
import com.donaldolog.blog.repository.ArticleRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.Id
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController(private val articleRepository: ArticleRepository){

   @GetMapping("/article")
   fun getAllArticles(): List<Article> = articleRepository.findAll()

    @PostMapping("/article")
    fun createArticle(@Valid @RequestBody article: Article): Article = articleRepository.save(article)

    @GetMapping("/articles/{id}")
    fun getArticleById(@PathVariable(value = "id") articleId : Long): ResponseEntity<Article> {
        return articleRepository.findById(articleId).map { article ->
            ResponseEntity.ok(article)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/articles/{id}")
    fun updateArticleById(@PathVariable(value = "id") articleId: Long,
                          @Valid @RequestBody newArticle: Article): ResponseEntity<Article> {
        return articleRepository.findById(articleId).map { existingArticle ->
            val updateArticle: Article = existingArticle
                    .copy(title = newArticle.title, content = newArticle.content)
            ResponseEntity.ok().body(articleRepository.save(updateArticle))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/articles/{id}")
    fun deleteArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Void>{
        return articleRepository.findById(articleId).map { existingArticle ->
            articleRepository.delete(existingArticle)
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }.orElse( ResponseEntity<Void>(HttpStatus.NOT_FOUND))
    }

}
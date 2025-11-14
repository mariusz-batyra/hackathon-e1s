package com.e1s.hackathon.repository

import com.e1s.hackathon.model.Product
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnBean(MongoTemplate::class)
interface ProductRepository : MongoRepository<Product, String> {
    fun findByCategory(category: String): List<Product>
    fun findByNameContainingIgnoreCase(name: String): List<Product>
}


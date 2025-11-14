package com.e1s.hackathon.controller

import com.e1s.hackathon.model.Product
import com.e1s.hackathon.repository.ProductRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = ["host"])
class ProductController(private val productRepository: ProductRepository) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<Product>> {
        val products = productRepository.findAll()
        return ResponseEntity.ok(products)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: String): ResponseEntity<Product> {
        val product = productRepository.findById(id)
        return if (product.isPresent) {
            ResponseEntity.ok(product.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/category/{category}")
    fun getProductsByCategory(@PathVariable category: String): ResponseEntity<List<Product>> {
        val products = productRepository.findByCategory(category)
        return ResponseEntity.ok(products)
    }

    @GetMapping("/search")
    fun searchProducts(@RequestParam name: String): ResponseEntity<List<Product>> {
        val products = productRepository.findByNameContainingIgnoreCase(name)
        return ResponseEntity.ok(products)
    }

    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val savedProduct = productRepository.save(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: String, @RequestBody product: Product): ResponseEntity<Product> {
        return if (productRepository.existsById(id)) {
            val updatedProduct = product.copy(id = id)
            productRepository.save(updatedProduct)
            ResponseEntity.ok(updatedProduct)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): ResponseEntity<Void> {
        return if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}


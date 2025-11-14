package com.e1s.hackathon.config

import com.e1s.hackathon.model.Product
import com.e1s.hackathon.repository.ProductRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = ["host"])
class DataInitializer {

    @Bean
    fun initDatabase(productRepository: ProductRepository) = CommandLineRunner {
        // Clear existing data (optional - remove in production)
        productRepository.deleteAll()

        // Initialize with sample products
        val products = listOf(
            Product(
                name = "Laptop",
                description = "High-performance laptop for developers",
                price = 1299.99,
                category = "Electronics",
                inStock = true
            ),
            Product(
                name = "Wireless Mouse",
                description = "Ergonomic wireless mouse",
                price = 29.99,
                category = "Electronics",
                inStock = true
            ),
            Product(
                name = "Standing Desk",
                description = "Adjustable height standing desk",
                price = 599.99,
                category = "Furniture",
                inStock = true
            ),
            Product(
                name = "Mechanical Keyboard",
                description = "RGB mechanical keyboard with blue switches",
                price = 149.99,
                category = "Electronics",
                inStock = true
            ),
            Product(
                name = "Office Chair",
                description = "Ergonomic office chair with lumbar support",
                price = 399.99,
                category = "Furniture",
                inStock = false
            ),
            Product(
                name = "USB-C Hub",
                description = "7-in-1 USB-C hub adapter",
                price = 49.99,
                category = "Electronics",
                inStock = true
            ),
            Product(
                name = "Monitor",
                description = "27-inch 4K monitor",
                price = 449.99,
                category = "Electronics",
                inStock = true
            ),
            Product(
                name = "Desk Lamp",
                description = "LED desk lamp with adjustable brightness",
                price = 39.99,
                category = "Furniture",
                inStock = true
            )
        )

        productRepository.saveAll(products)

        println("Database initialized with ${products.size} products")
    }
}


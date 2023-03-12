package ru.itmo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.model.Product;
import ru.itmo.repository.ProductRepository;
import ru.itmo.repository.UserRepository;

@RestController
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/get/products")
    public Flux<Product> onAllProduct() {
        return productRepository.findAll();
    }

    @GetMapping("/get/products/{id}")
    public Flux<Product> onAllProductsById(@PathVariable("id") String id) {
        return userRepository
                .findById(id)
                .flatMapMany(user -> productRepository.findByCurrency(user.getCurrency()));
    }

    @PostMapping("/save/product")
    public Mono<Product> onSaveCatalog(@RequestBody Product product) {
        return productRepository.save(product);
    }
}

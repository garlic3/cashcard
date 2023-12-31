package com.example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// tells Spring that this class is Component of type RestController
// and capable of handling HTTP requests
@RestController
// indicates which address requests must have to access this Controller
@RequestMapping("/cashcards")
public class CashCardController {

    // Spring's Auto Configuration is utilizing its dependency injection(DI) framework,
    // specifically constructor injection, to supply CashCardController with the correct implementation of
    // CashCardRepository at runtime
    private final CashCardRepository cashCardRepository;

    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }


    // @GetMapping: marks a method as a handler method
    // @PathVariable : makes Spring Web aware of the requestedId supplied in the HTTP
    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId){
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);

        if(cashCardOptional.isPresent()){
            return ResponseEntity.ok(cashCardOptional.get());
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(){
        return null;
    }


}

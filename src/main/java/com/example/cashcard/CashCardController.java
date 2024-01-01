package com.example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


// tells Spring that this class is Component of type RestController
// and capable of handling HTTP requests
@RestController
// indicates which address requests must have to access this Controller
@RequestMapping("/cashcards")
class CashCardController {

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
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal){

        // Principal : holds our user's authenticated, authorized information
        CashCard cashCard = findCashCard(requestedId, principal);

        if(cashCard != null){
            return ResponseEntity.ok(cashCard);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal){
        CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());

        // saves a new CashCard for us, and returns the saved object with a unique id provided by the database
        CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
        URI locationOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();

    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal){
        // PageRequest : Java Bean implementation of Pageable
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        // getSort() : extracts the sort query parameter from the request URI
                        // getSortOr() : provides default values for the page, size, sort parameters
                        // page : 0, size : 20
                        // if any of the three required parameters are not passed to the application,
                        // then reasonable defaults will be provided
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                )
        );
        return ResponseEntity.ok(page.getContent());

    }
    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal){
        // 기존 cashCard
        CashCard cashCard = findCashCard(requestedId, principal);

        if(cashCard != null){
            // 새로운 cashCard
            CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
            cashCardRepository.save(updatedCashCard);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    // helper method
    private CashCard findCashCard(Long requestId, Principal principal){
        return cashCardRepository.findByIdAndOwner(requestId, principal.getName());
    }



}

package com.example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// tells Spring that this class is Component of type RestController
// and capable of handling HTTP requests
@RestController
// indicates which address requests must have to access this Controller
@RequestMapping("/cashcards")
public class CashCardController {


    // @GetMapping: marks a method as a handler method
    // @PathVariable : makes Spring Web aware of the requestedId supplied in the HTTP
    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId){
        // data management -> shouldn't be concerned
        if(requestedId.equals(99L)){
            CashCard cashCard = new CashCard(99L, 123.45);
            return ResponseEntity.ok(cashCard);
        }else{
            return ResponseEntity.notFound().build();
        }

//        CashCard cashCard = new CashCard(99L, 123.45);
//        return ResponseEntity.ok(cashCard);
    }
}

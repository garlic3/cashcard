package com.example.cashcard;

import org.springframework.data.annotation.Id;


// @Id : tells Spring Data which field is the ID
record CashCard(@Id Long id, Double amount) {
}

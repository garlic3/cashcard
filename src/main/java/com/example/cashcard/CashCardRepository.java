package com.example.cashcard;

import org.springframework.data.repository.CrudRepository;

// CrudRepository : an interface supplied by Spring data
// <domain type, ID type>
public interface CashCardRepository extends CrudRepository<CashCard, Long>  {

    // NoSuchBeanDefinitionException
    // Spring is trying to find a properly configured class to provide
    // during the dependency injection phase of Auto Configuration, but none qualify


}

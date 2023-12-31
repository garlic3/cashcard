package com.example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

// CrudRepository : an interface supplied by Spring data
// <domain type, ID type>
public interface CashCardRepository extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {

    // NoSuchBeanDefinitionException
    // Spring is trying to find a properly configured class to provide
    // during the dependency injection phase of Auto Configuration, but none qualify

    CashCard findByIdAndOwner(Long id, String owner);
    Page<CashCard> findByOwner(String owner, PageRequest pageRequest);

}

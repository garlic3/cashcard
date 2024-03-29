### Spring
- comprehensive framework that provides various modules for building different types of applications

### Spring Boot
- more opinionated, streamlined version of Spring
- with an embedded web server

### Spring's Inversion of Control Container
-Spring boot allows to configure how and when dependencies are provided to application at runtime
- Spring boot allows to provide an external configuration that specifies how and when such dependencies ard used
- Dependency injection and accompanying frameworks are one way ofr achieving inversion of control, and Spring developers 
will often state that dependencies are "injected" into their applications at runtime

### API Contract & JSON

- how should API consumers interact with the API?
- What data do consumers need to sen in various scenarios?
- What data should the API return to consumers, and when?
- What does the API communicate when it's used incorrectly?

### API Contracts

- Consumer Driven Contracts
- Provider Driven Contracts
- as a formal agreement between a software provider and a consumer that abstractly communicates how to interact
with each other
- This contract defines how API providers and consumers interact, what data exchanges looks like,
and how to communicate success and failure cases
- The provider and consumers only share the same API contracts

### Why Are API Contracts Important?

- API contracts communicate the behavior of a REST API
- provide specific details about the data being serialized(or deserialized) for each command and parameter being exchanged

### Why Is JSON?

- JSON(Javascript Object Notation) provides a data interchange format that represents the particular information of
an object in a format
- When compared to XML(Extensible Markup Language), JSON reads and writes quicker, is easier to use, and takes up less space


### What Is Test Driven Development(TDD)?

- test before implementing the application code
- By asserting expected behavior before implementing the desired functionality, designing the system based on 
what we want it to do, rather than what the system already does
- the tests guide you to write the minimum code needed to satisfy the implementation
- tried-and-true technique to help application developers design simple yet robust software, and guard against functionality
regressions and bugs

### The Testing Pyramid

- Unit Tests : exercises a small "unit" of the system that's isolated from the rest of the system
- a high ratio of Unit Test in testing pyramid, as they're key to designing highly cohesive, loosely coupled software
- Integration Tests : exercise a subset of the system and may exercise groups of units in one test
- They are more complicated to write and maintain, and run slower than unit tests
- End-to-End Tests : exercises the system using the same interface that a user would, such as a web browser
- They can be very slow and fragile because they use simulated user interactions in potentially complicated UIs
- Implement the smallest number of these tests

### The Red, Green, Refactor Loop

- Red : Write a failing test for the desired functionality
- Green : Implement the simplest thing that can work to make the test pass
- Refactor : Look for opportunities to simplify, reduce duplication, or otherwise improve the code without changing 
any behavior to refactor

### REST, CRUD, and HTTP

- REST : Representational State Transfer
- data objects : Resource Representations
- The purpose of RESTful API : to manage the state of these Resources
- CRUD : the four basic perations that can be performed on objects in data store
- In HTTP, a caller sends a Request to a URI -> A web server receives the request, and routes it to a request hanlder 
-> The handler creates a Response, which is then sent back to the caller

- Request : Method(Verb), URI(Endpoint), Body
- Response : Status Code, Body

- For CREATE : POST
- For READ : GET
- For UPDATE : PUT
- For DELETE : DELETE

### The Request Body

- To create or update a resource, we need to submit data to the API

### Spring Annotations and Component Scan

- Spring Beans : objects that Spring configure and instantiate
- Spring Annotation : directs Spring to create an instance of the class during Spring's Component Scan phase
- The Bean is stored in Spring's IoC container

### Spring Web Controllers

- In Spring Web, Requests are handled by Controllers
- @RestController : The Controller gets injected into Spring Web, which routes API requests(handled by the Controller) 
to the correct method

``` 
@RestController 
class CashCardController{
}
 ```

- A Controller method can be designated a handler method, to be called when a request that the method knows how to handle
  (called a "matching request") is receiced

```
private CashCard findById(Long requestId){
}
```

- Read endpoints should use the HTTP GET method
- Spring route requests to the method only on GET requests

```
@GetMapping("/cashcards/{requestedId}")
private CashCard findById(Long requestedId){
}
```

- The parameter name matches the {requestId} text within the @Getmapping parameter,
Spring assign(inject) the correct value to the requestedId variable

```
@GetMappting("/cashcards/{requestedId}")
private CashCard findById(@PathVariable Long requestedId){

}
```

- REST says that the Response needs to contain Body and a Response code of 200(OK)
- ResponseEntity class : provides several utility methods to produce Response Entities

```
@RestController
class CashCardController{
    @GetMapping("/cashcards/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestId){
        CashCard cashCard = /* */
        return ResponseEntity.ok(cashCard);
    }
}
```

### Controller-Repository Architecture

- The Separation of Concerns principle states that well-designed software should be modular,
with each module having distinct and separate concerns from any other module

- mixing the concerns of Controller, which is an abstraction of a web interface, with the concerns of
reading and writing data to a data store, such as a database
- to enforce data managements separation via the Repository pattern
- Controller : in a layer near the client(as it receives and responds to web requests)
- Repository : in a layer near the data store(as it reads from and writes to the data store)
- The Repository is the interface between the application and the database, and provides a common abstraction for any
database, making it easier to switch to a different database when needed

- API Clients(Web browsers, apps, other applications) 
-> Controller(Communications Interface Layer) 
-> Other Layers(Business logic) 
-> Persistence Layer(Repositories) 
-> Data Store

### Choosing a database

- Embedded : it's a java library, so it can be added to the project just like any other dependency
- In-memory : it stores data in memory only, as opposed to persisting data in permanent, durable storage


### Spring Data's CrudRepository

- Spring Data takes care of this implementation for us during the IoC container startup time
- The Spring runtime will then expose the repository as yet another bean that you can reference wherever needed in application

```
interface CashCardRepository extends CrudRepository<CashCard, Long> {

}
```

### Implementing POST

- Who specifies the ID - the client, or the server?
- In the API Request, how do we represent the object to be created?
- Which HTTP method should we use in the Requests?
- What does the API send as a Response?

### Idempotence and HTTP

- Idempotence operation : as one which, if performed more than once, results in the same outcome
- In REST API, an idempotent operation is one that even if it were to be performed several times, the resulting data on
the server would be the same as if it had been performed only once
- GET, PUT, DELETE : idempotent
- POST, PATCH : not idempotent
- REST permits POST as one of the proper methods to use for Create operations

### The POST Request and Response

- Request : Method(POST), URI(/cashcards/), Body({amount:123.45})

- 201 CREATED status : the API is specifically communicating that data was added to the data store on the server
- The HTTP standard specifies that the Location header in a 201 CREATED response should contain the URI of created resource
- This is handy because it allows the caller to easily fetch the new resource using the GET endpoint
- Response : Status Code(201 CREATED), Header(Location=/cashcards/42)

### Spring Web Convenience Methods

- created() method
```
return ResponseEntity.created(uriOfCashCard).build();
```

```
return ResponseEntity
        .status(HttpStatus.CREATED)
        .header(HttpHeaders.LOCATION, uriOfCashCard.toASCIIString())
        .build();
```

### Requesting a List of Cash Cards

- findAll() method : fetch all the Cash Cards in the database

```
@GetMapping()
private ResponseEntity<Iterable<CashCard>> findAll(){
  return ResponseEntity.ok(cashCardRepository.findAll());
}
```

- How do I return only the Cash Cards that user owns?
- What if there are hundreds of Cash Card?
  Should the API return an unlimited number of results or return them in "chunks"?
- Should the Cash Cards be returned in a particular order?

### Pagination and Sorting

- Ideally, an API should not be able to produce a response with unlimited size, because this could overwhelm the client 
or server memory, not the mention taking quite a long time
- Pagination in Spring is to specify the page length and the page index

### Regarding Unordered Queries

- Although Spring does provide an "unordered" sorting strategy, let's be explicit when we select which fields for sorting
- Minimize cognitive overhead 
- Minimize future errors

### Spring Data Pagination API

```
Page<CashCard> page2 = cashCardRepository.findall(
  PageRequest.of(
    1, // page index for the second page - indexing starts at 0
    10, // page size(the last page might have fewer items)
    Sort.by(new Sort.Order(Sort.Direction.DESC, "amount"))
);
```

### The Request and Response

- Pagination : Spring can parse out the page and size parameters if you pass Pageable object to a PagingAndSortingRepository
find..() method
- Sorting : Spring can parse out the sort parameter, consisting of the field name and the direction separated by a comma


### The URI

1. Get the second Page
```
  /cashcards?page=1
```
2. ... where a page has length of 3
```
  /cashcards?page=1&size=3
```
3. ... sorted by the current Cash Card balance
```
  /cashcards?page=1&size=3&sort=amount
```
4. ... in descending order
```
  /cashcards?page=1&size=3&sort=amount,desc
```

### The Java Code

- Pageable : allows Spring to parse out the page number and size query string parameters
- Spring provides defaults : page=0, size=20
- page.getContent() method : return the Cash Cards contained in the Page object to the caller

```
@GetMapping
private ResponseEntity<List<CashCards>> findAll(Pageable pageable) {
  Page<CashCard> page = cashCardRepository.findAll(
    PageRequest.of(
      pageable.getPageNumber(),
      pageable.getPageSize(),
      pageable.getSortOr(Sort.by(Sort.Direction.DESC, "amount"))
  );
  return ResponseEntity.ok(page.getContent());
}
```

### Authentication

- Authentication is the act of a Principal proving its identity to the system
- provide credentials(a username and password using Basic Authentication)
- The proper credentials have been presented, the Principal is authenticated(logged in)
- HTTP is a stateless protocol, so each request must contain data that proves it's from an authenticated Principal
- An Authentication Session(or Auth Session, or just Session) is created when a user gets authenticated
- A Session Token(a string of random characters) that is generated, and placed in a Cookie
- A Cookie is a set of data stored in a web client(such as a browser), and associated with a specific URI

- Cookies are automatically sent to the server with every request(As long as the server checks that the Token in the 
Cookie is valid, unauthenticated requests can be rejected)
- Cookies can persist for a certain amount of time even if the web page is closed and later re-visited

### Spring Security and Authentication

- Spring Security implements authentication in the Filter Chain
- The Filter Chain is a component of Java web architecture which allows programmers to define a sequence of methods 
that get called prior to the Controller
- Each Filter in the chain decides whether to allow request processing to continue, or not
- Spring Security inserts a filter which checks the user's authentication and returns with a 401 UNAUTHORIZED response 
if the request is not authenticated

### Authorization

- Authorization happens after authentication, and allows different users of the same system to have different permissions

- Spring Security provides Authorization via Role-Based Access Control(RBAC)
- Principal has a number of Roles
- Each resource(or operation) specifies which Roles a Principal must have in order to perform actions with proper authorization
- can configure role-based authorization at both a global level and a per-method basis

### Same Origin Policy

- This policy states that only scripts which are contained in a web page are allowed to send requests to the origin(URI) of the web page

### Cross-Origin Resource Sharing

- Cross-Origin Resource Sharing(CORS) is a way that browsers and servers cna cooperate to relax the SOP
- A server can explicitly allow a list of "allowed origins" of requests coming from an origin outside the server's
- @CrossOrigin(if use the annotation without any arguments, it will allow all origins)

### Common Web Exploits

### Cross-Site Request Forgery(CSRF)

- Cross-Site Request Forgery(CSRF) : "Sea-Surf", Session Riding. enabled by Cookies
- CSRF attacks happen when a malicious piece of code sends a request to a server where a user is authenticated
- When the server receives the Authentication Cookie, it has no way of knowing if the victim sent the harmful 
request unintentionally
- To protect against CSRF attacks, use a CSRF Token(a unique token is generated on each request)

### Cross-Site Scripting(XSS)

- This occurs when an attacker is somehow able to "trick" the victim application into executing arbitrary code
- In XSS, arbitrary malicious code executes on the client or on the server
- XSS attacks don't depend on Authentication
- The main way to guard against XSS attacks is to properly process all data from external sources(like web forms and URI
query strings)

### PUT and PATCH

- PUT : create or replace the complete record
- PATCH : update only some fields of the existing record(partial update)
- Partial updates free the client from having to load the entire record and then
transmit the entire record back to the server

### PUT and POST

- The HTTP standard doesn't specify whether the POST or PUT verb is preferred for a Create operation

### Surrogate and Natural Keys

- Natural Key(supplied by the client to the API)
- Surrogate Key(usually generated by the server)

- The important difference is whether the URI(which includes the ID of the resource) needs to be generated by the server or not
- POST : If the server need to return the URI of the created resource(or the data user to construct the URI)
- PUT : When the resource URI is known at creation time

### Resources and Sub-Resource

- POST : create a sub-resource(child resource) under(after), or within the request URI
- PUT : creates or replaces(updates) a resource at a specific request URI

### Response Body and Status Code

- Return 201 CREATED(if you created the object)
- Return 200 OK(if you replaced an existing object)
- Return 204 NO CONTENT(an empty response body)

| HTTP Method | Operation   | Definition of Resource URI           | What does it do?                                           | Response Status Code | Response Body        |
|-------------|-------------|--------------------------------------|------------------------------------------------------------|----------------------|----------------------|
| POST        | Create      | Server generates and returns the URI | Creates a sub-resource("under" or "within" the passed URI) | 201 CREATED          | The Created resource |
| PUT         | Create      | Client supplies the URI              | createds a resource(at the Request URI)                    | 201 CREATED          | The created resource |
| PUT         | Update      | Client supplies the URI              | Replaces the resource(The entire recored is replaced)      | 204 NO CONTENT       | (empty)              |
| PATCH       | Update      | Client supplies the URI              | Partial Update(modify only fields included in the request) | 200 OK               | The updated resource |


[//]: # (PUT won’t support creating a Cash Card.)

[//]: # (Our new Update endpoint &#40;which we'll build in the upcoming Lab&#41;:)

[//]: # (will use the PUT verb.)

[//]: # (accepts a Cash Card, and replaces the existing Cash Card with it.)

[//]: # (on success, will return 204 NO CONTENT with an empty body.)

[//]: # (will return a 404 NOT FOUND for an unauthorized update, as well as attempts to update nonexistent IDs.)


### Implementing DELETE

- Request : Verb(DELETE), URI(/cashcards/{id}), Body(empty)
- Response : Status Code(204 NO CONTENT), Body(empty)

| Response Code  | Use Case                               |
|----------------|----------------------------------------|
| 204 NO CONTENT | record ok, principal ok, delete ok     |
| 404 NOT FOUND  | record does not exist(non-existent ID) |
| 404 NOT FOUND  | record ok, principal is not the owner  |


### Additional Options
### Hard and Soft Delete

- hard delete : delete record from the database
- soft delete : marking records as "deleted" in the database
- IS_DELETED boolean or a DELETED_DATE timestamp column(in database)

### Audit Trail and Archiving





















































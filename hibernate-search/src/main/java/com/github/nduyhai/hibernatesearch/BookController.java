//package com.github.nduyhai.hibernatesearch;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//public class BookController {
//
//    private SearchService searchService;
//
//    private BookRepository bookRepository;
//
//    public BookController(SearchService searchService) {
//        this.searchService = searchService;
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Book>> search(@RequestParam String criteria) {
//        try {
//            return new ResponseEntity<>(this.searchService.search(criteria, 10), HttpStatus.OK);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/fuzzy")
//    public ResponseEntity<List<Book>> fuzzy(@RequestParam String criteria) {
//        try {
//            return new ResponseEntity<>(this.searchService.fuzzy(criteria, 10), HttpStatus.OK);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/book")
//    public ResponseEntity<String> add(@RequestBody Book book) {
//        try {
//            this.bookRepository.save(book);
//            return new ResponseEntity<>(HttpStatus.OK);
//
//        } catch (Exception ex) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public BookRepository getBookRepository() {
//        return bookRepository;
//    }
//
//    @Autowired
//    public void setBookRepository(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//}

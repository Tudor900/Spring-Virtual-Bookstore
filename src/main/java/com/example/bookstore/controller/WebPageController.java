package com.example.bookstore.controller;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.entity.Genre;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.service.CustomerService;
import com.example.bookstore.service.GenreService;
import com.example.bookstore.service.AuthorService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller

public class WebPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private AuthorService authorService;

    @GetMapping("/")
    public String homePage(@CookieValue(name="userId", required = false)String userId, Model model){
        model.addAttribute("listofbooks", bookService.fetchBookList());

        Boolean isLoggedIn = (userId != null && !userId.isEmpty());
        if(isLoggedIn){

            Boolean userexists = customerService.checkForCustomerByUniqueId(userId);
            Boolean isAdmin = customerService.checkForAdmin(userId);
            model.addAttribute("isAdmin", isAdmin);
        }
        else {
            model.addAttribute("isAdmin", false);
        }


        model.addAttribute("isLoggedIn", isLoggedIn);



        return "index";
    }

    @GetMapping("/addBook")
    public String addBookPage(Model model){
        Book book = new Book();
        book.setAuthor(new Author());
        book.setGenre(new Genre());

        model.addAttribute("genreList",genreService.fetchGenreList());
        model.addAttribute("authorList", authorService.fetchAuthorList());


        model.addAttribute("book", book);
        return "addBooks";
    }

    @PostMapping("/savebook")
    public String saveBook(@ModelAttribute("book") Book book){
        bookService.saveBook(book);
        return "redirect:/";
    }

    @GetMapping ("/createaccount")
    public String createAccount(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "createAccount";
    }

    @PostMapping("/savenewaccount")
    public String saveNewAccount(@ModelAttribute("customer") Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model){
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "loginform";
    }

    @PostMapping("/logincheck")
    public String checkLogin(@ModelAttribute("customer") Customer customer, HttpServletResponse response){

        Boolean loginCorrect = customerService.checkForCustomer(customer);
        if(loginCorrect) {
            Customer customer1 = customerService.getCustomer(customer);
            String userIdentifier = customer1.getUniqueID();
            System.out.println(customer1);
            System.out.println(userIdentifier);
            Cookie userCookie = new Cookie("userId", userIdentifier);

            userCookie.setMaxAge(60 * 60);
            userCookie.setPath("/");
            response.addCookie(userCookie);
            return "redirect:/";
        }
        return "redirect:/login?error=true";

    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        if(cookies!= null){
            for (Cookie cookie : cookies)
            {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

        }
        return "redirect:/";
    }

    @PostMapping("/savegenre")
    public String saveGenre(@ModelAttribute("genre")Genre genre)
    {
        genreService.saveGenre(genre);
        return "redirect:/";
    }

    @PostMapping("/saveauthor")
    public String saveAuthor(@ModelAttribute("author")Author author)
    {
        authorService.saveAuthor(author);
        return "redirect:/";
    }


    @GetMapping ("/addgenreorauthor")
    public String addAuthorOrGenre(Model model)
    {
        Genre genre = new Genre();
        Author author = new Author();
        model.addAttribute("genre", genre);
        model.addAttribute("author", author);
        return "addAuthor&Genre";
    }

    @GetMapping("/useraccount")
    public String userAccountSettings(@CookieValue(name="userId", required = true)String userId, Model model)
    {
        Customer customer = customerService.getCustomerByUniqueId(userId);
        model.addAttribute("customer", customer);
        return "usersettings";
    }

    @PostMapping("/saveaccount/{param}")
    public String updateAccount(@CookieValue(name="userId", required = true)String userId, @PathVariable String param, @RequestParam Map<String, String> formData)
    {
        customerService.updateCustomer(param, userId, formData);


        return "redirect:/useraccount";
    }





}

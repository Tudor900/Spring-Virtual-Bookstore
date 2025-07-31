package com.example.bookstore.controller;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.entity.Genre;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.service.CustomerService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;


@Controller

public class WebPageController {

    @Autowired
    private BookService bookService;
    @Autowired
    private CustomerService customerService;

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




}

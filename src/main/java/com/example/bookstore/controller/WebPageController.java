package com.example.bookstore.controller;

import com.example.bookstore.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.bookstore.service.BookService;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.service.CustomerService;
import com.example.bookstore.service.GenreService;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.OrderItemService;
import com.example.bookstore.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;


import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.valueOf;


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
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;




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

    @GetMapping("/secured-page")
    public String securedPage(Model model, OAuth2AuthenticationToken authentication) {
        // Retrieve user attributes from the OAuth2AuthenticationToken
        var userAttributes = authentication.getPrincipal().getAttributes();
        model.addAttribute("name", userAttributes.get("name"));
        model.addAttribute("email", userAttributes.get("email"));

        return "secured-page"; // This is a page only accessible to authenticated users
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

//    @PostMapping("/addbooktocart")
//    public String addBookToCart(@CookieValue(name="userId", required = true)String userId,@RequestParam Map<Long, Long> formData,HttpServletResponse response)
//    {
//
//    }


    @GetMapping("/checkout")
    public String showCart(@CookieValue(name = "shoppingCart", required = false) String shoppingCartCookie, Model model, @CookieValue(name="userId") String userId) {
        List cartBooks = Collections.emptyList();

        if (shoppingCartCookie != null && !shoppingCartCookie.isEmpty()) {
            try {
                // Use Jackson's ObjectMapper to parse the JSON string from the cookie
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Integer> cartItems = objectMapper.readValue(shoppingCartCookie, Map.class);

                // Extract the book IDs from the map's keys and convert to a List of Long
                List<Long> bookIds = cartItems.keySet().stream()
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                // Call the service method with the list of IDs
                cartBooks = bookService.findByBookIDIn(bookIds);

                // Add the cart quantities to the model if needed (optional)
                model.addAttribute("cartItems", cartItems);
                System.out.println(cartItems);

            } catch (Exception e) {
                // Handle parsing errors, e.g., malformed cookie data
                e.printStackTrace();
            }
        }
        System.out.println(cartBooks);

        model.addAttribute("cartBooks", cartBooks);

        Customer customer = customerService.getCustomerByUniqueId(userId);

        model.addAttribute("customer", customer);

        return "checkout";
    }


    @PostMapping("/sendorder")
    public String sendOrder(@CookieValue(name = "shoppingCart", required = true) String shoppingCartCookie, Model model, @CookieValue(name="userId", required = true) String userId) {
        List cartBooks = Collections.emptyList();
        Customer customer = customerService.getCustomerByUniqueId(userId);

        List<OrderItem> orderItemList = new ArrayList<>();

        Order order = Order.builder().customer(customer).build();
        if (shoppingCartCookie != null && !shoppingCartCookie.isEmpty()) {
            try {










                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Integer> cartItems = objectMapper.readValue(shoppingCartCookie, Map.class);

                for (Map.Entry<String, Integer> entry : cartItems.entrySet()){
                    String bookID = entry.getKey();
                    Book book = bookService.findByBookID(valueOf(bookID));

                    Integer bookQuant = entry.getValue();


                    OrderItem orderItem = OrderItem.builder().order(order).book(book).quantity(bookQuant).build();

                    order.getOrderItems().add(orderItem);
                }





            } catch (Exception e) {
                // Handle parsing errors, e.g., malformed cookie data
                e.printStackTrace();
            }
        }


        orderService.saveOrder(order);




        return "redirect:/checkout";
    }




    @GetMapping("/myorders")
    public String myOrders(@CookieValue(value = "userId", required = true)String userId, Model model)
    {
        Customer customer = customerService.getCustomerByUniqueId(userId);
        List<Order> orders = orderService.fetchCustomerOrders(customer);

        model.addAttribute("listoforders", orders);
        return "/myorders";
    }

    @GetMapping("/allorders")
    public String allOrders(@CookieValue(value ="userId")String userId, Model model){
        if(customerService.checkForAdmin(userId)){
            List<Order> orders = orderService.fetchOrderList();
            model.addAttribute("listoforders", orders);
            return "/allorders";
        }
        return "redirect:/";
    }
}

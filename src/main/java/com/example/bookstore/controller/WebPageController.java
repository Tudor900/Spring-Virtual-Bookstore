package com.example.bookstore.controller;

import com.example.bookstore.entity.*;
import com.example.bookstore.security.CustomOAuth2User;
import com.example.bookstore.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.example.bookstore.entity.Customer;
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

    private final CustomUserDetailService customUserDetailService;


    public WebPageController(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("isLoggedIn", false); // or true, depending on logic
        model.addAttribute("isAdmin", false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null; // Initialize customer to null



        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            Object principal = authentication.getPrincipal();
            System.out.println("DEBUG: Principal class is: " + principal.getClass().getName());

            if (principal instanceof Customer) {
                customer = (Customer) principal;
                System.out.println("DEBUG: Customer from form login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);
            } else if (principal instanceof CustomOAuth2User) {
                customer = ((CustomOAuth2User) principal).getCustomer();
                System.out.println("DEBUG: Customer from OAuth2 login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            }

            if (customer != null) {
                model.addAttribute("customer", customer);
                System.out.println("DEBUG: 'customer' object added to model in index method.");
            } else {
                System.out.println("DEBUG: 'customer' object is null after principal check in index method.");
            }
        } else {
            System.out.println("DEBUG: Not authenticated or anonymous customer in index method.");
        }

        model.addAttribute("listofbooks", bookService.fetchBookList());
        if(customerService.checkForAdmin(customer.getUniqueID())) {
            model.addAttribute("isAdmin", true);
        }
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

    @GetMapping("/register")
    public String register(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "register";
    }

    @PostMapping("/register")
    public String saveRegister(@ModelAttribute("customer") Customer customer) {
        customUserDetailService.registerUser(customer);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
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
    public String userAccountSettings(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null; // Initialize customer to null

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            Object principal = authentication.getPrincipal();
            System.out.println("DEBUG: Principal class is: " + principal.getClass().getName());

            if (principal instanceof Customer) {
                customer = (Customer) principal;
                System.out.println("DEBUG: Customer from form login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            } else if (principal instanceof CustomOAuth2User) {
                customer = ((CustomOAuth2User) principal).getCustomer();
                System.out.println("DEBUG: Customer from OAuth2 login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            }

            if (customer != null) {
                model.addAttribute("customer", customer);
                System.out.println("DEBUG: 'customer' object added to model in index method.");
            } else {
                System.out.println("DEBUG: 'customer' object is null after principal check in index method.");
            }
        } else {
            System.out.println("DEBUG: Not authenticated or anonymous customer in index method.");
        }
        model.addAttribute("customer", customer);
        return "usersettings";
    }

    @PostMapping("/saveaccount/{param}")
    public String updateAccount(@PathVariable String param, @RequestParam Map<String, String> formData, HttpServletResponse response) {
        // Get the current customer from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer currentCustomer = null;

        if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User) {
            currentCustomer = ((CustomOAuth2User) authentication.getPrincipal()).getCustomer();
        } else if (authentication != null && authentication.getPrincipal() instanceof Customer) {
            currentCustomer = (Customer) authentication.getPrincipal();
        }

        if (currentCustomer != null) {
            // Update the customer in the database
            customerService.updateCustomer(param, currentCustomer.getUniqueID(), formData);

            // After the update, reload the customer and update the security context
            Customer updatedCustomer = customerService.getCustomerByUniqueId(currentCustomer.getUniqueID());
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                    updatedCustomer,
                    authentication.getCredentials(),
                    authentication.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        }

        // Redirect back to the user account page to show the updated information
        return "redirect:/useraccount";
    }

//    @PostMapping("/addbooktocart")
//    public String addBookToCart(@CookieValue(name="userId", required = true)String userId,@RequestParam Map<Long, Long> formData,HttpServletResponse response)
//    {
//
//    }


    @GetMapping("/checkout")
    public String showCart(@CookieValue(name = "shoppingCart", required = false) String shoppingCartCookie, Model model) {
        List cartBooks = Collections.emptyList();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null; // Initialize customer to null

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            Object principal = authentication.getPrincipal();
            System.out.println("DEBUG: Principal class is: " + principal.getClass().getName());

            if (principal instanceof Customer) {
                customer = (Customer) principal;
                System.out.println("DEBUG: Customer from form login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            } else if (principal instanceof CustomOAuth2User) {
                customer = ((CustomOAuth2User) principal).getCustomer();
                System.out.println("DEBUG: Customer from OAuth2 login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            }

            if (customer != null) {
                model.addAttribute("customer", customer);
                System.out.println("DEBUG: 'customer' object added to model in index method.");
            } else {
                System.out.println("DEBUG: 'customer' object is null after principal check in index method.");
            }
        } else {
            System.out.println("DEBUG: Not authenticated or anonymous customer in index method.");
        }



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


        model.addAttribute("customer", customer);


        return "checkout";
    }


    @PostMapping("/sendorder")
    public String sendOrder(@CookieValue(name = "shoppingCart", required = true) String shoppingCartCookie, Model model) {
        List cartBooks = Collections.emptyList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null; // Initialize customer to null

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            Object principal = authentication.getPrincipal();
            System.out.println("DEBUG: Principal class is: " + principal.getClass().getName());

            if (principal instanceof Customer) {
                customer = (Customer) principal;
                System.out.println("DEBUG: Customer from form login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            } else if (principal instanceof CustomOAuth2User) {
                customer = ((CustomOAuth2User) principal).getCustomer();
                System.out.println("DEBUG: Customer from OAuth2 login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            }

            if (customer != null) {
                model.addAttribute("customer", customer);
                System.out.println("DEBUG: 'customer' object added to model in index method.");
            } else {
                System.out.println("DEBUG: 'customer' object is null after principal check in index method.");
            }
        } else {
            System.out.println("DEBUG: Not authenticated or anonymous customer in index method.");
        }

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
    public String myOrders(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null; // Initialize customer to null

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            Object principal = authentication.getPrincipal();
            System.out.println("DEBUG: Principal class is: " + principal.getClass().getName());

            if (principal instanceof Customer) {
                customer = (Customer) principal;
                System.out.println("DEBUG: Customer from form login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            } else if (principal instanceof CustomOAuth2User) {
                customer = ((CustomOAuth2User) principal).getCustomer();
                System.out.println("DEBUG: Customer from OAuth2 login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            }

            if (customer != null) {
                model.addAttribute("customer", customer);
                System.out.println("DEBUG: 'customer' object added to model in index method.");
            } else {
                System.out.println("DEBUG: 'customer' object is null after principal check in index method.");
            }
        } else {
            System.out.println("DEBUG: Not authenticated or anonymous customer in index method.");
        }
        List<Order> orders = orderService.fetchCustomerOrders(customer);

        model.addAttribute("listoforders", orders);
        return "/myorders";
    }

    @GetMapping("/allorders")
    public String allOrders(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = null; // Initialize customer to null

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            Object principal = authentication.getPrincipal();
            System.out.println("DEBUG: Principal class is: " + principal.getClass().getName());

            if (principal instanceof Customer) {
                customer = (Customer) principal;
                System.out.println("DEBUG: Customer from form login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            } else if (principal instanceof CustomOAuth2User) {
                customer = ((CustomOAuth2User) principal).getCustomer();
                System.out.println("DEBUG: Customer from OAuth2 login. Email: " + customer.getEmail() + ", Firstname: " + customer.getFirstname());
                model.addAttribute("isLoggedIn", true);

            }

            if (customer != null) {
                model.addAttribute("customer", customer);
                System.out.println("DEBUG: 'customer' object added to model in index method.");
            } else {
                System.out.println("DEBUG: 'customer' object is null after principal check in index method.");
            }
        } else {
            System.out.println("DEBUG: Not authenticated or anonymous customer in index method.");
        }




        if(customerService.checkForAdmin(customer.getUniqueID())){
            List<Order> orders = orderService.fetchOrderList();
            model.addAttribute("listoforders", orders);
            return "/allorders";
        }
        return "redirect:/";
    }
}

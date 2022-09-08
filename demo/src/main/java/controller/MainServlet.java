/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import dao.AccountDao;
import dao.BookDao;
import dao.OrderDao;
import dao.UserDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Account;
import model.Book;
import model.Order;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns ={"/app/*", "/app"})
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDao userDAO;
    private AccountDao accountDao;
    private BookDao bookDao;
    private OrderDao orderDao;

    public void init() {
        userDAO = new UserDao();
        accountDao = new AccountDao();
        bookDao = new BookDao();
        orderDao = new OrderDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();

        switch (action) {
            case "/register":
                showRegisterForm(request, response);
                break;
            case "/login":
                showLoginForm(request, response);
                break;
            case "/add":
                insertUser(request, response);
                break;
            case "/check":
                login(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            case "/search":
                search(request, response);
                break;
            default:
                showHomePage(request, response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User userLogined = userDAO.login(name, password);
        if(userLogined!=null){
            request.getSession().setAttribute("userLogined", userLogined);
            request.getRequestDispatcher("/app/home").forward(request, response);
        }else{
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.getSession().setAttribute("userLogined", null);
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    private void showHomePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String router = "/login.jsp";
        User loginedUser = (User)request.getSession().getAttribute("userLogined");
        if(loginedUser!=null){
            Order unconfirmedOrder = orderDao.findUnconfirmOrderByUserID(loginedUser);
            Map<Book, Integer> cart = new HashMap<>();
            if(unconfirmedOrder != null){
                cart = bookDao.findBooksFromOrder(unconfirmedOrder.getId());
            }
            request.setAttribute("cart", cart);
            List <Book> listBooks = bookDao.selectAllBooks();
            request.setAttribute("listBooks", listBooks);
            router = "/home.jsp";
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(router);
        dispatcher.forward(request, response);
    }

    private void search(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String router = "/login.jsp";
        if(request.getSession().getAttribute("userLogined")!=null){
            List <Book> listBooks = bookDao.selectBooksByName((String)request.getParameter("search"));
            request.setAttribute("listBooks", listBooks);
            router = "/home.jsp";
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(router);
        dispatcher.forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String phoneNum = request.getParameter("phoneNum");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        User newUser = new User(name, phoneNum, address);
        if(accountDao.isExisted(username)==null){
            userDAO.register(newUser);
            int userID = userDAO.getNewID();
            accountDao.insertAccount(userID, new Account(username, password));
            request.getSession().setAttribute("userLogined", userDAO.getUserByUserID(userID));
            response.sendRedirect(request.getContextPath()+"/app/");
        }else{
            request.setAttribute("user", newUser);
            request.setAttribute("account", new Account(username, password));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
        }
    }
}

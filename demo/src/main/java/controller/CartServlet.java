package controller;

import dao.BookDao;
import dao.OrderDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Book;
import model.Order;
import model.User;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns ={"/cart/*", "/cart"})
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BookDao bookDao;
    private OrderDao orderDao;

    public void init() {
        bookDao = new BookDao();
        orderDao = new OrderDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        System.out.println("Path Info: "+request.getPathInfo());
        try {
            switch (action) {
                case "/add":
                    addBook(request, response);
                    break;
                case "/save":
                    save(request, response);
                    break;
                case "/confirm":
                    confirm(request, response);
                    break;
                case "/remove":
                    remove(request, response);
                    break;
                case "/viewAll":
                    showCart(request, response);
                    break;
                default:
                    showCart(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String router = "/login.jsp";
        User loginedUser = (User)request.getSession().getAttribute("userLogined");
        if(loginedUser!=null){
            Order unconfirmedOrder = orderDao.findUnconfirmOrderByUserID(loginedUser);
            while(unconfirmedOrder==null){
                Order newOrder = new Order();
                newOrder.setUser(loginedUser);
                orderDao.createOrder(newOrder);
                unconfirmedOrder = orderDao.findUnconfirmOrderByUserID(loginedUser);
            }
            request.setAttribute("orderID", unconfirmedOrder.getId());
            request.setAttribute("cart", bookDao.findBooksFromOrder(unconfirmedOrder.getId()));
            router = "/cart.jsp";
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(router);
        dispatcher.forward(request, response);
    }

    private void confirm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String router = "/login.jsp";
        User loginedUser = (User)request.getSession().getAttribute("userLogined");
        if(loginedUser!=null){
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            orderDao.confirmOrder(bookDao.findBooksFromOrder(orderID), orderID);
            bookDao.updateListBook(bookDao.findBooksFromOrder(orderID));
            router = "/cart.jsp";
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(router);
        dispatcher.forward(request, response);
    }

    private void remove(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        User loginedUser = (User)request.getSession().getAttribute("userLogined");
        if(loginedUser!=null){
            String[] query = request.getQueryString().split(";");
            int orderID = Integer.parseInt(query[0].substring(9));;
            int bookID = Integer.parseInt(query[1].substring(8));
            orderDao.deleteOrderBook(orderID, bookID);
        }
        response.sendRedirect(request.getContextPath()+"/cart/");
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        User loginedUser = (User)request.getSession().getAttribute("userLogined");
        if(loginedUser!=null){
            String[] query = request.getQueryString().split(";");
            int orderID = Integer.parseInt(query[0].substring(9));;
            int bookID = Integer.parseInt(query[1].substring(8));
            int quantity = Integer.parseInt(query[2].substring(9));
            orderDao.changeOrder(orderID, bookID, quantity);
        }
        response.sendRedirect(request.getContextPath()+"/cart/");
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String queryString = request.getQueryString();
        int bookID = Integer.parseInt(queryString.substring(3));
        String quantity = request.getParameter("quantity"+Integer.toString(bookID));
        User loginedUser = (User)request.getSession().getAttribute("userLogined");
        Order unconfirmedOrder = orderDao.findUnconfirmOrderByUserID(loginedUser);
        while(unconfirmedOrder==null){
            Order newOrder = new Order();
            newOrder.setUser(loginedUser);
            orderDao.createOrder(newOrder);
            unconfirmedOrder = orderDao.findUnconfirmOrderByUserID(loginedUser);
        }
        orderDao.addChangeToOrder(unconfirmedOrder, bookDao.selectBook(bookID), Integer.parseInt(quantity));
        response.sendRedirect(request.getContextPath()+"/app/");
    }
}

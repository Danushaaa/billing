package com.mycompany.backend.resources;

import DAO.OrderDAO;
import Model.Order;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("orders")
public class OrderResource {
    private final OrderDAO orderDAO = new OrderDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addOrder(Order order) {
        try {
            boolean success = orderDAO.addOrder(order);
            if (success) {
                return Response.status(Response.Status.CREATED).entity(order).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add order").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrders() {
        try {
            List<Order> orders = orderDAO.getAllOrders();
            return Response.ok(orders).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }
}
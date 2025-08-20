/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.backend.resources;


import DAO.CustomerDAO;
import Model.Customer;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;



@Path("customers")
public class CustomerResource {
    private final CustomerDAO customerDAO = new CustomerDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(Customer customer) {
        try {
            boolean success = customerDAO.addCustomer(customer);
            if (success) {
                return Response.status(Response.Status.CREATED).entity("Customer added successfully").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add customer").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("accountNumber") String accountNumber) {
        try {
            Customer customer = customerDAO.getCustomer(accountNumber);
            if (customer != null) {
                return Response.ok(customer).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            return Response.ok(customers).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("{accountNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("accountNumber") String accountNumber, Customer customer) {
        try {
            customer.setAccountNumber(accountNumber); // Ensure account number matches
            boolean success = customerDAO.updateCustomer(customer);
            if (success) {
                return Response.ok("Customer updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found or update failed").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{accountNumber}")
    public Response deleteCustomer(@PathParam("accountNumber") String accountNumber) {
        try {
            boolean success = customerDAO.deleteCustomer(accountNumber);
            if (success) {
                return Response.ok("Customer deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found or delete failed").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }
}
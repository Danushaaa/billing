/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.backend.resources;

import DAO.CashierDAO;
import Model.Cashier;
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

@Path("cashiers")
public class CashierResource {
    private final CashierDAO cashierDAO = new CashierDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCashier(Cashier cashier) {
        try {
            boolean success = cashierDAO.addCashier(cashier);
            if (success) {
                return Response.status(Response.Status.CREATED).entity("Cashier added successfully").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add cashier").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("{cashierId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCashier(@PathParam("cashierId") String cashierId) {
        try {
            Cashier cashier = cashierDAO.getCashier(cashierId);
            if (cashier != null) {
                return Response.ok(cashier).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Cashier not found").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCashiers() {
        try {
            List<Cashier> cashiers = cashierDAO.getAllCashiers();
            return Response.ok(cashiers).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("{cashierId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCashier(@PathParam("cashierId") String cashierId, Cashier cashier) {
        try {
            cashier.setCashierId(cashierId); // Ensure cashier ID matches
            boolean success = cashierDAO.updateCashier(cashier);
            if (success) {
                return Response.ok("Cashier updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Cashier not found or update failed").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{cashierId}")
    public Response deleteCashier(@PathParam("cashierId") String cashierId) {
        try {
            boolean success = cashierDAO.deleteCashier(cashierId);
            if (success) {
                return Response.ok("Cashier deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Cashier not found or delete failed").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }
}
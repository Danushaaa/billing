package com.mycompany.backend.resources;

import DAO.ItemDAO;
import Model.Item;
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

@Path("items")
public class ItemResource {
    private final ItemDAO itemDAO = new ItemDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(Item item) {
        try {
            boolean success = itemDAO.addItem(item);
            if (success) {
                return Response.status(Response.Status.CREATED).entity("Item added successfully").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to add item").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("{itemCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("itemCode") String itemCode) {
        try {
            Item item = itemDAO.getItem(itemCode);
            if (item != null) {
                return Response.ok(item).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItems() {
        try {
            List<Item> items = itemDAO.getAllItems();
            return Response.ok(items).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("{itemCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("itemCode") String itemCode, Item item) {
        try {
            item.setItemCode(itemCode); // Ensure item code matches
            boolean success = itemDAO.updateItem(item);
            if (success) {
                return Response.ok("Item updated successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found or update failed").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{itemCode}")
    public Response deleteItem(@PathParam("itemCode") String itemCode) {
        try {
            boolean success = itemDAO.deleteItem(itemCode);
            if (success) {
                return Response.ok("Item deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found or delete failed").build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }
}
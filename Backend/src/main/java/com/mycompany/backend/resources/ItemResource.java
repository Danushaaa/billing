package com.mycompany.backend.resources;

import DAO.ItemDAO;
import Model.Item;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("items")
public class ItemResource {
    private final ItemDAO itemDAO = new ItemDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(Item item) {
        try {
            if (item.getItemCode() == null || item.getItemName() == null || item.getUnitPrice() < 0 || item.getQuantityInStock() < 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid item data: itemCode, itemName, unitPrice, and quantityInStock must be valid").build();
            }
            boolean success = itemDAO.addItem(item);
            if (success) {
                return Response.status(Response.Status.CREATED).entity(item).build();
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
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found: " + itemCode).build();
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("itemCode") String itemCode, Item item) {
        try {
            if (!itemCode.equals(item.getItemCode())) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Item code in path and body must match").build();
            }
            if (item.getItemName() == null || item.getUnitPrice() < 0 || item.getQuantityInStock() < 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid item data: itemName, unitPrice, and quantityInStock must be valid").build();
            }
            boolean success = itemDAO.updateItem(item);
            if (success) {
                return Response.ok(item).build(); // Return updated item
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found or update failed: " + itemCode).build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{itemCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("itemCode") String itemCode) {
        try {
            boolean success = itemDAO.deleteItem(itemCode);
            if (success) {
                return Response.ok("Item deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Item not found or delete failed: " + itemCode).build();
            }
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage()).build();
        }
    }
}
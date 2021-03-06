package service;

import data.DataHandler;
import model.AutoTeile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.util.Map;
import java.util.UUID;

@Path("teil")
public class AutoTeileService {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTeile() {
        Map<String, AutoTeile> autoTeileMap = data.DataHandler.getAutoTeileMap();

        Response response = Response
                .status(200)
                .entity(autoTeileMap)
                .build();
        return response;
    }

    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readTeile(
            @QueryParam("teil") String bezeichnung
    ) {
        AutoTeile teil = null;
        int httpStatus;

        try {
            UUID.fromString(bezeichnung);
            teil = data.DataHandler.readBook(bezeichnung);
            if (teil.getHerstellerName() == null) {
                httpStatus = 484;
            } else {
                httpStatus = 200;
            }
        } catch (IllegalArgumentException argEx) {
            httpStatus = 400;
        }

        Response response = Response
                .status(200)
                .entity(teil)
                .build();
        return response;
    }
}

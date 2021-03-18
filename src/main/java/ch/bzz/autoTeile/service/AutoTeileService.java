package ch.bzz.autoTeile.service;

import ch.bzz.autoTeile.data.DataHandler;
import ch.bzz.autoTeile.model.AutoTeile;
import ch.bzz.autoTeile.model.Lager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Path("teil")
public class AutoTeileService {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTeile() {
        List<Lager> lagerList = ch.bzz.autoTeile.data.DataHandler.getLagerList();

        Response response = Response
                .status(200)
                .entity(lagerList)
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
            teil = ch.bzz.autoTeile.data.DataHandler.readTeil(bezeichnung);
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

    /**
     * creates a new AutoTeil
     * @param bezeichnung the name of the Autoteil
     * @param hersteller the hersteller
     * @param price the price
     * @param herstellerUUID the unique key of the hersteller
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAutoteil(
            @FormParam("bezeichnung") String bezeichnung,
            @FormParam("hersteller") String hersteller,
            @FormParam("herstellerUUID") String herstellerUUID,
            @FormParam("price") BigDecimal price
    ) {
        int httpStatus = 200;
        AutoTeile teil = new AutoTeile();
        teil.setTeilUUID(UUID.randomUUID().toString());
        setValues(
                teil,
                bezeichnung,
                hersteller,
                herstellerUUID,
                price
        );

        DataHandler.insertTeil(teil);

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }
}

package ch.bzz.autoTeile.service;

import ch.bzz.autoTeile.data.DataHandler;
import ch.bzz.autoTeile.model.AutoTeile;
import ch.bzz.autoTeile.model.Lager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Path("lager")
public class LagerListeService {
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lagerList() {
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
    public Response readList(
            @QueryParam("list") String bezeichnung
    ) {
        Lager lager = null;
        int httpStatus;

        try {
            UUID.fromString(bezeichnung);
            lager = ch.bzz.autoTeile.data.DataHandler.readList(lager);
            if (lager.getLagerList() == null) {
                httpStatus = 484;
            } else {
                httpStatus = 200;
            }
        } catch (IllegalArgumentException argEx) {
            httpStatus = 400;
        }

        Response response = Response
                .status(200)
                .entity(lager)
                .build();
        return response;
    }

    /**
     * creates a new entry in lagerlist
     * @param autoteil the Autoteil
     * @param eintragsdatum the date of entry
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createLagerList(
            @FormParam("autoteil") String autoteil,
            @FormParam("eintragsdatum") Date eintragsdatum,
            @FormParam("lagerUUID") String lagerUUID
    ) {
        int httpStatus = 200;
        Lager lager = new Lager();
        lager.setLagerUUID(UUID.randomUUID().toString());
        setValues(
                autoteil,
                eintragsdatum,
                lagerUUID
        );

        DataHandler.insertLager(lager);

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }
}

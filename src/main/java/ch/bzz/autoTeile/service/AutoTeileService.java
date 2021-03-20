package ch.bzz.autoTeile.service;

import ch.bzz.autoTeile.data.DataHandler;
import ch.bzz.autoTeile.model.AutoTeile;
import ch.bzz.autoTeile.model.Hersteller;
import ch.bzz.autoTeile.model.Lager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * provides services for the autoteile
 * <p>
 * AutoTeile
 *
 * @author Jason A. Caviezel
 */
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
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAutoteil(
            @FormParam("bezeichnung") String bezeichnung,
            @FormParam("hersteller") Hersteller hersteller,
            @FormParam("teilUUID") String teilUUID,
            @FormParam("price") BigDecimal price
    ) {
        int httpStatus = 200;
        AutoTeile teil = new AutoTeile();
        teil.setTeilUUID(UUID.randomUUID().toString());
        teil.setBezeichnung(bezeichnung);
        teil.setPreis(price);
        teil.setHersteller(hersteller);

        DataHandler.insertTeil(teil);

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

    /**
     * updates an existing Autoteil
     * @param bezeichnung the bezeichnung
     * @param hersteller the hersteller
     * @param herstellerUUID the herstellerUUID
     * @param teilUUID the teilUUID
     * @param price the price
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateTeil(
            @FormParam("bezeichnung") String bezeichnung,
            @FormParam("hersteller") String hersteller,
            @FormParam("herstellerUUID") String herstellerUUID,
            @FormParam("teilUUID") String teilUUID,
            @FormParam("price") BigDecimal price
    ) {
        int httpStatus = 200;
        AutoTeile teil = new AutoTeile();
        try {
            UUID.fromString(teilUUID);
            teil.setHerstellerUUID(herstellerUUID);
            teil.setBezeichnung(bezeichnung);
            teil.setTeilUUID(teilUUID);
            teil.setPreis(price);
                if (DataHandler.updateTeil(teil)) {
                    httpStatus = 200;
                } else {
                    httpStatus = 404;
                }
            } catch (IllegalArgumentException argEx) {
                httpStatus = 400;
            }
            Response response = Response
                    .status(httpStatus)
                    .entity("")
                    .build();
            return response;
        }

    /**
     * deletes an existing Autoteil identified by its uuid
     * @param teilUUID  the unique key for the Teil
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAutoteil(
            @QueryParam("uuid") String teilUUID
    ) {
        int httpStatus;
        try {
            UUID.fromString(teilUUID);

            if (DataHandler.deleteAutoteil(teilUUID)) {
                httpStatus = 200;

            } else {
                httpStatus = 404;
            }
        } catch (IllegalArgumentException argEx) {
            httpStatus = 400;
        }

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }
}

package ch.bzz.autoTeile.service;

import ch.bzz.autoTeile.data.DataHandler;
import ch.bzz.autoTeile.model.AutoTeile;
import ch.bzz.autoTeile.model.Hersteller;
import ch.bzz.autoTeile.model.Lager;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
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
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
            @QueryParam("uuid") String teilUUID
    ) {
        AutoTeile teil = null;
        int httpStatus;

        try {
            UUID teilkey = UUID.fromString(teilUUID);
            teil = ch.bzz.autoTeile.data.DataHandler.readTeil(teilUUID);
            if (teil.getBezeichnung() == null) {
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
     * @param teil a valid Autoteile Object
     * @param herstellerUUID the unique key of the hersteller
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAutoteil(
            @Valid @BeanParam AutoTeile teil,
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
            @FormParam("herstellerUUID") String herstellerUUID
    ) {
        int httpStatus = 200;
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
     * @param herstellerUUID a valid UUID
     * @param teilUUID a valid UUID
     * @param teil a valid Autoteile Object
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateTeil(
            @Valid @BeanParam AutoTeile teil,
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
            @FormParam("herstellerUUID") String herstellerUUID,
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
            @FormParam("teilUUID") String teilUUID
    ) {
        int httpStatus = 200;
        try {
            UUID.fromString(teilUUID);
            teil.setHerstellerUUID(herstellerUUID);
            teil.setBezeichnung(teil.getBezeichnung());
            teil.setTeilUUID(teilUUID);
            teil.setPreis(teil.getPreis());
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
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
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

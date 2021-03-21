package ch.bzz.autoTeile.service;

import ch.bzz.autoTeile.data.DataHandler;
import ch.bzz.autoTeile.model.AutoTeile;
import ch.bzz.autoTeile.model.Hersteller;
import ch.bzz.autoTeile.model.Lager;

import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * provides services for the Lagerliste
 * <p>
 * AutoTeile
 *
 * @author Jason A. Caviezel
 */
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
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
            @QueryParam("uuid") String lagerUUID
    ) {
        Lager lager = null;
        int httpStatus;

        try {
            UUID.fromString(lagerUUID);
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
     * @param lagerUUID the Autoteil
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createLagerList(
            @FormParam("autoteil") String autoteil,
            @FormParam("eintragsdatum") Date eintragsdatum,
            @FormParam("lagerUUID") String lagerUUID,
            @FormParam("teilUUID") String teilUUID
    ) {
        int httpStatus = 200;
        Lager lager = new Lager();
        lager.setLagerUUID(UUID.randomUUID().toString());
        setValues(
                lager,
                autoteil,
                eintragsdatum,
                lagerUUID,
                teilUUID
        );

        DataHandler.insertLager(lager);

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

    /**
     * updates an existing Lager
     * @param autoteil the Autoteil
     * @param eintragsdatum the date of entry
     * @param lagerUUID the Autoteil
     * @param teilUUID the Autoteil
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateLager(
            @FormParam("autoteil") String autoteil,
            @FormParam("eintragsdatum") Date eintragsdatum,
            @FormParam("lagerUUID") String lagerUUID,
            @FormParam("teilUUID") String teilUUID
    ) {
        int httpStatus = 200;
        Lager lager;
        try {
            UUID.fromString(lagerUUID);
            lager = DataHandler.readLager(lagerUUID);
            if (lager.getEintragsdatum() != null) {
                httpStatus = 200;
                setValues(
                        lager,
                        autoteil,
                        eintragsdatum,
                        lagerUUID,
                        teilUUID
                );
                DataHandler.updateLager();
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
     * deletes an existing Lagereintrag identified by its uuid
     * @param lagerUUID  the unique key for the Lager
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteLager(
            @QueryParam("uuid") String lagerUUID
    ) {
        int httpStatus;
        try {
            UUID.fromString(lagerUUID);

            if (DataHandler.deleteLager(lagerUUID)) {
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
     * sets the attribute values of the book object
     * @param lager  the lager object
     * @param autoteil the autoteil in the lager
     * @param eintragsdatum the entry date
     * @param lagerUUID the unique key of the lager
     * @param teilUUID the unique key of the lager
     */
    private void setValues(
            Lager lager,
            String autoteil,
            Date eintragsdatum,
            String lagerUUID,
            String teilUUID) {
        lager.setEintragsdatum(eintragsdatum);

        AutoTeile teile = DataHandler.getAutoTeileMap().get(teilUUID);
        lager.setEintragsdatum(eintragsdatum);
    }
}

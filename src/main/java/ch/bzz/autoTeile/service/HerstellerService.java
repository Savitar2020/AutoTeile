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
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * provides services for the hersteller
 * <p>
 * AutoTeile
 *
 * @author Jason A. Caviezel
 */
@Path("hersteller")
public class HerstellerService {

    /**
     * produces a map of all hersteller
     *
     * @return Response
     */
        @GET
        @Path("list")
        @Produces(MediaType.APPLICATION_JSON)
        public Response listHersteller() {
            Map<String, Hersteller> herstellerMap = DataHandler.getHerstellerMap();

            Response response = Response
                    .status(200)
                    .entity(herstellerMap)
                    .build();
            return response;
        }

    /**
     * reads a single hersteller identified by the uuid
     *
     * @param herstellerUUID the UUID in the URL
     * @return Response
     */
        @GET
        @Path("read")
        @Produces(MediaType.APPLICATION_JSON)
        public Response readHersteller(
                @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
                @QueryParam("uuid") String herstellerUUID
        ) {
            Hersteller hersteller = null;
            int httpStatus;

            try {
                UUID.fromString(herstellerUUID);
                hersteller = ch.bzz.autoTeile.data.DataHandler.readTeil(herstellerUUID);
                if (hersteller.getHerstellerName() == null) {
                    httpStatus = 484;
                } else {
                    httpStatus = 200;
                }
            } catch (IllegalArgumentException argEx) {
                httpStatus = 400;
            }

            Response response = Response
                    .status(200)
                    .entity(hersteller)
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
                @Valid @BeanParam Hersteller hersteller,
                @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
                @FormParam("hersteller") Hersteller hersteller,
                @FormParam("teilUUID") String teilUUID
        ) {
            int httpStatus = 200;
            AutoTeile teil = new AutoTeile();
            teil.setTeilUUID(UUID.randomUUID().toString());
            teil.setHerstellerName(hersteller.getHerstellerName());
            teil.setTelephonnummer(teil.getTelephonnummer());
            teil.setHersteller(hersteller);

            DataHandler.insertTeil(teil);

            Response response = Response
                    .status(httpStatus)
                    .entity("")
                    .build();
            return response;
        }
}

    /**
     * updates an existing Hersteller
     * @param autoteil the Autoteil
     * @param herstellername the herstellername
     * @param telephonnummer the telephonnummer
     * @param herstellerUUID the herstellerUUID
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateHersteller(
            @Valid @BeanParam Hersteller hersteller,
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
            @FormParam("herstellerUUID") String herstellerUUID
    ) {
        int httpStatus = 200;
        try {
            UUID.fromString(herstellerUUID);
            hersteller.setHerstellerUUID(herstellerUUID);
            hersteller.setHerstellerName(hersteller.getHerstellerName());
            hersteller.setTelephonnummer(hersteller.getTelephonnummer());
                if (DataHandler.updateHersteller(hersteller)) {
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
     * deletes an existing hersteller identified by its uuid
     * @param herstellerUUID  the unique key for the hersteller
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteHersteller(
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}-")
            @QueryParam("uuid") String herstellerUUID
    ) {
        int httpStatus;
        try {
            UUID.fromString(herstellerUUID);

            if (DataHandler.deleteHersteller(herstellerUUID) == -1) {
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

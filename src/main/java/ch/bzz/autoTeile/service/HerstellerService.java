package ch.bzz.autoTeile.service;

import ch.bzz.autoTeile.data.DataHandler;
import ch.bzz.autoTeile.model.AutoTeile;
import ch.bzz.autoTeile.model.Hersteller;
import ch.bzz.autoTeile.model.Lager;

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
                @QueryParam("hersteller") String herstellerUUID
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
            @FormParam("autoteil") String autoteil,
            @FormParam("herstellername") String herstellername,
            @FormParam("telephonnummer") int telephonnummer,
            @FormParam("herstellerUUID") String herstellerUUID
    ) {
        int httpStatus = 200;
        Hersteller hersteller = new Hersteller();
        try {
            UUID.fromString(herstellerUUID);
            hersteller.setHerstellerUUID(herstellerUUID);
            hersteller.setHerstellerName(herstellername);
            hersteller.setTelephonnummer(telephonnummer);
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

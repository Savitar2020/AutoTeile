/**
 * view-controller for lageredit.html
 *
 *
 * @author  Jason A. Caviezel
 */

/**
 * register listeners and load the teil data
 */
$(document).ready(function () {
    loadHersteller();
    loadTeil();

    /**
     * listener for submitting the form
     */
    $("#TeileditForm").submit(saveTeil);

    /**
     * listener for button [abbrechen], redirects to Lagerlist
     */
    $("#cancel").click(function () {
        window.location.href = "./lager.html";
    });


});

/**
 *  loads the data of this book
 *
 */
function loadTeil() {
    let teilUUID = $.urlParam("uuid");
    if (teilUUID) {
        $
            .ajax({
                url: "./resource/teil/read?uuid=" + teilUUID,
                dataType: "json",
                type: "GET"
            })
            .done(showTeil)
            .fail(function (xhr, status, errorThrown) {
                if (xhr.status == 403) {
                    window.location.href = "./login.html";
                } else if (xhr.status == 404) {
                    $("#message").text("Kein Teil gefunden");
                } else {
                    window.location.href = "./lager.html";
                }
            })
    }

}

/**
 * shows the data of this Teil
 * @param  teil  the teil data to be shown
 */
function showTeil(teil) {
    $("#teilUUID").val(teil.teilUUID);
    $("#bezeichnung").val(teil.bezeichnung);
    $("#hersteller").val(teil.hersteller.herstellerUUID);
    $("#price").val(teil.price);
}

/**
 * sends the teil data to the webservice
 * @param form the form being submitted
 */
function saveTeil(form) {
    form.preventDefault();

    let url = "./resource/teil/";
    let type;

    let teilUUID = $("#teilUUID").val();
    if (teilUUID) {
        type= "PUT";
        url += "update"
    } else {
        type = "POST";
        url += "create";
    }

    $
        .ajax({
            url: url,
            dataType: "text",
            type: type,
            data: $("#teileditForm").serialize()
        })
        .done(function (jsonData) {
            window.location.href = "./lager.html"
        })
        .fail(function (xhr, status, errorThrown) {
            if (xhr.status == 404) {
                $("#message").text("Dieses Teil existiert nicht");
            } else {
                $("#message").text("Fehler beim Speichern des Teils");
            }
        })
}

function loadHersteller() {
    $
        .ajax({
            url: "./resource/hersteller/list",
            dataType: "json",
            type: "GET"
        })
        .done(showHersteller)
        .fail(function (xhr, status, errorThrown) {
            if (xhr.status == 404) {
                $("#message").text("Kein Hersteller gefunden");
            } else {
                window.location.href = "./lager.html";
            }
        })
}

function showHersteller(hersteller) {

    $.each(hersteller, function (uuid, hersteller) {
        $('#hersteller').append($('<option>', {
            value: hersteller.herstellerUUID,
            text: hersteller.hersteller
        }));
    });
}

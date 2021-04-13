/**
 * view-controller for lager.html
 *
 * @author  Jason A. Caviezel
 */

/**
 * register listeners and load all teile
 */
$(document).ready(function () {
    loadTeile();

    /**
     * listener for buttons within shelfForm
     */
    $("#shelfForm").on("click", "button", function () {
        if (confirm("Wollen Sie dieses Teil wirklich löschen?")) {
            deleteTeil(this.value);
        }
    });



});

function loadTeile() {
    $
        .ajax({
            url: "./resource/teil/list",
            dataType: "json",
            type: "GET"
        })
        .done(showTeil)
        .fail(function (xhr, status, errorThrown) {
            if (xhr.status == 403) {
                window.location.href = "./login.html";
            } else if (xhr.status == 404) {
                $("#message").text("keine Teile vorhanden");
            }else {
                $("#message").text("Fehler beim auslesen der Teile");
            }
        })

}

/**
 * shows all teile as a table
 *
 * @param teilData all teile as an array
 */
function showTeil(teilData) {

    let table = document.getElementById("lager");
    clearTable(table);

    $.each(teilData, function (uuid, teil) {
        if (teil.title) {
            let row = table.insertRow(-1);
            let cell = row.insertCell(-1);
            cell.innerHTML = teil.title;

            cell = row.insertCell(-1);
            cell.innerHTML = teil.author;

            cell = row.insertCell(-1);
            cell.innerHTML = teil.publisher.publisher;

            cell = row.insertCell(-1);
            cell.innerHTML = teil.price;

            cell = row.insertCell(-1);
            cell.innerHTML = teil.isbn;

            cell = row.insertCell(-1);
            cell.innerHTML = "<a href='./lageredit.html?uuid=" + uuid + "'>Bearbeiten</a>";

            cell = row.insertCell(-1);
            cell.innerHTML = "<button type='button' id='delete_" + uuid + "' value='" + uuid + "'>Löschen</button>";


        }
    });
}

function clearTable(table) {
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }
}

/**
 * send delete request for a teil
 * @param teilUUID
 */
function deleteTeil(teilUUID) {
    $
        .ajax({
            url: "./resource/teil/delete?uuid=" + teilUUID,
            dataType: "text",
            type: "DELETE",
        })
        .done(function (data) {
            loadTeile();
            $("#message").text("Teil gelöscht");

        })
        .fail(function (xhr, status, errorThrown) {
            $("#message").text("Fehler beim Löschen des Teils");
        })
}

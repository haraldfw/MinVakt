/**
 * Created by alan on 18/01/2017.
 */

$(document).ready(() => {
    let fromDate = null;
    let toDate = null;


    $("#go").click(event => {
        let json = {
            date_from: $("#fraDato").serializeObject().date,
            date_to: $("#tilDato").serializeObject().date,
            // cause: $("#absenceCause").text(),
            // elaboration: $("#elaboration").text()
        };

        let username = $("#username").text();

        console.log(json);

        $.ajax({
            type: "post",
            url: "/api/user/" + username + "/unavailable",
            data: JSON.stringify(json),
            contentType: "application/json; charset=utf-8"
        }).done(console.log).fail(err => {
            console.error("Failed to submit");
            console.error(err);
        });
    });
});
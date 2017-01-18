/**
 * Created by alan on 17/01/2017.
 */

$(document).ready(() => {
    $("#form").form(json => {
        $.ajax({
            type: "post",
            url: "/api/login",
            data: JSON.stringify(json),
            contentType: "application/json; charset=utf-8"
        })
            .done(data => {
                window.location = "/welcome";
            }).fail(err => {
            console.error("Failed to login!");
            console.error(err);
        });
    });
});
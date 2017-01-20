/**
 * Created by alan on 20/01/2017.
 */

$(document).ready(() => {
    $("#form").form(json => {
        $.ajax({
            type: "post",
            url: "/api/password/change",
            data: JSON.stringify(json),
            contentType: "application/json; charset=utf-8"
        }).done(data => {
        }).fail(err => {
            console.error("Failed to change!");
            console.error(err);
        });
    });
});
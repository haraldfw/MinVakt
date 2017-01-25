/**
 * Created by gards on 24-Jan-17.
 */
$(document).ready(function () {
    $("#generate_transfer_request").click(function () {
        var shift_id = $("#shift_input").val();
        var user_id = $("#user_input").val();
        var url = "/api/notifications/generate_transfer_request_notification?shift_id=" + shift_id + "&user_id=" + user_id;

        $.post(url, function () {

        }).done(function () {
            alert("Gikk bra");
        }).fail(function () {
            alert("Gikk dårlig");
        })
    });

    $("#generate_remove_request").click(function () {
        var shift_id = $("#shift_input").val();
        var url = "/api/notifications/generate_release_from_shift_request_notification?shift_id="+shift_id;

        $.post(url, function () {

        }).done(function () {
            alert("Gikk bra");
        }).fail(function () {
            alert("Gikk dårlig");
        })
    });
});
/**
 * Created by alan on 26/01/2017.
 */

function submitted(data) {
    username.text(data.user_model.username);
    message.slideDown(200);
    input.val('');
}

function failed(err) {
    const body = $.parseJSON(err.responseText);
    error.text(body.error_msg);
    error.slideDown(200);
}

let input, message, error, username;

$(document).ready(() => {
    error = $('#error');
    message = $('#mld');
    input = $("input");
    username = $("#show-username");

    input.focus(() => {
        message.slideUp(200);
        error.slideUp(200);
    });
});
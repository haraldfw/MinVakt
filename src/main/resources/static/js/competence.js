/**
 * Created by alan on 26/01/2017.
 */

function submitted(data) {
    comp_name.text(comp.val());
    message.slideDown(200);
}

function failed(err) {
    error.slideDown(200);
}

let comp_name, comp, message, error;

$(document).ready(() => {
    comp_name = $('#comp-name');
    error = $('#error');
    message = $('#mld');
    comp = $('#comp');

    let inp = $("input");
    inp.focus(() => {
        message.slideUp(200);
        error.slideUp(200);
    });
});
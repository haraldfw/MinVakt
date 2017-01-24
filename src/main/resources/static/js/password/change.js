/**
 * Created by alan on 20/01/2017.
 */

let pwd;
let pwd2;
let meter;

function submitted(data) {
    window.location = '/user/profile';
}

$(document).ready(() => {
    pwd = $("#pwd");
    pwd2 = $("#pwd2");
    meter = $("#password-strength-meter");

    pwd.on("input", () => {
        vaildate();
    });

    pwd2.on("input", () => {
        vaildate();
    });
});


function vaildate() {
    let password = pwd.val();
    let result = zxcvbn(password);

    meter.val(result.score);

    if (password !== pwd2.val()) {
        pwd[0].setCustomValidity("Passordene må stemme overens.");
    } else {
        pwd[0].setCustomValidity("");
    }

    return true;
}
/**
 * Created by alan on 20/01/2017.
 */

let pwd;
let pwd2;
let meter;

function passwords_equal(json) {
    if (json.new_password !== pwd2.val()) {
        console.log("Passordene må stemme overens.");
        pwd[0].setCustomValidity("Passordene må stemme overens."); //FIXME
        return false;
    }

    return true;
}

function submitted(data) {
    window.location = '/user/profile';
}

function failed(err) {
    console.error("Failed to change!");
    console.error(err);
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
}
/**
 * Created by alan on 20/01/2017.
 */

let pwd;
let pwd2;
let meter;

$(document).ready(() => {
    pwd = $("#pwd");
    pwd2 = $("#pwd2");
    meter = $("#password-strength-meter");

    $("#form").form(json => {
        if (json.new_password !== pwd2.val()) {
            console.log("Passordene må stemme overens.");
            pwd[0].setCustomValidity("Passordene må stemme overens."); //FIXME
        } else {
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
        }
    });

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
/**
 * Created by Nicole on 20.01.2017.
 */

let rpt_pwd, new_pwd, meter, check_mark;

$(document).ready(() => {
    $("#rpt-pwd, #new-pwd").on("focus", function () {
        $("#incorrect-pwd").slideUp(200);
    });

    new_pwd = $("#new-pwd");
    rpt_pwd = $("#rpt-pwd");
    meter = $("#password-strength-meter");
    check_mark = $("#check-mark");

    new_pwd.on("input", () => {
        vaildate();
    });

    rpt_pwd.on("input", () => {
        vaildate();
    });
});

function failed() {
    $("#incorrect-pwd").slideDown(200);
}

function success() {
    window.location = "/login";
}

function vaildate() {
    let password = new_pwd.val();
    let result = zxcvbn(password);

    meter.val(result.score);

    if (password.length > 7 && password === rpt_pwd.val()) {
        check_mark.fadeIn(200);
        rpt_pwd[0].setCustomValidity("");
    } else {
        check_mark.fadeOut(200);
        rpt_pwd[0].setCustomValidity("Passordene må stemme overens og være lengere enn 7 bokstaver.");
    }
}


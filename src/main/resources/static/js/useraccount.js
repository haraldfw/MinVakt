/**
 * Created by alan on 17/01/2017.
 */

let rpt_pwd, new_pwd, meter;

$(document).ready(() => {
    $(".btn-edit:not(#edit-pwd)").click(function () {
        $("#new-pwd-box").slideUp(200);
        disableEnabledInputs();
        let targetInput = $(this).parent().siblings(".input-cell").find("input");

        targetInput.removeAttr("disabled");
        targetInput.select();
    });

    let input_box = $(".sbmts");

    input_box.keypress(function (event) {
        if (event.which == 13) {
            $(this).attr("disabled", true);
        }
    });

    input_box.not(".pwd-input").blur(function () {
        $(this).attr("disabled", true);
    });

    $("#edit-pwd").click(function () {
        $("#new-pwd-box input").removeAttr("disabled");
        $("#fake-pwd-input").slideUp(100);
        $("#new-pwd-box").slideDown(200);
    });

    $(".pwd-input").on("focus", function () {
        $("#incorrect-pwd").slideUp(200);
    });

    new_pwd = $("#new-pwd");
    rpt_pwd = $("#rpt-pwd");
    meter = $("#password-strength-meter");
    // check_mark = $("#check-mark");

    new_pwd.on("input", () => {
        vaildate();
    });

    rpt_pwd.on("input", () => {
        vaildate();
    });

    $(document).on("change", "#imgUpload", function() {
        $("#upload-file-info").html($(this).val()).show();
    });

});

function disableEnabledInputs() {
    let enabledInputs = $(".account-box input:not(:disabled)");
    enabledInputs.attr("disabled", true);
}

function success() {
    console.log("Passord ble endret!");
    $("#new-pwd-box").slideUp(200);
    $("#fake-pwd-input").slideDown(100);
}

function failure() {
    $("#incorrect-pwd").slideDown(200);
}

function vaildate() {
    let password = new_pwd.val();
    let result = zxcvbn(password);

    meter.val(result.score);

    if (password.length > 7 && password === rpt_pwd.val()) {
        // check_mark.fadeIn(200);
        rpt_pwd[0].setCustomValidity("");
    } else {
        // check_mark.fadeOut(200);
        rpt_pwd[0].setCustomValidity("Passordene må stemme overens og være lengere enn 7 bokstaver.");
    }
}


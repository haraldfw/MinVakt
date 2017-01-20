/**
 * Created by Nicole on 20.01.2017.
 */

$(document).ready(() => {

    $("#rpt-pwd, #new-pwd").on("input", function () { // input = when a key is pressed
        var newPwd = $("#new-pwd").val();
        var rptPwd = $("#rpt-pwd").val();
        $("#check-mark").hide();
        if (newPwd.length > 0 && newPwd === rptPwd) {
            $("#check-mark").fadeIn(200);
        }
    });

    $("#save-pwd").on("click", function (event) {
        var newPwd = $("#new-pwd").val();
        var rptPwd = $("#rpt-pwd").val();
        if (newPwd !== rptPwd) {
            event.preventDefault();
            $("#incorrect-pwd").slideDown(200);
        }
    });

    $("#rpt-pwd, #new-pwd").on("focus", function () {
        $("#incorrect-pwd").slideUp(200);
    });

});


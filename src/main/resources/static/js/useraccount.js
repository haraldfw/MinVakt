/**
 * Created by alan on 17/01/2017.
 */

$(document).ready(() => {
    $(".glyphicon.glyphicon-edit").click(function () {
        disableEnabledInputs();
        let targetInput = $(this).parent().siblings(".input-cell").find("input");

        targetInput.removeAttr("disabled");
        targetInput.select();
    });

    let input_box = $(".account-box input");

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

    $("#save-pwd-btn").click(function () {
        $("#new-pwd-box").slideUp(200);
        $("#fake-pwd-input").slideDown(100);
    });
});

function disableEnabledInputs() {
    let enabledInputs = $(".account-box input:not(:disabled)");
    enabledInputs.attr("disabled", true);
}
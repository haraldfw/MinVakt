/**
 * Created by Ingunn on 13.01.2017.
 */

$(document).ready(function() {

    $("#testing, #testingg").click(function() {
        if($("#test").css("display") == "none") {
            $("#test").css("display", "inline");
        } else {
            $("#test").css("display", "none");
        }


    });
    $("#testing2, #testingg2").click(function() {
        if($("#test2").css("display") == "none") {
            $("#test2").css("display", "inline");
        } else {
            $("#test2").css("display", "none");
        }

    });

    $(".no-worker").click(function() {
        $("#modalShift").modal("show");
        $("#modalFree").css("display", "inline");
        $("#modalOther").css("display", "none");
        $("#modalOwn").css("display", "none");
    });


    $(".self").click(function() {
        $("#modalShift").modal("show");
        $("#modalOwn").css("display", "inline");
        $("#modalOther").css("display", "none");
        $("#modalFree").css("display", "none");

    });
    $(".worker").click(function() {
        $("#modalShift").modal("show");
        $("#modalOther").css("display", "inline");
        $("#modalOwn").css("display", "none");
        $("#modalFree").css("display", "none");

    });

    $("#shiftChange").click(function() {
        if ($("#shiftSelect").css("display") === "none") {
            $("#shiftSelect").css("display", "inline");
        } else {
            $("#shiftSelect").css("display", "none");
        }
    });

    $(".timedisplay").click(function() {
        $("#infoTime").modal("show");
        var text = $(this).html();
        $("#titleTime").html(text);
    });
});
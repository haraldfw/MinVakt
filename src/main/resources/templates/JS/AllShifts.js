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
        $("#freeShift").modal("show");
    });
    $(".self").click(function() {
        $("#ownShift").modal("show");
    });
    $(".worker").click(function() {
        $("#otherShift").modal("show");
    });

    $("#shiftChange").click(function() {
        if ($("#shiftSelect").css("display") === "none") {
            $("#shiftSelect").css("display", "inline");
        } else {
            $("#shiftSelect").css("display", "none");
        }
    });
});
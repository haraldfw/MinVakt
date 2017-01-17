/**
 * Created by Ingunn on 13.01.2017.
 */

/*
left: calc(11.11% + (11.11%)/3 + ((11.11%)/3)*16);
width: calc(((11.11%)/3)*8 - calc((11.11%)/3) - 1px);
*/


$(document).ready(function() {


    var from = "0730";
    var to = "1400";

    // min/0.6 = prosent

    var pos = "worker";


    function test(from, to) {
        var diff = to-from; //7h
        var lengde = 7;

        if(diff % 100 === 0) {
            diff /= 100;
        } else {
            diff %= 100;

        }
        //alert(diff)

        $("#testtest").css("left", "calc(11.11% + ((11.11%/3)*" + lengde +"))");
        $("#testtest").css("width", "calc((11.11%/3)*" + diff + " - 1px)");
        $("#testtest").addClass("worker");
    }


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

    test(from, to);



});
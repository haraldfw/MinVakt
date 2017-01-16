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

    $(".shift-box").click(function() {
        $("#tester").modal("show");

    });
});
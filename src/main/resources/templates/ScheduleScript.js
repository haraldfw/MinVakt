/**
 * Created by Knut on 12.01.2017.
 */

$(document).ready(function() {


    var selectedShift = -1;


    $(".dayInnhold").click(function() {

    });

    $(".shift").click(function() {



        //alert("heisann");
        $("#modalTest").modal("show");
        //TODO: gjøre ting med iden man får her og hente fra database$(this).attr("id")
        selectedShift = $(this).attr("id");
        //.val()
        if ($(this).hasClass("normal-shift")) {
            $(".modal-title").html("Skift Onsdag 11. jan. 2017");
            $("#shift-time").html("Du har et skift fra 00:00 - 12:00 på Onsdag 11. jan. 2017");
            $("#absenceButton").css("display", "inline-block");
            $("#removeAbsenceButton").css("display", "none");
            //alert("vanlig skift");

        } else if ($(this).hasClass("absence-shift")) {
            //absenceButton
            //alert("fravær");
            $(".modal-title").html("Skift Onsdag 11. jan. 2017");
            $("#shift-time").html("Det er et skift tilgjengelig fra 12:00 - 24:00 på Onsdag 11. jan. 2017");
            $("#absenceButton").css("display", "none");
            $("#removeAbsenceButton").css("display", "inline-block");

        } else if ($(this).hasClass("availible-shift")) {
            //alert("tilgjengelig");
            $(".modal-title").html("Skift Lørdag 14. jan. 2017");
            $("#shift-time").html("Du har satt deg tilgjengelig for dette skiftet, fra 00:00 - 12:00 på Lørdag 14 jan. 2017");
            $("#absenceButton").css("display", "none");
            $("#removeAbsenceButton").css("display", "none");
        }

        //alert(selectedShift);
    });

    $("#absenceButton").click(function() {
        //alert(selectedShift);
        $(".shift#" + selectedShift).removeClass("normal-shift").addClass("absence-shift");
        $("#modalTest").modal("toggle");
    });

    $("#removeAbsenceButton").click(function() {

        $(".shift#" + selectedShift).removeClass("absence-shift").addClass("normal-shift");
        $("#modalTest").modal("toggle");
    });
});
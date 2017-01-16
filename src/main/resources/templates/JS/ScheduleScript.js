/**
 * Created by Knut on 12.01.2017.
 */

$(document).ready(function() {


    var selectedShift = -1;
    var questionAnswer = false;
    var shiftType = -1; //Goes from 0 to 4, 0 = normal-shift, 1 = absence-shift, 2 = availible-shift, 3 = ??, 4 =


    $(".dayInnhold").click(function() {

    });

    $(".shift").click(function() {



        //alert("heisann");
        $("#modalTest").modal("show");
        //TODO: gjøre ting med iden man får her og hente fra database$(this).attr("id")
        selectedShift = $(this).attr("id");

        if ($(this).hasClass("normal-shift")) {
            //Modal for shift
            $("#modal-shift-title").html("Skift Onsdag 11. jan. 2017");
            $("#shift-time").html("Du har et skift fra 00:00 - 12:00 på Onsdag 11. jan. 2017");
            $("#absenceButton").css("display", "inline-block");
            $("#removeAbsenceButton").css("display", "none");
            $("#removeAvailibilityButton").css("display", "none");
            shiftType = 0;
            //alert("vanlig skift");
            $(".panel-group").css("display", "block");

        } else if ($(this).hasClass("absence-shift")) {
            //absenceButton
            //alert("fravær");
            $("#modal-shift-title").html("Skift Onsdag 11. jan. 2017");
            $("#shift-time").html("Det er et skift tilgjengelig fra 12:00 - 24:00 på Onsdag 11. jan. 2017");
            $("#absenceButton").css("display", "none");
            $("#removeAbsenceButton").css("display", "inline-block");
            $("#removeAvailibilityButton").css("display", "none");
            shiftType = 1;
            $(".panel-group").css("display", "block");

        } else if ($(this).hasClass("availible-shift")) {
            //alert("tilgjengelig");
            $("#modal-shift-title").html("Skift Lørdag 14. jan. 2017");
            $("#shift-time").html("Du har satt deg tilgjengelig for dette skiftet, fra 00:00 - 12:00 på Lørdag 14 jan. 2017");
            $("#absenceButton").css("display", "none");
            $("#removeAbsenceButton").css("display", "none");
            $("#removeAvailibilityButton").css("display", "inline-block");
            shiftType = 2;
            $(".panel-group").css("display", "none");
        }

        //alert(selectedShift);
    });

    $("#absenceButton").click(function() {
        //alert(selectedShift);
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").html("Vil du melde om fravær for dette skiftet?");

        if (questionAnswer) {
            //$(".shift#" + selectedShift).removeClass("normal-shift").addClass("absence-shift");
            //$("#modalTest").modal("toggle");
        }
    });

    $("#removeAbsenceButton").click(function() {
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").html("Vil du fjerne fravær for dette skiftet?");

        if (questionAnswer) {
            //alert("asasds");
            //$(".shift#" + selectedShift).removeClass("absence-shift").addClass("normal-shift");
            //$("#modalTest").modal("toggle");
        } else {

        }
    });

    $("#removeAvailibilityButton").click(function() {
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").html("Vil du fjerne tilgjengelighet for dette skiftet?");

        if (questionAnswer) {
            //$(".shift#" + selectedShift).remove();
            //$("#modalTest").modal("toggle");
        }
    });

    $(".dayTop").click(function() {
        //alert($(this).html() + ", legge inn fravær"); //TODO: legge inn fravær på en enkelt dag
        shiftType = 4;
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").html("Legge inn fravær for " + $(this).html() + "?");
    });

    $("#noButton").click(function() {
       questionAnswer = false;
    });

    $("#yesButton").click(function() {
        questionAnswer = true;
        $("#modalYesNo").modal("toggle");
        if (shiftType === 0) {
            $(".shift#" + selectedShift).removeClass("normal-shift").addClass("absence-shift");
        } else if (shiftType === 1) {
            $(".shift#" + selectedShift).removeClass("absence-shift").addClass("normal-shift");
        } else if (shiftType === 2){
            $(".shift#" + selectedShift).remove();
        }//Shift type 3 is missing

        if (shiftType === 4) {
            //Add availibility for a single day??
        } else {
            $("#modalTest").modal("toggle"); //Legge til hide isteden for toggle?
        }
    });

    $(".co-worker-panel-box").click(function() {
        //alert($(this).html());
        $(".co-worker-name").html($(this).html());
        //$("#shift-modal-shadow").css("display", "block");
        $("#modalUserProfile").modal("show");
    });

    $("#modalUserProfile").on("shown.bs.modal", function() {
        $("#shift-modal-shadow").css("display", "block");
        //$("#shift-modal-shadow").addClass("shown-modal-overlay");
    });

    $("#modalUserProfile").on("hidden.bs.modal", function() {
        $("#shift-modal-shadow").css("display", "none");
    });
});
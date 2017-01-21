/**
 * Created by Knut on 12.01.2017.
 */

$(document).ready(function() {


    var selectedShift = -1;
    var questionAnswer = false;
    var shiftType = -1; //Goes from 0 to 4, 0 = normal-shift, 1 = absence-shift, 2 = availible-shift, 3 = ??, 4 =

    var username = $("#username").html();

    /* For displaying the weeknames and current month*/
    var dayCounter = 1; // [0-6?]
    var today = new Date();
    var dateCounter = 1; // [0-31]
    var dayNames = ["Søndag", "Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag"];
    var monthNames = ["jan.", "feb.", "mar.", "apr.", "jun.", "jul.", "aug.", "sep.", "okt", "nov.", "des."];
    //TODO: fix this when changing week

    /* To change the date names */

    function changeTopDayNames() {

    }

    $(".dayTop").each(function() {
        //dateCounter = today.getDate();

        $(this).html(dayNames[dateCounter] + " " + (today.getDate() - today.getDay() + dayCounter) + ". " + monthNames[today.getMonth()]);
        if (today.getDay() === dayCounter) {
            $(this).addClass("dayTop-today");
        }
        dayCounter++; /*TODO: monthNames[today.getMonth() vil kanskje ikke vise riktig måned i månedsskifte */
        dateCounter++;
    });

    var currentWeekUrl = "/api/shift/" + username +"/week";
    var url = currentWeekUrl;

    function getShifts(url) {
        $.get(url, function() {// + today.getFullYear() + "/" + today.getMonth() + "/1", function(data) {//TODO: legg inn brukernavn som er pålogget
            //alert("okidoki" + data);
        }).done(function(data) {
            var jsonArray = data;

            for(var i = 0; i < jsonArray.length; i++) {
                var obj = jsonArray[i];
                var user_model = obj.user_model;
                //alert(user_model.id);

                //alert(jsonArray[i].start_time);
                var shiftStart = new Date(jsonArray[i].start_time);
                var shiftEnd = new Date(jsonArray[i].end_time);
                //alert(dateStart.getHours());
                var elementDistanceTop = shiftStart.getHours() * (44.5 / 12); //44.5 is the height of 12 hours //TODO: make constant of this
                var hoursOfWork = Math.abs(shiftEnd - shiftStart) / 3600000; //3600000 is milliseconds in hour
                var elementHeight = hoursOfWork * (44.5 / 12); //44.5 is the height of 12 hours

                var dateNumber = shiftStart.getDate() - (today.getDate() - today.getDay());

                var absence = "";
                if (obj.absent) {
                    absence = " absence-shift";
                } else {
                    absence = " normal-shift"
                }

                var newElement = '<div id="2" class="shift' + absence + '" style="top: ' + elementDistanceTop + 'vh; height: ' + elementHeight + 'vh">Skift test</div>';

                $(".shiftsheet .dayDisplay:nth-child(" + dateNumber + ") .dayInnhold").append(newElement);
            }

            //alert("DONE. ok" + data);
        }).fail(function() {
            alert("Det skjedde en feil med innhenting av skift for brukeren.");
        });
    };
    getShifts(url);

    $(".dayInnhold").on("click", ".shift", function(e) {//".dayInnhold").on("click", ".shift",



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
            $("#shift-time").html("Det har lagt inn fravær for skiftet ditt kl. 12:00 - 24:00 på Onsdag 11. jan. 2017");
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
        e.preventDefault();
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
        $("#yesNo-Question").html("Legge inn tilgjengelighet for " + $(this).html() + "?");
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

    /* For displaying previous and next week */
    $("#buttonPreviousWeek").click(function() {
        $(".shift").remove();
        //TODO: make function

        url = "/api/shift/haraldfw/2017/0/20/week";

        getShifts(url);
    });

    //TODO: change the topDisplay to display new current days

    $("#buttonNextWeek").click(function() {
        $(".shift").remove();
    });
});
/**
 * Created by Knut on 12.01.2017.
 */

$(document).ready(function() {


    var selectedShift = -1;
    var questionAnswer = false;
    var shiftType = -1; //Goes from 0 to 4, 0 = normal-shift, 1 = absence-shift, 2 = availible-shift, 3 = ??, 4 =

    var username = $("#username").html();

    /* For displaying the weeknames and current month*/
    var dayCounter = 0; // [0-6, man-søn]
    var today = new Date();
    //var dateCounter = 1; // [0-31]
    //var dayNames = ["Søndag", "Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag"];
    var dayNames = ["Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"];
    //I javascript er 0=søndag, 1= mandag osv.
    var tempFix = [6, 0, 1, 2, 3, 4, 5];
    var monthNames = ["jan.", "feb.", "mar.", "apr.", "mai.", "jun.", "jul.", "aug.", "sep.", "okt", "nov.", "des."];

    var weekStartDate = today.getDate() - tempFix[today.getDay()];


    /* Function for adding days to a javascript date object */
    function addDays(date, days) {
        return new Date(date.getTime() + days*24*60*60*1000); //24*60*60*60*1000 is milliseconds in a day
    }

    var currentDate = new Date();
    /* Function for changing what date and month is displayed at the top of each day */
    function changeTopDayNames() {
        $(".dayTop").each(function() {
            //dateCounter = today.getDate();
            var dateToday = weekStartDate + dayCounter;

            $(this).html(dayNames[dayCounter] + " " + currentDate.getDate() /*dateToday*/ + ". " + monthNames[currentDate.getMonth()/*today.getMonth()*/]);
            if (today.getDate() === dateToday) {
                $(this).addClass("dayTop-today");
            } else {
                //alert("har allerede denne klassen");
                if ($(this).hasClass("dayTop-today")) {
                    $(this).removeClass("dayTop-today");
                }
            }
            dayCounter++;
            //dateCounter++;

            currentDate = addDays(currentDate, 1);
        });
        currentDate = addDays(currentDate, -7);
    }
    changeTopDayNames();


    var currentWeekUrl = "/api/shift/" + username +"/week";
    var url = currentWeekUrl;

    function getShifts(url) {
        $.get(url, function() {// + today.getFullYear() + "/" + today.getMonth() + "/1", function(data) {//TODO: kan kanskje hente brukernavn i backend istenden
            //alert("okidoki" + data);
        }).done(function(data) {
            var jsonArray = data;

            for(var i = 0; i < jsonArray.length; i++) {
                var obj = jsonArray[i];
                var user_model = obj.user_model;
                //alert(user_model.id);

                //alert(jsonArray[i].start_time);
                var shiftId = obj.id;
                var shiftStart = new Date(jsonArray[i].start_time);
                var shiftEnd = new Date(jsonArray[i].end_time);
                //alert(dateStart.getHours());
                var elementDistanceTop = shiftStart.getHours() * (44.5 / 12); //44.5 is the height of 12 hours //TODO: make constant of this
                var hoursOfWork = Math.abs(shiftEnd - shiftStart) / 3600000; //3600000 is milliseconds in hour

                var totalElementHeight = hoursOfWork * (44.5 / 12); //44.5 is the height of 12 hours
                //TODO: does only work with hours yet

                var absence = "";
                if (obj.absent) {
                    absence = " absence-shift";
                } else {
                    absence = " normal-shift"
                }

                //var dateNumber = shiftStart.getDate() - weekStartDate + 1;//(today.getDate() - today.getDay());
                var dateNumber = shiftStart.getDate() - (currentDate.getDate() - tempFix[currentDate.getDay()]) + 1;//(today.getDate() - today.getDay());

                var fromTime = "";
                if (shiftStart.getHours() < 10) {
                    fromTime = "0";//TODO: fix 00:00 --> blir 0:00
                }
                fromTime = shiftStart.getHours() + ":";
                if (shiftStart.getMinutes() % 10 === 0) {
                    fromTime += "0";
                }
                fromTime += shiftStart.getMinutes();

                var toTime = "";
                if (shiftEnd.getHours() < 10) {
                    toTime = "0";
                }
                toTime += shiftEnd.getHours() + ":";
                if (shiftEnd.getMinutes() % 10 === 0) {
                    toTime += "0";
                }
                toTime += shiftEnd.getMinutes();

                var shiftCenteredText = '<p class="shift-center-text">' + fromTime + ' - ' + toTime + '</p></div>';
                var dateRange = '<br />' + shiftStart.getDate() + ". " + monthNames[shiftStart.getMonth()] + ' - ' + shiftEnd.getDate() + ". " + monthNames[shiftEnd.getMonth()];
                var shiftCenteredTextTwoDays = '<p class="shift-center-text">' + fromTime + ' - ' + toTime + dateRange + '</p></div>';

                //For checking if the shift start at a date and goes to the next date
                if (shiftStart.getDate() === shiftEnd.getDate() && shiftStart.getMonth() === shiftEnd.getMonth() &&
                                                                shiftStart.getFullYear() === shiftEnd.getFullYear()) {
                    //If the shift is only on the same day
                    var newElement = '<div id="' + shiftId + '" class="shift' + absence + '" style="top: ' + elementDistanceTop + 'vh; height: ' + totalElementHeight + 'vh">' +
                        shiftCenteredText;
                    $(".shiftsheet .dayDisplay:nth-child(" + dateNumber + ") .dayInnhold").append(newElement);
                } else {
                    //If the shift goes from one day to another
                    //elementHeight = hoursOfWork * (44.5 / 12);
                    var heightDone = 89 - elementDistanceTop;

                    //alert(elementHeight); //=129

                    //elementHeight = heightDone; //89vh is the max size of "dayInnhold" elements
                    //TODO: elementHeight

                    //Element 1, det som går til enden først
                    var newElement = '<div id="' + shiftId + '" class="shift' + absence + ' shift-non-rounded-bottom" style="top: ' + elementDistanceTop + 'vh; height: ' + heightDone + 'vh">' +
                        shiftCenteredText;
                    $(".shiftsheet .dayDisplay:nth-child(" + dateNumber + ") .dayInnhold").append(newElement);

                    //Element next day(s)

                    var extraElementCounter = 0;
                    var nonRoundedClass = "";
                    while(totalElementHeight > heightDone) {//TODO: endre til sånn at den går maks 7 ganger
                        //alert(totalElementHeight + "; " + heightDone);
                        var currentElementHeight = 0;
                        if ((totalElementHeight - heightDone) > 89) {
                            //Det er større enn en dag og man vil få element med samme størrelse som en dag
                            currentElementHeight = 89;
                            heightDone += 89;
                            nonRoundedClass = "shift-non-rounded-both";
                        } else {
                            currentElementHeight = totalElementHeight - heightDone;
                            heightDone += currentElementHeight;
                            nonRoundedClass = "shift-non-rounded-top";
                        }
                        extraElementCounter++;

                        var newElementNextDay = '<div id="' + shiftId + '" class="shift' + absence + ' ' + nonRoundedClass +'" style="top: 0; height: ' + currentElementHeight + 'vh">' +
                            shiftCenteredTextTwoDays;
                        $(".shiftsheet .dayDisplay:nth-child(" + (dateNumber+extraElementCounter) + ") .dayInnhold").append(newElementNextDay);
                    }

                    /*var elementHeightNextDay = (hoursOfWork * (44.5 / 12)) - elementHeight;
                    var newElementNextDay = '<div id="2" class="shift' + absence + '" style="top: 0vh; height: ' + elementHeightNextDay + 'vh">Skift test</div>';
                    $(".shiftsheet .dayDisplay:nth-child(" + (dateNumber+1) + ") .dayInnhold").append(newElementNextDay);*/
                }


            }

            //alert("DONE. ok" + data);
        }).fail(function() {
            alert("Det skjedde en feil med innhenting av skift for brukeren.");
        });
    }
    getShifts(url);

    function getAvailibleUsers(url) {
        $.get(url, function() {
            alert("okidoki1");
        }).done(function() {
            alert("okidoki2");
        }).fail(function () {
            alert("Det skjedde en feil med innhenting av data for skift.");
        });
    }

    //Clickevent handler for a shift element
    $(".dayInnhold").on("click", ".shift", function(e) {//".dayInnhold").on("click", ".shift",

        $("#modalTest").modal("show");
        //TODO: gjøre ting med iden man får her og hente fra database$(this).attr("id")
        selectedShift = $(this).attr("id");

        //Get availible users for changing worker of a shift
        var availibilityUrl = "/api/shift/get_available_users_for_shift?shift_id=" + selectedShift;
        getAvailibleUsers(availibilityUrl);

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

        currentDate = addDays(currentDate, -7);

        weekStartDate -= 7;
        dayCounter = 0;
        changeTopDayNames();

        //url = "/api/shift/haraldfw/2017/0/" + weekStartDate + "/week";
        url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";

        getShifts(url);
    });

    //TODO: change the topDisplay to display new current days

    $("#buttonNextWeek").click(function() {
        $(".shift").remove();

        currentDate = addDays(currentDate, 7);
        weekStartDate += 7;
        dayCounter = 0;
        changeTopDayNames();

        //url = "/api/shift/haraldfw/2017/0/" + weekStartDate + "/week"; //TODO: legg til månedsvariabel
        url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";

        getShifts(url);
    });
});
/**
 * Created by Knut on 12.01.2017.
 */

$(document).ready(function() {
    var selectedShift = -1;
    var questionAnswer = false;

    /*
    * shiftType is a handler that handles the last action of the user
    * [0, -->]
    * 0 = normal-shift
    * 1 = absence-shift
    * 2 = available-shift
    * ...
    * 5 = when a user is asked another co-worker about taking a shift
    * */
    var shiftType = -1;

    var username = $("#username").html();

    /* For displaying the weeknames and current month*/
    var dayCounter = 0; // [0-6, man-søn]
    var today = new Date();
    var dayNames = ["Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"];
    //I javascript er 0=søndag, 1= mandag osv.
    var tempFix = [6, 0, 1, 2, 3, 4, 5];
    var monthNames = ["jan.", "feb.", "mar.", "apr.", "mai.", "jun.", "jul.", "aug.", "sep.", "okt", "nov.", "des."];
    var weekStartDate = addDays(today, -tempFix[today.getDay()]);

    var shiftIderForIdag = [];

    /* Function for adding days to a javascript date object */
    function addDays(date, days) {
        return new Date(date.getTime() + days*24*60*60*1000); //24*60*60*60*1000 is milliseconds in a day
    }

    var currentDate = weekStartDate; //new Date();
    /* Function for changing what date and month is displayed at the top of each day */
    function changeTopDayNames() {
        dayCounter = 0;
        $(".dayTop").each(function() {
            var dateToday = weekStartDate.getDate() + dayCounter;

            //$(this).html(dayNames[dayCounter] + " " + currentDate.getDate() /*dateToday*/ + ". " + monthNames[currentDate.getMonth()/*today.getMonth()*/]);
            $(this).html(dayNames[dayCounter] + " " + currentDate.getDate() + ". " + monthNames[currentDate.getMonth()/*today.getMonth()*/]);
            if (today.getDate() === currentDate.getDate() && today.getMonth() === currentDate.getMonth() && today.getFullYear() && currentDate.getFullYear()) {
                $(this).addClass("dayTop-today");
            } else {
                if ($(this).hasClass("dayTop-today")) {
                    $(this).removeClass("dayTop-today");
                }
            }
            dayCounter++;

            currentDate = addDays(currentDate, 1);
        });
        currentDate = addDays(currentDate, -7);
    }
    changeTopDayNames();

    var currentWeekUrl = "/api/shift/" + username +"/week";
    //var url = currentWeekUrl; //TODO: sjekk om er brukt og fjern

    function getShifts(url) { //TODO: teste og sjekke om denne virker
        shiftIderForIdag = [];
        $.get(url, function() {// + today.getFullYear() + "/" + today.getMonth() + "/1", function(data) {//TODO: kan kanskje hente brukernavn i backend istenden
            //alert("okidoki" + data);
        }).done(function(data) {
            var jsonArray = data;

            for(var i = 0; i < jsonArray.length; i++) {
                var obj = jsonArray[i];
                var user_model = obj.user_model;

                var shiftId = obj.id;
                var shiftStart = new Date(jsonArray[i].start_time);
                var shiftEnd = new Date(jsonArray[i].end_time);
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
                var tempDateNumber = addDays(shiftStart, - (weekStartDate.getDate() + tempFix[currentDate.getDay()]) + 1);

                console.log("\n-------------------");
                console.log("\nNygreie");
                console.log('shiftStart.getDate(): ' +  shiftStart.getDate() +
                    ", currentDate.getDate(): " + currentDate.getDate() +
                    ", tempFix[currentDate.getDay()]: "+  tempFix[currentDate.getDay()]);

                dateNumber = tempDateNumber.getDate();
                //dateNumber = Math.floor(shiftStart.getTime() - weekStartDate.getTime());

                var fromTime = "";
                if (shiftStart.getHours() < 10) {
                    fromTime = "0";
                }
                fromTime += shiftStart.getHours() + ":";
                if (shiftStart.getMinutes() < 10) {
                    fromTime += "0";
                }
                fromTime += shiftStart.getMinutes();

                var toTime = "";
                if (shiftEnd.getHours() < 10) {
                    toTime = "0";
                }
                toTime += shiftEnd.getHours() + ":";
                if (shiftEnd.getMinutes() < 10) {
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
                    //alert(dateNumber + "; "); //TODO: CHECK
                } else {
                    //If the shift goes from one day to another
                    //elementHeight = hoursOfWork * (44.5 / 12);
                    var heightDone = 89 - elementDistanceTop;

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
                }
            }

            //For hovering all shift elements with matching id (all elements for same shift) //TODO: endre til alle typer skift
            $(".shift").hover(function() {
                var matchId = $(this).attr("id");
                $(".shift").each(function() {
                    if ($(this).attr("id") === matchId) {
                        $(this).toggleClass("normal-shift-matching");
                    }
                });

            }), function() {
                var matchId = $(this).attr("id");
                $(".shift").each(function() {
                    if ($(this).attr("id") === matchId) {
                        $(this).toggleClass("normal-shift-matching");
                    }
                });
            };

            if (shiftEnd < today) {
                shiftIderForIdag.push(shiftId);
            }

            if (shiftEnd < (today + 2)) {
                //TODO: sjekk dette
            }

            //alert("DONE. ok" + data);
        }).fail(function() {
            alert("Det skjedde en feil med innhenting av skift for brukeren.");
        });
    }
    getShifts(currentWeekUrl);

    //TODO: fiks andre metoden som ikke trenger å gå til /dato/week, men bare /week
    var currentWeekAvailability = "/api/available/" + username +"/" + weekStartDate.getFullYear() + "/" + weekStartDate.getMonth() + "/" + weekStartDate.getDate() + "/week";
    function getAvailability(url) {
        $.get(url, function() {

        }).done(function(data) {
            var availabilityArray = data;
            for (var i = 0; i < availabilityArray.length; i++) {
                //alert("hei " + availabilityArray[i].start_time);
                var currentA = availabilityArray[i]; //currentA is shortcut for currentAvailability[i]
                var cAStart = new Date(availabilityArray[i].start_time);
                //alert("start-tid: " + availabilityArray[i].start_time.getDate());
                var cAEnd = new Date(availabilityArray[i].end_time);
                var currentAId = 123; //TODO: legg inn id til available fra databasen

                var elementDistanceTop = cAStart.getHours() * (44.5 / 12); //44.5 is the height of 12 hours //TODO: make constant of this here too
                var hoursOfWork = Math.abs(cAEnd - cAStart) / 3600000; //3600000 is milliseconds in hour

                var totalElementHeight = hoursOfWork * (44.5 / 12); //44.5 is the height of 12 hours //TODO: does only work with hours yet
                alert(elementDistanceTop  + "; " + totalElementHeight);
                if (cAStart.getDate() === cAEnd.getDate() && cAStart.getMonth() === cAEnd.getMonth() &&
                    cAStart.getFullYear() === cAEnd.getFullYear()) {

                    //If the shift is only on the same day
                    var newElement = '<div id="' + currentAId + '" class="shift available-shift" style="top: ' + elementDistanceTop + 'vh; height: ' + totalElementHeight + 'vh">' +
                        shiftCenteredText;
                    alert(newElement);
                    $(".shiftsheet .dayDisplay:nth-child(" + dateNumber + ") .dayInnhold").append(newElement);
                }
            }
        }).fail(function(data) {
            alert("Det skjedde en feil med innhenting av tilgjengelighet for brukeren.");
        });
    }
    //getAvailability(currentWeekAvailability);

    function getAvailableUsers(url) {
        $.get(url, function() {
            //alert("okidoki1");
        }).done(function(data) {
            $(".a-p-box").remove(); //a-p-box = available workers that can take a shift
            var jsonArray = data;
            var workerCounter = 1;
            for (var i = 0; i < jsonArray.length; i++) {
                var workerId = jsonArray[i].id;
                var workerName = jsonArray[i].first_name + " " + jsonArray[i].last_name;
                var workerType = "panel-footer ";
                if (workerCounter % 2 === 0) {
                    workerType = "panel-body ";
                }
                if (username === jsonArray[i].username) {
                    //TODO: fix
                } else {
                    $("#co-worker-available-collapse").append('<div id="' + workerId + '" class="' + workerType + 'co-worker-panel-box a-p-box">' + workerName + '</div>');
                    workerCounter++;
                }
            }
        }).fail(function () {
            alert("Det skjedde en feil med innhenting av data for skift.");
        });
    }

    var sendUrl = "";

    $(".co-worker-list").on("click", ".a-p-box", function(e) {
        //alert($(this).attr("id") + ", " + selectedShift);
        var selectedWorkerId = $(this).attr("id");
        sendUrl = "/api/notifications/generate_transfer_request_notification?shift_id=" + selectedShift + "&user_id=" + selectedWorkerId;
        var nameForChanger = $(this).html();

        shiftType = 5;
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").html("Vil du spørre " + nameForChanger + " om å ta over skiftet ditt?");

        e.preventDefault();
    });

    //Clickevent handler for a shift element
    $(".dayInnhold").on("click", ".shift", function(e) {//".dayInnhold").on("click", ".shift",

        $("body").addClass("modal-prevent-jump"); //TODO: legg denne på show og hide modal

        $("#modalTest").modal("show");
        selectedShift = $(this).attr("id");

        //Get available users for changing worker of a shift
        var availabilityUrl = "/api/shift/get_available_users_for_shift?shift_id=" + selectedShift;
        getAvailableUsers(availabilityUrl);

        //Starter modal for hvert type skift man trykker på, men endrer innholdet.
        if ($(this).hasClass("normal-shift")) {
            $("#modal-shift-title").html("Skift Onsdag 11. jan. 2017");
            $("#shift-time").html("Du har et skift fra 00:00 - 12:00 på Onsdag 11. jan. 2017");
            $("#absenceButtonDiv").css("display", "inline-block");
            $("#removeAbsenceButtonDiv").css("display", "none");
            $("#removeAvailabilityButtonDiv").css("display", "none");
            shiftType = 0;
            $("#other-workers-panel").css("display", "block");
            $("#changeShiftOwnerButtonDiv").css("display", "block");

        } else if ($(this).hasClass("absence-shift")) {
            $("#modal-shift-title").html("Skift Onsdag 11. jan. 2017");
            $("#shift-time").html("Det har lagt inn fravær for skiftet ditt kl. 12:00 - 24:00 på Onsdag 11. jan. 2017");
            $("#absenceButtonDiv").css("display", "none");
            $("#removeAbsenceButtonDiv").css("display", "inline-block");
            $("#removeAvailabilityButtonDiv").css("display", "none");
            shiftType = 1;
            $("#other-workers-panel").css("display", "block");
            $("#changeShiftOwnerButtonDiv").css("display", "none");

        } else if ($(this).hasClass("available-shift")) {
            $("#modal-shift-title").html("Skift Lørdag 14. jan. 2017");
            $("#shift-time").html("Du har satt deg tilgjengelig for dette skiftet, fra 00:00 - 12:00 på Lørdag 14 jan. 2017");
            $("#absenceButtonDiv").css("display", "none");
            $("#removeAbsenceButtonDiv").css("display", "none");
            $("#removeAvailabilityButtonDiv").css("display", "inline-block");
            shiftType = 2;
            $("#other-workers-panel").css("display", "none");
            $("#changeShiftOwnerButtonDiv").css("display", "none");
        }

        e.preventDefault();
    });

    /* Modal buttons
    *  goes under here */

    $("#changeShiftOwnerButton").click(function() {
        $("#available-workers-panel").toggleClass("non-display-class");
    });

    $("#absenceButton").click(function() {
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").html("Vil du melde om fravær for dette skiftet?");
    });

    $("#removeAbsenceButton").click(function() {
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").html("Vil du fjerne fravær for dette skiftet?");
    });

    $("#removeAvailabilityButton").click(function() {
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
        if (shiftType === 0) { //Shift goes from normal to absence
            var postUrl = "/api/user/" + username + "/registerabsence/" + selectedShift;
            /*}).fail(function() {alert("Det gikk en feil med å legge inn fravær for dette skiftet")*/
            //TODO: legg til egenmeldingsystem
            $.ajax({
                url: postUrl,
                type: 'PUT',
                success: function(res) {
                    $(".shift#" + selectedShift).removeClass("normal-shift").addClass("absence-shift");
                    $(".shift").each(function() {
                        if ($(this).attr("id") === selectedShift) {
                            $(this).removeClass("normal-shift").addClass("absence-shift");
                        }
                    });
                }//TODO: legg inn error/fail
            });

        } else if (shiftType === 1) {
            $(".shift#" + selectedShift).removeClass("absence-shift").addClass("normal-shift");
        } else if (shiftType === 2){
            $(".shift#" + selectedShift).remove();
        }//Shift type 3 is missing

        if (shiftType === 5) {
            //When a user is asked another co-worker about taking the shift
            $.post(sendUrl, function() {

            }).done(function() {
                alert("Du har sendt forespørsel om å bytte dette skiftet.");
            }).fail(function() {
                alert("Kunne ikke sende forespørsel om vaktbytte");
            });
        }

        if (shiftType === 4) {
            //Add availability for a single day??
        } else {
            $("#modalTest").modal("toggle"); //Legge til hide isteden for toggle?
        }
    });

    $(".co-worker-panel-box").click(function() {
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

    $("#modalTest").on("shown.bs.modal", function() {
        var funnet = false;
        for (var i = 0; i < shiftIderForIdag.length; i++) {
            var currentId = parseInt(shiftIderForIdag[i]);
            if (currentId === parseInt(selectedShift)) {
                funnet = true;
                break;
            }
        }

        if (funnet) {
            $("#changeActualStartEndTimesButton").css("display", "block");
        } else {
            $("#changeActualStartEndTimesButton").css("display", "none");
        }
    });

    /*$("#modalTest").on("shown.bs.modal", function() {
        $("body").addClass("modal-prevent-jump");
    });

    $("#modalTest").on("hidden.bs.modal", function() {
        $("body").removeClass("modal-prevent-jump");
    });*/ //TODO: fix this

    /* For displaying previous and next week */
    $("#buttonPreviousWeek").click(function() {
        $(".shift").remove();
        //TODO: make function

        currentDate = addDays(currentDate, -7);

        weekStartDate = addDays(weekStartDate, -7);
        changeTopDayNames();

        url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";

        getShifts(url);
    });

    //TODO: change the topDisplay to display new current days

    $("#buttonNextWeek").click(function() {
        $(".shift").remove();

        currentDate = addDays(currentDate, 7);
        weekStartDate = addDays(weekStartDate, 7);
        changeTopDayNames();

        url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";

        getShifts(url);
    });
});
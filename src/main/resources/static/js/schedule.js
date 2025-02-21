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
    * 6 = when a user wants to remove availability from the schedule
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
    var monthLongNames = ["Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"];
    var weekStartDate = addDays(today, -tempFix[today.getDay()]);

    var shiftIderForIdag = [];
    var currentYears = [];

    /* For calendar */
    $(".cell-cal").hover(function() {
        if($(this).hasClass("inactive-month")) {
            $(this).toggleClass("hover-adjust hover-adjust-inactive");
        } else if($(this).hasClass("today")) {
            $(this).toggleClass("hover-adjust hover-adjust-today");
        } else {
            $(this).toggleClass("hover-adjust");
        }

        if($($(this).siblings(".cell-cal")).hasClass("inactive-month")) {
            $(this).siblings(".cell-cal.inactive-month").toggleClass("hover-adjust hover-adjust-inactive");
            $(this).siblings(".cell-cal").not(".inactive-month").toggleClass("hover-adjust");
        } else if($($(this).siblings(".cell-cal")).hasClass("today")) {
            $(this).siblings(".cell-cal.today").toggleClass("hover-adjust hover-adjust-today");
            $(this).siblings(".cell-cal").not(".today").toggleClass("hover-adjust");
        } else {
            $(this).siblings(".cell-cal").toggleClass("hover-adjust");
        }
    });

    $(".cell-cal").click(function() {
        var monthYearArray = $(this).children(".month-year").html().split(" ");
        var dagIkkeArray = $(this).children(".display-day").html();

        var datoo = new Date(monthYearArray[0], monthYearArray[1] , dagIkkeArray);

        $(".shift").remove();

        weekStartDate = addDays(datoo, -tempFix[datoo.getDay()]);
        currentDate = weekStartDate;
        changeTopDayNames();

        $("#calendarModal").modal("toggle");
        $(".cell-cal").removeClass("active-day active-week-left active-week-middle active-week-right");

        $(this).parent().children("td:nth-of-type(2)").addClass("active-week-left active-week-middle");
        for(var k = 3; k < 8; k++) { //fiks på bedre måte
            $(this).parent().children("td:nth-of-type(" + k + ")").addClass("active-week-middle");
        }
        $(this).parent().children("td:nth-of-type(8)").addClass("active-week-right active-week-middle");

        currentWeekAvailability = "/api/available/" + username +"/" + weekStartDate.getFullYear() + "/" + weekStartDate.getMonth() + "/" + weekStartDate.getDate() + "/week";
        url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";

        getShifts(url);

    });


    /* End for calendar */

    /* Function for adding days to a javascript date object */
    function addDays(date, days) {
        return new Date(date.getTime() + days*24*60*60*1000); //24*60*60*60*1000 is milliseconds in a day
    }

    var currentDate = weekStartDate; //new Date();
    /* Function for changing what date and month is displayed at the top of each day */
    function changeTopDayNames() {
        dayCounter = 0;
        currentYears = [];
        $("#currentDate").text(weekStartDate.getDate() + ". " + monthLongNames[weekStartDate.getMonth()] + " " + weekStartDate.getFullYear());
        $(".dayTop").each(function() {
            var dateToday = weekStartDate.getDate() + dayCounter;

            //$(this).html(dayNames[dayCounter] + " " + currentDate.getDate() /*dateToday*/ + ". " + monthNames[currentDate.getMonth()/*today.getMonth()*/]);
            $(this).text(dayNames[dayCounter] + " " + currentDate.getDate() + ". " + monthNames[currentDate.getMonth()/*today.getMonth()*/]);
            if (today.getDate() === currentDate.getDate() && today.getMonth() === currentDate.getMonth() && today.getFullYear() && currentDate.getFullYear()) {
                $(this).addClass("dayTop-today"); //TODO; FIX
            } else {
                if ($(this).hasClass("dayTop-today")) {
                    $(this).removeClass("dayTop-today");
                }
            }
            dayCounter++;

            currentYears.push(currentDate.getFullYear());
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
                    absence += " absence-shift";
                //} else if (obj.locked) {
                    //absence += " locked-shift";
                } else {
                    absence += " normal-shift";
                }

                if (obj.locked) {
                    absence += " locked-shift";
                }

                //var dateNumber = shiftStart.getDate() - weekStartDate + 1;//(today.getDate() - today.getDay());
                var dateNumber = shiftStart.getDate() - (currentDate.getDate() - tempFix[currentDate.getDay()]) + 1;//(today.getDate() - today.getDay());
                var tempDateNumber = addDays(shiftStart, - (weekStartDate.getDate() + tempFix[currentDate.getDay()]) + 1);

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

                var lockedSign = "";
                if (obj.locked) {
                    lockedSign = '<i class="ion-locked locked-sign-top"></i>';
                }

                //For checking if the shift start at a date and goes to the next date
                if (shiftStart.getDate() === shiftEnd.getDate() && shiftStart.getMonth() === shiftEnd.getMonth() &&
                                                                shiftStart.getFullYear() === shiftEnd.getFullYear()) {
                    //If the shift is only on the same day
                    var newElement = '<div id="' + shiftId + '" class="shift' + absence + '" style="top: ' + elementDistanceTop + 'vh; height: ' + totalElementHeight + 'vh">' +
                        lockedSign + shiftCenteredText;
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
                        lockedSign + shiftCenteredText;
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
                            lockedSign + shiftCenteredTextTwoDays;
                        $(".shiftsheet .dayDisplay:nth-child(" + (dateNumber+extraElementCounter) + ") .dayInnhold").append(newElementNextDay);
                    }
                }

                if (shiftEnd < today) {
                    shiftIderForIdag.push(shiftId);
                }

                if (shiftEnd < (today + 2)) {
                    //TODO: sjekk dette
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

            getAvailability(currentWeekAvailability);
            //alert("DONE. ok" + data);
        }).fail(function() {
            alert("Det skjedde en feil med innhenting av vakter for brukeren.");
        });
    }
    getShifts(currentWeekUrl);

    //TODO: fiks andre metoden som ikke trenger å gå til /dato/week, men bare /week
    var currentWeekAvailability = "/api/available/" + username +"/" + weekStartDate.getFullYear() + "/" + weekStartDate.getMonth() + "/" + weekStartDate.getDate() + "/week";
    function getAvailability(url) { //TODO: skal lage egen metode for å legge inn skift element
        $.get(url, function() {

        }).done(function(data) {
            var availabilityArray = data;
            for (var i = 0; i < availabilityArray.length; i++) {
                //var currentA = availabilityArray[i]; //currentA is shortcut for currentAvailability[i]
                var cAStart = new Date(availabilityArray[i].start_time);
                //alert("start-tid: " + availabilityArray[i].start_time.getDate());
                var cAEnd = new Date(availabilityArray[i].end_time);
                var currentAId = 123; //TODO: legg inn id til available fra databasen

                var elementDistanceTop = cAStart.getHours() * (44.5 / 12); //44.5 is the height of 12 hours //TODO: make constant of this here too
                var hoursOfWork = Math.abs(cAEnd - cAStart) / 3600000; //3600000 is milliseconds in hour

                var totalElementHeight = hoursOfWork * (44.5 / 12); //44.5 is the height of 12 hours //TODO: does only work with hours yet
                //alert(elementDistanceTop  + "; " + totalElementHeight + "; " + hoursOfWork);

                var fromTime = "";
                if (cAStart.getHours() < 10) {
                    fromTime = "0";
                }
                fromTime += cAStart.getHours() + ":";
                if (cAStart.getMinutes() < 10) {
                    fromTime += "0";
                }
                fromTime += cAStart.getMinutes();

                var toTime = "";
                if (cAEnd.getHours() < 10) {
                    toTime = "0";
                }
                toTime += cAEnd.getHours() + ":";
                if (cAEnd.getMinutes() < 10) {
                    toTime += "0";
                }
                toTime += cAEnd.getMinutes();

                var shiftCenteredText = '<p class="shift-center-text">' + fromTime + ' - ' + toTime + '</p></div>';
                var dateRange = '<br />' + cAStart.getDate() + ". " + monthNames[cAStart.getMonth()] + ' - ' + cAEnd.getDate() + ". " + monthNames[cAEnd.getMonth()];
                var shiftCenteredTextTwoDays = '<p class="shift-center-text">' + fromTime + ' - ' + toTime + dateRange + '</p></div>';


                var dateNumber = cAStart.getDate() - (currentDate.getDate() - tempFix[currentDate.getDay()]) + 1;//(today.getDate() - today.getDay());
                var tempDateNumber = addDays(cAStart, - (weekStartDate.getDate() + tempFix[currentDate.getDay()]) + 1);

                dateNumber = tempDateNumber.getDate();

                if (cAStart.getDate() === cAEnd.getDate() && cAStart.getMonth() === cAEnd.getMonth() &&
                    cAStart.getFullYear() === cAEnd.getFullYear()) {

                    //If the shift is only on the same day
                    var newElement = '<div id="' + currentAId + '" class="shift available-shift" style="top: ' + elementDistanceTop + 'vh; height: ' + totalElementHeight + 'vh">' +
                        shiftCenteredText;
                    $(".shiftsheet .dayDisplay:nth-child(" + dateNumber + ") .dayInnhold").append(newElement);
                } else {
                    //If the shift goes from one day to another
                    //elementHeight = hoursOfWork * (44.5 / 12);
                    var heightDone = 89 - elementDistanceTop;

                    //elementHeight = heightDone; //89vh is the max size of "dayInnhold" elements
                    //TODO: elementHeight

                    //Element 1, det som går til enden først
                    var newElement = '<div id="' + currentAId + '" class="shift available-shift shift-non-rounded-bottom" style="top: ' + elementDistanceTop + 'vh; height: ' + heightDone + 'vh">' +
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

                        var newElementNextDay = '<div id="' + currentAId + '" class="shift available-shift ' + nonRoundedClass +'" style="top: 0; height: ' + currentElementHeight + 'vh">' +
                            shiftCenteredTextTwoDays;
                        $(".shiftsheet .dayDisplay:nth-child(" + (dateNumber+extraElementCounter) + ") .dayInnhold").append(newElementNextDay);
                    }
                }
            }


            workTimeUrl = "/api/shift/" + username + "/" + weekStartDate.getFullYear() + "/" + weekStartDate.getMonth() + "/" + weekStartDate.getDate() + "/work";
            getWorktimeAWeeek(workTimeUrl);

        }).fail(function(data) { //TODO: sjekk feilmelding?
            alert("Det skjedde en feil med innhenting av tilgjengelighet for brukeren.");
        });
    }
    //getAvailability(currentWeekAvailability);

    var workTimeUrl = "";
    function getWorktimeAWeeek(url) {
        $.get(url, function() {

        }).done(function(data) {
            $("#work-time-week").html("Timer uke " + weekStartDate.getWeek() + ": " + data + "");
        }).fail(function(data) {
            alert("Det skjedde en feil med innhenting av timer denne uken.");
        });
    }

    function getAvailableUsers(url) {
        $.get(url, function() {
            //alert("okidoki1");
        }).done(function(data) {
            $(".empty-a-p-box").remove();
            $(".a-p-box").remove(); //a-p-box = available workers that can take a shift
            var jsonArray = data;
            var workerCounter = 1;
            for (var i = 0; i < jsonArray.length; i++) {
                var workerId = jsonArray[i].id;
                var workerName = escapeHtml(jsonArray[i].first_name) + " " + escapeHtml(jsonArray[i].last_name);
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

            if (jsonArray.length === 0) {
                $("#co-worker-available-collapse").append('<div class="panel-footer co-worker-panel-box empty-a-p-box">Ingen ledige folk å spørre</div>');
            }
        }).fail(function () {
            alert("Det skjedde en feil med innhenting av data for vakten.");
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
        $("#yesNo-Question").text("Vil du spørre " + nameForChanger + " om å ta over vakten din?");

        e.preventDefault();
    });

    //Clickevent handler for a shift element
    $(".dayInnhold").on("click", ".shift", function(e) {//".dayInnhold").on("click", ".shift",

        //$("body").addClass("modal-prevent-jump"); //TODO: legg denne på show og hide modal

        selectedShift = $(this).attr("id");
        var funnet = false;
        for (var i = 0; i < shiftIderForIdag.length; i++) {
            var currentId = parseInt(shiftIderForIdag[i]);//Skift ider før idag
            if (currentId === parseInt(selectedShift)) {
                funnet = true;
                break;
            }
        }

        $("#change-shift-times-div").css("display", "none");
        $("#changeActualStartEndTimesButtonDiv").css("display", "none");
        $("#removeAvailabilityButtonDiv").css("display", "none");
        if (funnet) { //dersom man trykker på et skift som er før i tid
            //$("#changeActualStartEndTimesButtonDiv").css("display", "block");
            //Fjerne tilgjengelige personer til vakt også
            $("#changeShiftOwnerButtonDiv").css("display", "none");
            $("#available-workers-panel").css("display", "none");
            $("#absenceButtonDiv").css("display", "none");
        } else {
            $("#changeActualStartEndTimesButtonDiv").css("display", "none");
        }

        $("#modalTest").modal("show");

        if ($(this).hasClass("normal-shift")) { //TODO: det er vel bare vanlige skift som skal ha denne?
            //Get available users for changing worker of a shift
            var availabilityUrl = "/api/shift/get_available_users_for_shift?shift_id=" + selectedShift;
            getAvailableUsers(availabilityUrl);
        }

        var modalTitle = "";
        var bodyModalText = "";
        var shiftTopBarText = $(this).parent().siblings(".dayTop").html();
        var currentShiftYear = currentYears[$(this).parent().parent().index()];

        //Starter modal for hvert type skift man trykker på, men endrer innholdet.
        if ($(this).hasClass("normal-shift")) {
            modalTitle = "Vakt ";
            shiftType = 0;
            $("#absenceButtonDiv").css("display", "inline-block");
            $("#removeAbsenceButtonDiv").css("display", "none");
            $("#removeAvailabilityButtonDiv").css("display", "none");
            $("#changeShiftOwnerButtonDiv").css("display", "block");
            if (funnet) {
                $("#available-workers-panel").css("display", "none");
                $("#absenceButtonDiv").css("display", "none");
                $("#changeShiftOwnerButtonDiv").css("display", "none");
                $("#changeActualStartEndTimesButtonDiv").css("display", "block");
                $("#change-shift-times-div").css("display", "block");
                bodyModalText = "Du hadde en vakt fra ";
            } else {
                //$("#available-workers-panel").css("display", "block");
                $("#absenceButtonDiv").css("display", "block");
                $("#changeShiftOwnerButtonDiv").css("display", "block");

                bodyModalText = "Du har en vakt fra ";
            }

        } else if ($(this).hasClass("absence-shift")) {
            modalTitle = "Fravær ";
            bodyModalText = "Du lagt inn fravær for vakten din ";
            shiftType = 1;
            $("#absenceButtonDiv").css("display", "none");
            if (funnet) {
            } else {
                $("#removeAbsenceButtonDiv").css("display", "inline-block");
            }
            $("#changeShiftOwnerButtonDiv").css("display", "none");
            $("#available-workers-panel").css("display", "none");
            $("#changeActualStartEndTimesButtonDiv").css("display", "none");

        } else if ($(this).hasClass("available-shift")) {
            modalTitle = "Tilgjengelighet ";
            bodyModalText = "Du har satt deg tilgjengelig for perioden ";
            shiftType = 2;

            $("#absenceButtonDiv").css("display", "none");
            $("#removeAbsenceButtonDiv").css("display", "none");
            if (funnet) {
            } else {
                $("#removeAvailabilityButtonDiv").css("display", "inline-block");
            }
            $("#changeShiftOwnerButtonDiv").css("display", "none");
            $("#available-workers-panel").css("display", "none");
            $("#changeActualStartEndTimesButtonDiv").css("display", "none");

        } else if ($(this).hasClass("locked-shift")) {
            modalTitle = "Låst vakt";
            bodyModalText = "Denne vakten er under behandling og er foreløpig låst. " +
                "Kontakt administrasjonen dersom dette er en feil.";
        }
        $("#shift-time").html(bodyModalText + $(this).children("p").html() + " på " + shiftTopBarText + " " + currentShiftYear);
        if ($(this).hasClass("locked-shift")) {
            //Ingen knapper eller andre elementer skal vises her
            $("#absenceButtonDiv").css("display", "none");
            $("#removeAbsenceButtonDiv").css("display", "none");
            $("#changeShiftOwnerButtonDiv").css("display", "none");
            $("#available-workers-panel").css("display", "none");

            $("#modal-shift-title").text(modalTitle);
            $("#shift-time").html(bodyModalText + "<br>Dato: " + $(this).children("p").html() + " " + shiftTopBarText + " " + currentShiftYear);
        } else {
            $("#shift-time").html(bodyModalText + $(this).children("p").html() + " på " + shiftTopBarText + " " + currentShiftYear);
            $("#modal-shift-title").text(modalTitle + shiftTopBarText + " " + weekStartDate.getFullYear());
        }

        e.preventDefault();
    });

    /* Modal buttons
    *  goes under here */

    $("#changeShiftOwnerButton").click(function() {
       //$("#available-workers-panel").toggleClass("non-display-class");
        $("#available-workers-panel").css("display", "block");
        $(this).parent().css("display", "none");
    });

    $("#absenceButton").click(function() {
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").text("Vil du melde om fravær for denne vakten?");
    });

    $("#removeAbsenceButton").click(function() {
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").text("Vil du fjerne fravær for denne vakten?");
    });

    $("#removeAvailabilityButton").click(function() {
        //shiftType = 6;
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").text("Vil du fjerne tilgjengelighet for denne vakten?");
    });

    var clickedAvailabilityTop = -1;//TODO: legg inn -->
    $(".dayTop").click(function() {
        //alert($(this).html() + ", legge inn fravær"); //TODO: legge inn fravær på en enkelt dag
        shiftType = 4;
        $("body").addClass("modal-prevent-jump");
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").text("Legge inn tilgjengelighet for " + $(this).html() + "?");
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
                    $("#response-model-body").text("Du har lagt inn fravær for denne vakten.");
                    $("#response-modal").modal("show");
                },
                error: function(res) {
                    //TODO: håndtere hvilken type feilmelding
                    $("#response-model-body").text("Kunne ikke sette fravær for denn vakten.");
                    $("#response-modal").modal("show");
                }
            });

        } else if (shiftType === 1) {
            $(".shift#" + selectedShift).removeClass("absence-shift").addClass("normal-shift");
        } else if (shiftType === 2){
            $(".shift#" + selectedShift).remove();
        } else if (shiftType === 3) {
            $.post();
        }
        //Shift type 3 is missing

        if (shiftType === 5) {
            //When a user is asked another co-worker about taking the shift
            $.post(sendUrl, function() {

            }).done(function() {
                $("#response-modal-body").text("Du har sendt forespørsel om å bytte denne vakten");
                $("#response-modal").modal("show");
            }).fail(function() {
                $("#response-modal-body").text("Kunne ikke sende forespørsel om vaktbytte");
                $("#response-modal").modal("show");
            });
        }

        if (shiftType === 6) {

        }

        if (shiftType === 4) {
            //Add availability for a single day??
        } else {
            $("#modalTest").modal("toggle"); //Legge til hide isteden for toggle?
        }
    });

    $("#modalUserProfile").on("shown.bs.modal", function() {
        $("#shift-modal-shadow").css("display", "block");
        //$("#shift-modal-shadow").addClass("shown-modal-overlay");
    });

    $("#modalUserProfile").on("hidden.bs.modal", function() {
        $("#shift-modal-shadow").css("display", "none");
    });

    $("#modalTest").on("shown.bs.modal", function() {

    });

    $("#modalTest").on("hidden.bs.modal", function() {
        $("body").removeClass("modal-prevent-jump");//FIXME
    }); //TODO: fix this

    $("#modalYesNo").on("hidden.bs.modal", function() {
        $("body").removeClass("modal-prevent-jump"); //FIXME
    });

    /* For displaying previous and next week */
    $("#buttonPreviousWeek").click(function() {
        $(".shift").remove();
        //TODO: make function

        currentDate = addDays(currentDate, -7);

        weekStartDate = addDays(weekStartDate, -7);
        changeTopDayNames();

        currentWeekAvailability = "/api/available/" + username +"/" + weekStartDate.getFullYear() + "/" + weekStartDate.getMonth() + "/" + weekStartDate.getDate() + "/week";
        url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";

        getShifts(url);

        $(".cell-cal").removeClass("active-day active-week-left active-week-middle active-week-right today inactive-month hover-adjust hover-adjust-inactive hover-adjust-today");

        month = weekStartDate.getMonth();
        year = weekStartDate.getFullYear();
        firstDay = new Date(year, month, 1);
        plotDays(weekStartDate, weekStartDate);
    });

    //TODO: change the topDisplay to display new current days

    $("#buttonNextWeek").click(function() {
        $(".shift").remove();

        currentDate = addDays(currentDate, 7);
        weekStartDate = addDays(weekStartDate, 7);
        changeTopDayNames();

        currentWeekAvailability = "/api/available/" + username +"/" + weekStartDate.getFullYear() + "/" + weekStartDate.getMonth() + "/" + weekStartDate.getDate() + "/week";
        url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";

        getShifts(url);

        $(".cell-cal").removeClass("active-day active-week-left active-week-middle active-week-right today inactive-month hover-adjust hover-adjust-inactive hover-adjust-today");

        month = weekStartDate.getMonth();
        year = weekStartDate.getFullYear();
        firstDay = new Date(year, month, 1);
        plotDays(weekStartDate, weekStartDate);
    });

    $("#changeActualStartEndTimesButton").click(function() {
        if ($("#changeActualShiftTimesDatePDivs").css("display") == "none") {
            $("#changeActualShiftTimesDatePDivs").css("display", "block");
        } else {
            $("#changeActualShiftTimesDatePDivs").css("display", "none");
        }
    });

    $("#newStartShiftDatePicker").datetimepicker({
        format: "YYYY-MM-DD HH:mm",
        sideBySide: true
    });

    $("#newEndShiftDatePicker").datetimepicker({
        format: "YYYY-MM-DD HH:mm",
        useCurrent: false,
        sideBySide: true
    });

    $("#newStartShiftDatePicker").on("dp.change", function (e) {
        $('#newEndShiftDatePicker').data("DateTimePicker").minDate(e.date);
    });

    $("#newEndShiftDatePicker").on("dp.change", function (e) {
        $('#newStartShiftDatePicker').data("DateTimePicker").maxDate(e.date);
    });

    $("#currentDate").click(function() {
       $("#calendarModal").modal("show");
    });

    var changeShiftTimesUrl = "/api/shift/settime";
    $("#change-shift-times-button").click(function() {
        var newStartTime = $('#newStartShiftDatePicker').data("DateTimePicker").date();
        var newEndTime = $('#newEndShiftDatePicker').data("DateTimePicker").date();

        var jsonObject = {
            shift_id: selectedShift,
            start_time: newStartTime,
            end_time: newEndTime
        };

        //alert("start?");
        $.ajax({
            url: changeShiftTimesUrl,
            type: 'POST',
            data: JSON.stringify(jsonObject),
            contentType: 'application/json',
            dataType: "json",
            success: function(res) {
                //alert("okidokischmoki");
                //alert(res);
            },
            error: function(res) {
                //alert("Det skjedde en feil med å sette ny start og slutt tidspunkt.");
                //alert(res);
            },
            /*always: function(res) {
                alert("hei");
                getShifts(url);
            }*/
        }).always(function (data, textStatus, jqXHR) {
            $(".shift").remove();
            if (jqXHR.responseJSON) {
                //alert("asdsa");
            } else {
                //alert("html res");
                //alert(data);
                //alert(textStatus);
            }
            getShifts(url);
            $("#modalTest").modal("hide");

            //$("#response-modal").modal("show");
            //$("#response-model-body").text("Vakttider er oppdatert");
        });
        //alert("test");//FIXME: ASAP
    });

    $("#availability").submit(function() {
        return jsonSubmitFns(this, () => {
            //console.log('Close modal.');
            $("#myModal").modal("hide");
            $("#response-model-body").text("Du har lagt inn ny tilgjengelighet");
            $("#response-modal").modal("show");
            url = "/api/shift/" + username +"/" + currentDate.getFullYear() + "/" + currentDate.getMonth() + "/" + currentDate.getDate() + "/week";
            getShifts(url);
        }, (e) => {
            //console.log('Error: ', e);
            $("#myModal").modal("hide");
            $("#response-model-body").text("Kunne ikke legge inn tilgjengelighet");
            $("#response-modal").modal("show");
        }, dateProcces(['date_to', 'date_from']))
    });
});
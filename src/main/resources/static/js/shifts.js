/**
 * Created by Ingunn on 13.01.2017.
 */

$(document).ready(function() {


    var today = new Date();
    var username = $("#username").html();

    function plotShifts(date) {
        $.get("api/shift/" + date.getFullYear() + "/" + date.getMonth() + "/" + date.getDate() + "", function () {

        }).done(function (data) {
            var jArray = data;
            var monthNames = ["Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"];

            for (var i = 0; i < jArray.length; i++) {

                var startTime = new Date(jArray[i].start_time);
                var endTime = new Date(jArray[i].end_time);

                var minFrom = startTime.getMinutes();
                var hourFrom = startTime.getHours();
                var minTo = endTime.getMinutes();
                var hourTo = endTime.getHours();
                var minFromPros = minFrom / 60;
                var minToPros = minTo / 60;
                var diff = 0; //skiftlengde
                var lengde = 0; //start pkt

                //if same date
                if (startTime.getFullYear() === endTime.getFullYear() && startTime.getMonth() === endTime.getMonth() && startTime.getDate() === endTime.getDate()) {
                    diff = (hourTo + minToPros) - (hourFrom + minFromPros); // tiltid - starttid
                    lengde = hourFrom + minFromPros;

                } else if (startTime.getFullYear() === date.getFullYear() && startTime.getMonth() === date.getMonth() && startTime.getDate() === date.getDate()) {
                    //hvis vakt starter på valgt dato og går til ny dag
                    diff = 24.0 - (hourFrom + minFromPros);
                    lengde = hourFrom + minFromPros;

                } else if (endTime.getFullYear() === date.getFullYear() && endTime.getMonth() === date.getMonth() && endTime.getDate() === date.getDate()) {
                    //hvis vakt slutter på valgt dato
                    diff = hourTo + minToPros - 0; //ikke fjern 0
                    //lengde blir lik 0

                } else {
                    //hvis vakt går igjennom hele dagen
                    diff = 24;
                    //lengde blir lik 0
                }
                var kompArray;
                var komp;

                if(jArray[i].competences == null || jArray[i].competences === undefined || !(jArray[i].competences)) {
                    kompArray = [""];
                    komp = kompArray[0];
                } else {
                    if(jArray[i].competences.length !== 0) {
                        kompArray = jArray[i].competences;
                        kompArray.sort(); //Sorterer kompetanser i alfabetisk rekkefølge TODO: sorter også i back end

                        komp = kompArray[0].name;
                        for (var j = 1; j < kompArray.length; j++) {
                            komp += (", " + kompArray[j].name);
                        }
                    } else {
                        kompArray = [""];
                        komp = kompArray[0];
                    }
                }

                if (hourFrom < 10) {
                    hourFrom = "0" + hourFrom;
                }
                if (hourTo < 10) {
                    hourTo = "0" + hourTo;
                }
                if (minFrom < 10) {
                    minFrom = "0" + minFrom;
                }
                if (minTo < 10) {
                    minTo = "0" + minTo;
                }

                var classKomp;
                var tidtid = hourFrom + ":" + minFrom + " - " + hourTo + ":" + minTo;
                if (jArray[i].absent) { //hvis absent
                    classKomp = "no-worker";
                } else if(jArray[i].user_model == null || jArray[i].user_model === undefined || !(jArray[i].user_model)) { //hvis ingen bruker
                    classKomp = "no-worker";
                } else if(jArray[i].user_model.username === username) { //hvis innlogget bruker
                    classKomp = "self";
                } else {
                    classKomp = "worker";
                }
                var un;
                var navn;
                var telefon = "";
                var address = "";
                var email = "";
                var dateOfBirth = "";

                var tid = 'Start: ' + startTime.getFullYear() + '/' + (startTime.getMonth() + 1) + '/' + startTime.getDate() + ' ' + hourFrom + ':' + minFrom + '<br/>' +
                    'Slutt: ' + endTime.getFullYear() + '/' + (endTime.getMonth() + 1) + '/' + endTime.getDate() + ' ' + hourTo + ':' + minTo;
                if(jArray[i].user_model == null || jArray[i].user_model === undefined || !(jArray[i].user_model)) {
                    un = "";
                    navn = "";
                } else {
                    un = escapeHtml(jArray[i].user_model.username);
                    navn = escapeHtml(jArray[i].user_model.first_name) + " " + escapeHtml(jArray[i].user_model.last_name);
                    telefon = escapeHtml(jArray[i].user_model.phonenumber);
                    email = escapeHtml(jArray[i].user_model.email);
                    address = escapeHtml(jArray[i].user_model.address);
                    dateOfBirth = escapeHtml(jArray[i].user_model.date_of_birth);//TODO: FIX
                }


                var vakt = '<div class="shift-box ' + classKomp + '" style="left: calc(11.11% + ((11.11%/3)*' + lengde + ')); width: calc((11.11%/3)*' + diff + ' - 1px);"><p class="tidtidtid">' + tidtid +
                    '</p><p class="tidLagring" style="display: none;">' + tid + '</p><p class="unLagring" style="display: none;">' + un + '</p><p class="navnLagring" style="display: none;">' + navn + '</p>' +
                    '<p class="telefonLagring" style="display: none;">' + telefon + '</p>' +
                    '<p class="adresseLagring" style="display: none;">' + address +'</p>' +
                    '<p class="emailLagring" style="display: none;">' + email + '</p>' +
                    '<p class="dateofbirthLagring" style="display: none;">' + dateOfBirth + '</p></div>';

                var vaktRad = '<div class="rad">' +
                    '<div class="common-cell position-id">' + jArray[i].id + '</div>' +
                    '<div class="common-cell"></div>' +
                    '<div class="common-cell"></div>' +
                    '<div class="common-cell"></div>' +
                    '<div class="common-cell"></div>' +
                    '<div class="common-cell"></div>' +
                    '<div class="common-cell"></div>' +
                    '<div class="common-cell"></div>' +
                    '<div class="common-cell"></div>' +
                    vakt +
                    '</div>';

                var kompetansegruppe = '<div class="kompetansegruppe">' +
                    '<div class="rad kompetanse-rad">' +
                    '<div class="dropdown-cell testing">' +
                    '<i class="ion-navicon-round burger-style"></i>' +
                    '</div>' +

                    '<div class="kompetanse-cell testing">' +
                     komp +
                    '</div>' +
                    '</div>' +
                    '<div class="drop">' +
                     vaktRad +
                    '</div>' +
                    '</div>';


                if (i === 0) {
                    $("#superDiv").append(kompetansegruppe);
                } else {
                    var bool = false;
                    $(".kompetanse-cell.testing").filter(function (index) {
                        if ($(this).text() === komp) {
                            $($(this).parent().siblings(".drop")).append(vaktRad);
                            bool = true;
                        }
                    });
                    if (!(bool)) {
                        $("#superDiv").append(kompetansegruppe);
                        bool = false;
                    }
                }

            }

            $("#datedate").text(date.getDate() + ". " + monthNames[date.getMonth()] + " " + date.getFullYear());

            /* Andre functions */
            //$(".testing").click(function() {
            //$(".rad.kompetanse-rad").on("click", ".testing", function () {
            $(".testing").click(function() {

                if (($(this).parent().siblings(".drop").css("display") == "none")) {
                    $(this).parent().siblings(".drop").css("display", "inline");
                } else {
                    $(this).parent().siblings(".drop").css("display", "none");
                }
            });

            $(".no-worker").click(function () {
                $("#modalShift").modal("show");
                $("#modalFree").css("display", "inline");
                $("#modalOther").css("display", "none");
                $("#modalOwn").css("display", "none");
                var text = $(".tidtidtid", this).html();
                $(".modal-title").text(text);
                var tid = $(".tidLagring", this).html();
                $("#tidsviser").html(tid);
            });

            $(".self").click(function () {
                $("#modalShift").modal("show");
                $("#modalOwn").css("display", "inline");
                $("#modalOther").css("display", "none");
                $("#modalFree").css("display", "none");

                var text = $(".tidtidtid", this).html();
                $(".modal-title").text(text);
                var tid = $(".tidLagring", this).html();
                $("#tidsviser").html(tid);
                var navn = $(".navnLagring", this).html();

                selectedShiftId = $(this).parent().children(".position-id").html();

                var availabilityUrl = "/api/shift/get_available_users_for_shift?shift_id=" + selectedShiftId;
                getAvailableUsers(availabilityUrl);
            });

            $(".worker").click(function () {
                $("#modalShift").modal("show");
                $("#modalOther").css("display", "inline");
                $("#modalOwn").css("display", "none");
                $("#modalFree").css("display", "none");
                var text = $(".tidtidtid", this).html();
                $(".modal-title").text(text);

                var tid = $(".tidLagring", this).html();
                var navn = $(".navnLagring", this).html();
                var telefon = $(".telefonLagring", this).html();
                var adresse = $(".adresseLagring", this).html();
                var email = $(".emailLagring", this).html();
                var doBirth = $(".dateofbirthLagring", this).html();

                $("#tidsviser").html(tid);
                $("#worker-name-in-modal").text(navn);
                $("#worker-phone-number").text(telefon);
                $("#worker-phone-number").attr("href", "tel:" + telefon);
                $("#worker-address").text(adresse);
                $("#worker-email-address").text(email);
                $("#worker-email-address").attr("href", "mailto:" + email);
                $("#worker-birth-date").text(doBirth);
            });
        });

    }

    /* End of plotShifts */

    /* Get available users for a shift */
    function getAvailableUsers(url) {
        $.get(url, function() {
            //alert("okidoki1");
        }).done(function(data) {
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
        }).fail(function () {
            alert("Det skjedde en feil med innhenting av data for skift.");
        });
    }

    $(".timedisplay").click(function () {
        $("#infoTime").modal("show");
        var text = $(this).html();
        $("#titleTime").text(text);
    });

    /* Function for adding and subtracting days to a javascript date object */
    function addDays(date, days) {
        return new Date(date.getTime() + days*24*60*60*1000); //24*60*60*60*1000 is milliseconds in a day
    }


    plotShifts(today);
    var count = today;

    $("#dayBack").click(function() {
        $("#superDiv").empty();
        count = addDays(count, -1);
        plotShifts(count);
        $(".cell-cal").removeClass("active-day active-week-left active-week-middle active-week-right today inactive-month hover-adjust hover-adjust-inactive hover-adjust-today");

        month = count.getMonth();
        year = count.getFullYear();
        firstDay = new Date(year, month, 1);
        plotDays(count, count);
    });

    $("#dayForth").click(function() {
        $("#superDiv").empty();
        count = addDays(count, 1);
        plotShifts(count);
        $(".cell-cal").removeClass("active-day active-week-left active-week-middle active-week-right today inactive-month hover-adjust hover-adjust-inactive hover-adjust-today");

        month = count.getMonth();
        year = count.getFullYear();
        firstDay = new Date(year, month, 1);
        plotDays(count, count);


    });


    $(".cell-cal").hover(function() {
        if($(this).hasClass("inactive-month")) {
            $(this).addClass("hover-adjust hover-adjust-inactive");
        } else if($(this).hasClass("today")) {
            $(this).addClass("hover-adjust hover-adjust-today");
        } else {
            $(this).addClass("hover-adjust");
        }
    }, function () {
        if($(this).hasClass("inactive-month")) {
            $(this).removeClass("hover-adjust hover-adjust-inactive");
        } else if($(this).hasClass("today")) {
            $(this).removeClass("hover-adjust hover-adjust-today");
        } else {
            $(this).removeClass("hover-adjust");
        }
    });

    function sleep(milliseconds) {
        var start = new Date().getTime();
        for (var i = 0; i < 1e7; i++) {
            if ((new Date().getTime() - start) > milliseconds){
                break;
            }
        }
    }

    $(".cell-cal").click(function() {
        var way = 0;

        $(".cell-cal").removeClass("today active-day active-week-left active-week-middle active-week-right hover-adjust hover-adjust-inactive hover-adjust-today");
        if($(this).hasClass(".inactive-month")) {
            $(".cell-cal").removeClass("inactive-month");
            if((this).html() > 20) {
                way = -1;
            } else {
                way = 1;
            }
        } else {
            $(".cell-cal").removeClass("inactive-month");
        }

        var monthYearArray = $(this).children(".month-year").html().split(" ");
        var dagIkkeArray = $(this).children(".display-day").html();
        var datoo = new Date(monthYearArray[0], monthYearArray[1] , dagIkkeArray);

        $("#superDiv").empty();
        plotShifts(datoo);

        theActive = datoo;
        count = datoo;
        month = count.getMonth();
        year = count.getFullYear();
        firstDay = new Date(year, month, 1);
        firstDay.setMonth(firstDay.getMonth() + way);

        plotDays(count, count);

        $("#calendarModal").modal("toggle");

    });

    $(".modal-title-title").text("Velg dato");


    $("#datedate").click(function() {
        $("#calendarModal").modal("toggle");
    });




    var sendUrl = "";
    var shiftType = -1;
    var selectedShiftId = -1;
    $(".co-worker-list").on("click", ".a-p-box", function(e) {
        var selectedWorkerId = $(this).attr("id");
        sendUrl = "/api/notifications/generate_transfer_request_notification?shift_id=" + selectedShiftId + "&user_id=" + selectedWorkerId;
        var nameForChanger = $(this).html();

        shiftType = 5;
        $("#modalYesNo").modal("show");
        $("#yesNo-Question").text("Vil du spørre " + nameForChanger + " om å ta over skiftet ditt?");

        e.preventDefault();
    });

    $("#yesButton").click(function() {
        $("#modalYesNo").modal("toggle");
        if (shiftType === 5) {
            //When a user is asked another co-worker about taking the shift
            $.post(sendUrl, function() {

            }).done(function() {
                alert("Du har sendt forespørsel om å bytte dette skiftet.");//TODO: fix a better "alert" or modal
            }).fail(function() {
                alert("Kunne ikke sende forespørsel om vaktbytte");
            });
        }
    });
});


/**
 * Created by Ingunn on 13.01.2017.
 */

$(document).ready(function() {


    var today = new Date();

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

                //if same date    TODO:endre dags dato til annet senere
                if (startTime.getFullYear() === endTime.getFullYear() && startTime.getMonth() === endTime.getMonth() && startTime.getDate() === endTime.getDate()) {
                    diff = hourTo + minToPros - hourFrom + minFromPros; // tiltid - starttid
                    lengde = hourFrom + minFromPros;

                } else if (startTime.getFullYear() === date.getFullYear() && startTime.getMonth() === date.getMonth() && startTime.getDate() === date.getDate()) {
                    //hvis vakt starter på valgt dato og går til ny dag
                    diff = 24.0 - hourFrom + minFromPros;
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
                } else if(jArray[i].user_model.username === $("#username").html()) { //hvis innlogget bruker
                    classKomp = "self";
                } else {
                    classKomp = "worker";
                }
                var un;
                var navn;

                var tid = 'Start: ' + startTime.getFullYear() + '/' + (startTime.getMonth() + 1) + '/' + startTime.getDate() + ' ' + hourFrom + ':' + minFrom + '<br/>' +
                    'Slutt: ' + endTime.getFullYear() + '/' + (endTime.getMonth() + 1) + '/' + endTime.getDate() + ' ' + hourTo + ':' + minTo;
                if(jArray[i].user_model == null || jArray[i].user_model === undefined || !(jArray[i].user_model)) {
                    un = "";
                    navn = "";
                } else {
                    un = jArray[i].user_model.username;
                    navn = jArray[i].user_model.first_name + " " + jArray[i].user_model.last_name;
                }


                var vakt = '<div class="shift-box ' + classKomp + '" style="left: calc(11.11% + ((11.11%/3)*' + lengde + ')); width: calc((11.11%/3)*' + diff + ' - 1px);"><p class="tidtidtid">' + tidtid +
                    '</p><p class="tidLagring" style="display: none;">' + tid + '</p><p class="unLagring" style="display: none;">' + un + '</p><p class="navnLagring" style="display: none;">' + navn + '</p></div>';

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

            $("#datedate").html(date.getDate() + ". " + monthNames[date.getMonth()] + " " + date.getFullYear());

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
                $(".modal-title").html(text);
                var tid = $(".tidLagring", this).html();
                $("#tidsviser").html(tid);
            });

            $(".self").click(function () {
                $("#modalShift").modal("show");
                $("#modalOwn").css("display", "inline");
                $("#modalOther").css("display", "none");
                $("#modalFree").css("display", "none");

                var text = $(".tidtidtid", this).html();
                $(".modal-title").html(text);
                var tid = $(".tidLagring", this).html();
                $("#tidsviser").html(tid);
                var navn = $(".navnLagring", this).html();
                $("#ansatt").html(navn);


            });
            $(".worker").click(function () {
                $("#modalShift").modal("show");
                $("#modalOther").css("display", "inline");
                $("#modalOwn").css("display", "none");
                $("#modalFree").css("display", "none");
                var text = $(".tidtidtid", this).html();
                $(".modal-title").html(text);

                var tid = $(".tidLagring", this).html();
                var navn = $(".navnLagring", this).html();
                $("#tidsviser").html(tid);
                $("#ansatt").html(navn);

            });

            $("#shiftChange").click(function () {
                if ($("#shiftSelect").css("display") === "none") {
                    $("#shiftSelect").css("display", "inline");
                } else {
                    $("#shiftSelect").css("display", "none");
                }
            });

            $(".timedisplay").click(function () {
                $("#infoTime").modal("show");
                var text = $(this).html();
                $("#titleTime").html(text);
            });

            $("#date").on("changeDate", function () {
                $("#selectDay").css("display", "none");
                $("#selectDay").css("display", "inline");
            });
        });

    };

    /* Function for adding and subtracting days to a javascript date object */
    function addDays(date, days) {
        return new Date(date.getTime() + days*24*60*60*1000); //24*60*60*60*1000 is milliseconds in a day
    }

    function borderActiveWeek() {} //Må være tom function

    plotShifts(today);
    var count = today;

    $("#dayBack").click(function() {
        $("#superDiv").empty();
        count = addDays(count, -1);
        plotShifts(count);
    });
    $("#dayForth").click(function() {
        $("#superDiv").empty();
        count = addDays(count, 1);
        plotShifts(count);
    });


    $(".cell-cal").hover(function() {
        if($(this).hasClass("inactive-month")) {
            $(this).toggleClass("hover-adjust hover-adjust-inactive");
        } else if($(this).hasClass("today")) {
            $(this).toggleClass("hover-adjust hover-adjust-today");
        } else {
            $(this).toggleClass("hover-adjust");
        }
    });

    $(".cell-cal").click(function() {
        var monthYearArray = $(this).children(".month-year").html().split(" ");
        var dagIkkeArray = $(this).children(".display-day").html();

        var datoo = new Date(monthYearArray[0], monthYearArray[1] , dagIkkeArray);

        $("#superDiv").empty();
        plotShifts(datoo);
        count = datoo;
        $("#calendarModal").modal("toggle");
        $(".cell-cal").removeClass("active-day active-week-left active-week-middle active-week-right");
        $(this).addClass("active-day");

    }); //TODO: BORDER MARKERING PÅ VALGT DATO

    $(".modal-title-title").html("Velg dato");
});
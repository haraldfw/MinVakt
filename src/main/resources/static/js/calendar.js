/**
 * Created by Ingunn on 24.01.2017.
 */

var thisDay = new Date();
var weeks = ["week1", "week2", "week3", "week4", "week5", "week6"];
var months = ["Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"];
var backDays = [6, 0, 1, 2, 3, 4, 5];

/* Function for adding and subtracting days to a javascript date object */
function addDays(date, days) {
    return new Date(date.getTime() + days*24*60*60*1000); //24*60*60*60*1000 is milliseconds in a day
}

var month = thisDay.getMonth();
var year = thisDay.getFullYear();
var firstDay = new Date(year, month, 1);

/* Function for plotting days in calendar. startDay: for å finne måned som skal plottes. startDay2: for å sette aktiv dag/uke når måned skiftes i calendar */
function plotDays(startDay, startDay2) {
    $("#monthYear").html(months[month] + " " + year);
    var firstCalendarDay = addDays(firstDay, -(backDays[firstDay.getDay()]));

    for (var i = 0; i < weeks.length; i++) {

        $("#" + weeks[i] + " .weekNr").html(firstCalendarDay.getWeek());
        for (var j = 2; j < 9; j++) {//8 children, ikke nr 1
            $("#" + weeks[i] + " td:nth-child(" + j + ")").html("<p class='display-day'>" + firstCalendarDay.getDate() + "</p>" + "<p class='month-year' style='display: none'>" + firstCalendarDay.getFullYear() + " " + firstCalendarDay.getMonth() + "</p>");
            if (firstCalendarDay.getDate() === thisDay.getDate() && firstCalendarDay.getMonth() === thisDay.getMonth() && firstCalendarDay.getFullYear() === thisDay.getFullYear()) {
                $("#" + weeks[i] + " td:nth-child(" + j + ")").addClass("today");
            }
            if (firstCalendarDay.getDate() === startDay2.getDate() && firstCalendarDay.getMonth() === startDay2.getMonth() && firstCalendarDay.getFullYear() === startDay2.getFullYear()) {
                $("#" + weeks[i] + " td:nth-child(" + j + ")").addClass("active-day");
                $("#" + weeks[i] + " td:nth-child(2)").addClass("active-week-left active-week-middle");
                for (var k = 3; k < 8; k++) { //fiks på bedre måte
                    $("#" + weeks[i] + " td:nth-child(" + k + ")").addClass("active-week-middle");
                }
                $("#" + weeks[i] + " td:nth-child(8)").addClass("active-week-right active-week-middle");
            }

            if (firstCalendarDay.getMonth() != startDay.getMonth()) {
                $("#" + weeks[i] + " td:nth-child(" + j + ")").addClass("inactive-month");
            }
            firstCalendarDay = addDays(firstCalendarDay, 1);
        }
    }
}

/* Function for å få ukenummer*/
Date.prototype.getWeek = function () {
    var target = new Date(this.valueOf());
    var dayNr = (this.getDay() + 6) % 7;
    target.setDate(target.getDate() - dayNr + 3);
    var firstThursday = target.valueOf();
    target.setMonth(0, 1);
    if (target.getDay() != 4) {
        target.setMonth(0, 1 + ((4 - target.getDay()) + 7) % 7);
    }
    return 1 + Math.ceil((firstThursday - target) / 604800000); // 604800000 = 7 * 24 * 3600 * 1000
};

var theActive; //aktiv dag/uke

$(document).ready(function() {
    plotDays(thisDay, thisDay); //dager blir satt for nåværende måned

    /* Change month */
    function newMonth(way) {

        if($(".cell-cal").hasClass("active-day")) {
            var monthYearArray = $(".active-day").children(".month-year").html().split(" ");
            var dagIkkeArray = $(".active-day").children(".display-day").html();
            var year1 = monthYearArray[0];
            var month1 = monthYearArray[1];

            theActive = new Date(year1, month1, dagIkkeArray);
        }

        if($(".cell-cal").hasClass("active-week-left")) {
            var monthYearArray = $(".active-week-left").children(".month-year").html().split(" ");
            var dagIkkeArray = $(".active-week-left").children(".display-day").html();
            var year1 = monthYearArray[0];
            var month1 = monthYearArray[1];

            theActive = new Date(year1, month1, dagIkkeArray);

        }

        $(".cell-cal").removeClass("active-day active-week-left active-week-middle active-week-right today inactive-month");
        firstDay.setMonth(firstDay.getMonth() + way);
        month = firstDay.getMonth();
        year = firstDay.getFullYear();

        plotDays(firstDay, theActive);


            /* For schedule //TODO: ikke remove
             $(this).parent().children("td:nth-of-type(2)").addClass("active-week-left active-week-middle");
             for(var k = 3; k < 8; k++) { //fiks på bedre måte
             $(this).parent().children("td:nth-of-type(" + k + ")").addClass("active-week-middle");
             }
             $(this).parent().children("td:nth-of-type(8)").addClass("active-week-right active-week-middle");


            $("#" + weeks[i] + " td:nth-child(2)").addClass("active-week-left active-week-middle");
            for (var k = 3; k < 8; k++) { //fiks på bedre måte
                $("#" + weeks[i] + " td:nth-child(" + k + ")").addClass("active-week-middle");
            }
            $("#" + weeks[i] + " td:nth-child(8)").addClass("active-week-right active-week-middle");
             */
        //}
    }

    $(".left").click(function() {
        newMonth(-1);
    });
    $(".right").click(function() {
        newMonth(1);
    });
});
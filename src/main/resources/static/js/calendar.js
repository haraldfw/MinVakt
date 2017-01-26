/**
 * Created by Ingunn on 24.01.2017.
 */


$(document).ready(function() {

    //TODO: sjekk om today markeres hvis du er på neste måned og kan se today fra forrige måned


    Date.prototype.getWeek = function () {
        var target  = new Date(this.valueOf());
        var dayNr   = (this.getDay() + 6) % 7;
        target.setDate(target.getDate() - dayNr + 3);
        var firstThursday = target.valueOf();
        target.setMonth(0, 1);
        if (target.getDay() != 4) {
            target.setMonth(0, 1 + ((4 - target.getDay()) + 7) % 7);
        }
        return 1 + Math.ceil((firstThursday - target) / 604800000); // 604800000 = 7 * 24 * 3600 * 1000
    };

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

    function plotDays(startDay) {
        $("#monthYear").html(months[month] + " " + year);
        var firstCalendarDay = addDays(firstDay, -(backDays[firstDay.getDay()]));

        for(var i = 0; i < weeks.length; i++) {

            $("#" + weeks[i] + " .weekNr").html(firstCalendarDay.getWeek());
            for(var j = 2; j < 9; j++) {//8 children, ikke nr 1
                $("#" + weeks[i] + " td:nth-child(" + j + ")").html(firstCalendarDay.getDate());
                if(firstCalendarDay.getDate() === thisDay.getDate() && firstCalendarDay.getMonth() === thisDay.getMonth() && firstCalendarDay.getFullYear() === thisDay.getFullYear()) {
                    $("#" + weeks[i] + " td:nth-child(" + j + ")").addClass("today");
                }
                if(firstCalendarDay.getMonth() != startDay.getMonth()) {
                    $("#" + weeks[i] + " td:nth-child(" + j + ")").addClass("inactive-month");
                }
                
                firstCalendarDay = addDays(firstCalendarDay, 1);
            }
        }
    }

    plotDays(thisDay)

    /* Change month */
    function newMonth(way) {
        $(".cell-cal").removeClass("today inactive-month");
        firstDay.setMonth(firstDay.getMonth() + way);
        month = firstDay.getMonth();
        year = firstDay.getFullYear();
        plotDays(firstDay);
    }

    $(".left").click(function() {
        newMonth(-1);
    });
    $(".right").click(function() {
        newMonth(1);

    });

    $("tr td:first-child").each(function() {
       //alert($(this).html());
    });
});
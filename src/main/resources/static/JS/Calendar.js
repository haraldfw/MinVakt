/**
 * Created by Ingunn on 24.01.2017.
 */


$(document).ready(function() {
    $(".cell").hover(function() {
        $(this).css("background-color", "#B1B1B1").css("cursor", "pointer"); //    filter: brightness(85%);//    filter: brightness(85%);"background-color", "#969696"("filter", "brightness(115%)").css("cursor", "pointer");
        $(this).siblings(".cell").css("background-color", "#B1B1B1").css("cursor", "pointer");

    }, function() {
        $(this).css("background-color", "transparent").css("cursor", "auto");
        $(this).siblings(".cell").css("background-color", "transparent").css("cursor", "auto");
    });

    function dayOfYear(now) { //var now = new Date();
        var start = new Date(now.getFullYear(), 0, 0);
        var diff = now - start;
        var oneDay = 1000 * 60 * 60 * 24;
        return Math.floor(diff / oneDay); //TODO: test om virker
    }

    var thisDay = new Date();
    var weeks = ["week1", "week2", "week3", "week4", "week5", "week6"];
    var months = ["Januar", "Februar", "Mars", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Desember"];
    var backDays = [6, 0, 1, 2, 3, 4, 5];

    /* Function for adding and subtracting days to a javascript date object */
    function addDays(date, days) {
        return new Date(date.getTime() + days*24*60*60*1000); //24*60*60*60*1000 is milliseconds in a day
    }

    function plotDays(startDay) {

        var month = thisDay.getMonth();
        var year = thisDay.getFullYear();
        $("#monthYear").html(months[month] + " " + year);
        var firstDay = new Date(year, month, 1);
        var firstCalendarDay = addDays(firstDay, -(backDays[firstDay.getDay()]));

        for(var i = 0; i < weeks.length; i++) {
            for(var j = 2; j < 9; j++) {//8 children, ikke nr 1
                $("#" + weeks[i] + " td:nth-child(" + j + ")").html(firstCalendarDay.getDate());
                firstCalendarDay = addDays(firstCalendarDay, 1);

            }
        }
    }

    plotDays(thisDay)


});
/**
 * Created by Ingunn on 24.01.2017.
 */


$(document).ready(function() {
    $(".cell").hover(function() {
        $(this).css("background-color", "#969696").css("cursor", "pointer");
        $(this).siblings(".cell").css("background-color", "#969696").css("cursor", "pointer");

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
    //dagnr: td:nth-child(2)

    for(var i = 0; i < weeks.length; i++) {

        for(var j = 2; j < 9; j++) {//8 children, ikke nr 1


        }

    }


});
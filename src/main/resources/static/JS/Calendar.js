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



});
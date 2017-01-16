/**
 * Created by Ingunn on 09.01.2017.
 */


$(document).ready(function() {
    $("#close").click(function() {
        if($(this).hasClass("ion-close")) {
            $("#nav, #nav333").animate({ //TODO: animere i CSS?
                left: "-15%"
            }, 400);
            $("#close").removeClass("ion-close").addClass("ion-navicon-round");
            $("#navbar-top").addClass("navbar-top-ute").removeClass("navbar-top-inne");
        } else {
            $("#nav, #nav333").animate({
                left: "0%"
            }, 400);
            $("#close").removeClass("ion-navicon-round").addClass("ion-close");
            $("#navbar-top").addClass("navbar-top-inne").removeClass("navbar-top-ute");
        }

    });
});
/**
 * Created by Ingunn on 09.01.2017.
 */

$(document).ready(function() {
    $(".close-close").click(function() {
        if($(this).hasClass("ion-close")) {
            $(".close-close").removeClass("ion-close").addClass("ion-navicon-round");
            $("#navbar-side").addClass("navbar-side-ute").removeClass("navbar-side-inne");
            $("#navbar-top").addClass("navbar-top-ute").removeClass("navbar-top-inne");
        } else {
            $(".close-close").removeClass("ion-navicon-round").addClass("ion-close");
            $("#navbar-side").addClass("navbar-side-inne").removeClass("navbar-side-ute");
            $("#navbar-top").addClass("navbar-top-inne").removeClass("navbar-top-ute");
        }
    });
});
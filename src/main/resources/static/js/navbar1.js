/**
 * Created by Ingunn on 09.01.2017.
 */

$(document).ready(function () {
    $(".close-close, .close-iconburger").click(function () {
        if ($(this).hasClass("ion-close")) {
            $(".close-close").removeClass("ion-close").addClass("ion-navicon-round");
            $(".close-iconburger").removeClass("ion-close").addClass("ion-navicon-round");
            $("#navbar-side").addClass("navbar-side-ute").removeClass("navbar-side-inne");
            $("#navbar-top").addClass("navbar-top-ute").removeClass("navbar-top-inne");
        } else {
            $(".close-iconburger").removeClass("ion-navicon-round").addClass("ion-close");
            $(".close-close").removeClass("ion-navicon-round").addClass("ion-close");
            $("#navbar-side").addClass("navbar-side-inne").removeClass("navbar-side-ute");
            $("#navbar-top").addClass("navbar-top-inne").removeClass("navbar-top-ute");
        }
    });

    /*$(window).click(function(e) {
     //alert("test");

     if (e.target.id === "navbar-side") {
     alert("hei");
     } else {
     if ($("#navbar-side").hasClass("navbar-side-inne")) {
     alert("inne remove");
     }
     }

     $(".close-close").removeClass("ion-close").addClass("ion-navicon-round");
     $(".close-iconburger").removeClass("ion-close").addClass("ion-navicon-round");
     $("#navbar-side").addClass("navbar-side-ute").removeClass("navbar-side-inne");
     $("#navbar-top").addClass("navbar-top-ute").removeClass("navbar-top-inne");
     });

     $(document).click(function() {

     //event.stopPropagation();
     });*/

    $(".noti-form").submit((event) => {
        event.preventDefault();

        let url = $(event.target).attr("action");
        postUrl(url);
    });
});


function postUrl(url) {
    $.ajax({
        type: "post",
        url: url
    }).done(data => {
        //TODO: better refresh notifications...
        location.reload();
    }).fail(err => {
        console.error("Failed to post!");
        console.error(err);
    });
}
/**
 * Created by Ingunn on 13.01.2017.
 */

/*
left: calc(11.11% + (11.11%)/3 + ((11.11%)/3)*16);
width: calc(((11.11%)/3)*8 - calc((11.11%)/3) - 1px);
TODO: ikke fjern
*/


$(document).ready(function() {

    $("#response-button").click(function() {
        var today = new Date();


        $.get("api/shift/" + today.getFullYear() + "/" + today.getMonth() + "/" + today.getDate() + "", function() {

        })
            .done(function(data) {
                var jArray = data;
                for(var i = 0; i < jArray.length; i++) {

                    var startTime = new Date(jArray[i].start_time);
                    var endTime = new Date(jArray[i].end_time);

                    var minFrom = startTime.getMinutes();
                    var hourFrom = startTime.getHours();
                    var minTo = endTime.getMinutes();
                    var hourTo = endTime.getHours();
                    var minFromPros = minFrom/60;
                    var minToPros = minTo/60;
                    var diff = 0; //skiftlengde
                    var lengde = 0; //start pkt

                    //if same date    TODO:endre dags dato til annet senere
                    if(startTime.getFullYear() === endTime.getFullYear() && startTime.getMonth() === endTime.getMonth() && startTime.getDate() === endTime.getDate()) {
                        diff = hourTo+minToPros-hourFrom+minFromPros; // tiltid - starttid
                        lengde = hourFrom+minFromPros;

                    } else if(startTime.getFullYear() === today.getFullYear() && startTime.getMonth() === today.getMonth() && startTime.getDate() === today.getDate()) {
                        //hvis vakt starter på valgt dato og går til ny dag
                        diff = 24.0-hourFrom+minFromPros;
                        lengde = hourFrom+minFromPros;

                    } else if(endTime.getFullYear() === today.getFullYear() && endTime.getMonth() === today.getMonth() && endTime.getDate() === today.getDate()) {
                        //hvis vakt slutter på valgt dato
                        diff = hourTo+minToPros-0; //ikke fjern 0
                        //lengde blir lik 0

                    } else {
                        //hvis vakt går igjennom hele dagen
                        diff = 24;
                        //lengde blir lik 0
                    }

                    var kompArray = jArray[i].competences;
                    kompArray.sort(); //Sorterer kompetanser i alfabetisk rekkefølge TODO: sjekk om dette virker, virker kanskje vet ikke

                    var komp = kompArray[0].name;
                    for(var j = 1; j < kompArray.length; j++) {
                        komp += (", " + kompArray[j].name);
                    }

                    if(hourFrom < 10) {
                        hourFrom = "0" + hourFrom;
                    }
                    if(hourTo < 10) {
                        hourTo = "0" + hourTo;
                    }
                    if(minFrom % 10 === 0) {
                        minFrom += "0";
                    }
                    if(minTo % 10 === 0) {
                        minTo += "0";
                    }
                    var tidtid = hourFrom + ":" + minFrom + " - " + hourTo + ":" + minTo;
                    var classKomp = "worker";

                    var tid = 'Start: ' + startTime.getFullYear() + '/' + startTime.getMonth()+1 + '/' + startTime.getDate() + ' ' + hourFrom + ':' + minFrom + '<br/>' +
                    'Slutt: ' + endTime.getFullYear() + '/' + endTime.getMonth()+1 + '/' + endTime.getDate() + ' ' + hourTo + ':' + minTo;
                    var un = jArray[i].user_model.username;
                    var navn = jArray[i].user_model.first_name + " " + jArray[i].user_model.last_name;

                    var vakt = '<div class="shift-box ' + classKomp + '" style="left: calc(11.11% + ((11.11%/3)*' + lengde + ')); width: calc((11.11%/3)*' + diff + ' - 1px);"><p class="tidtidtid">' + tidtid +
                        '</p><p class="tidLagring" style="display: none;">' + tid + '</p><p class="unLagring" style="display: none;">' + un + '</p><p class="navnLagring" style="display: none;">' + navn + '</p></div>';

                    var vaktRad = '<div class="rad">' +
                        '<div class="common-cell position-id">a1</div>' +
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
                        '</div>'+
                        '<div class="drop">' +
                        vaktRad +
                        '</div>' +
                        '</div>';



                    if(i === 0) {
                        $("#superDiv").append(kompetansegruppe);
                    } else {
                        var bool = false;
                        $(".kompetanse-cell.testing").filter(function(index) {
                            if($(this).text() === komp) {
                                $($(this).parent().siblings(".drop")).append(vaktRad);
                                bool = true;
                            }
                        });
                        if(!(bool)) {
                            $("#superDiv").append(kompetansegruppe);
                            bool = false;
                        }
                    }

                }


                /* Andre functions */

                $(".testing").click(function() {
                    if(($(this).parent().siblings(".drop").css("display") == "none")) {
                        $(this).parent().siblings(".drop").css("display", "inline");
                    } else {
                        $(this).parent().siblings(".drop").css("display", "none");
                    }
                });

                $(".no-worker").click(function() {
                    $("#modalShift").modal("show");
                    $("#modalFree").css("display", "inline");
                    $("#modalOther").css("display", "none");
                    $("#modalOwn").css("display", "none");
                });


                $(".self").click(function() {
                    $("#modalShift").modal("show");
                    $("#modalOwn").css("display", "inline");
                    $("#modalOther").css("display", "none");
                    $("#modalFree").css("display", "none");

                });
                $(".worker").click(function() {
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

                $("#shiftChange").click(function() {
                    if ($("#shiftSelect").css("display") === "none") {
                        $("#shiftSelect").css("display", "inline");
                    } else {
                        $("#shiftSelect").css("display", "none");
                    }
                });

                $(".timedisplay").click(function() {
                    $("#infoTime").modal("show");
                    var text = $(this).html();
                    $("#titleTime").html(text);
                });

                $("#date").on("changeDate", function() {
                    $("#selectDay").css("display", "none");
                    $("#selectDay").css("display", "inline");
                });
            });
    });
    /*
     var diffMin = 0;
     var lengde = diffHour + ;

     if(diff % 100 === 0) {
     diff /= 100;
     } else {
     diff %= 100;

     }*/
    /* TODO: ikke fjern kommentarer
     $("#testtest").css("left", "calc(11.11% + ((11.11%/3)*" + lengde +"))");
     $("#testtest").css("width", "calc((11.11%/3)*" + diff + " - 1px)");
     $("#testtest").addClass("worker");
     if(diff % 100 === 0) {
     diff /= 100;
     } else {
     diff %= 100;

     }
     /*
     $("#testing2, #testingg2").click(function() {
     if($("#test2").css("display") == "none") {
     $("#test2").css("display", "inline");
     } else {
     $("#test2").css("display", "none");
     }

     });*/
});
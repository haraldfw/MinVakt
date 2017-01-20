/**
 * Created by Ingunn on 13.01.2017.
 */

/*
left: calc(11.11% + (11.11%)/3 + ((11.11%)/3)*16);
width: calc(((11.11%)/3)*8 - calc((11.11%)/3) - 1px);
*/


$(document).ready(function() {

    $("#response-button").click(function() {
        var today = new Date();


        $.get("api/shift/" + today.getFullYear() + "/" + today.getMonth() + "/" + today.getDate() + "", function() {
            alert("asdasd");

        })
            .done(function(data) {
                var jarray = data;

                for(var i = 0; i < jarray.length; i++) {
                    //12.30 - 14.45
                    var startTime = new Date(jarray[i].start_time);
                    var endTime = new Date(jarray[i].end_time);

                    var minFrom = startTime.getMinutes();
                    var hourFrom = startTime.getHours();
                    var minTo = endTime.getMinutes();
                    var hourTo = endTime.getHours();
                    var minFromPros = minFrom/60;
                    var minToPros = minTo/60;

                    alert(hourTo);
                    alert(minToPros);
                    alert(hourFrom);
                    alert(minFromPros);
                    alert("ny");

                    var diff = hourTo+minToPros-hourFrom+minFromPros; //7h

                    var lengde = hourFrom+minFromPros;
                    var komp = "test2";
                    var tidtid = "test";
                    var classKomp = "worker";
                    var vakt = '<div class="shift-box ' + classKomp + '" style="left: calc(11.11% + ((11.11%/3)*' + lengde + ')); width: calc((11.11%/3)*' + diff + ' - 1px);">' + tidtid + '</div>';

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

                    var kompetansegruppe = '<div id="kompetanseGruppe">' +
                        '<div class="rad kompetanse-rad">' +
                        '<div id="testingg" class="dropdown-cell testing">' +
                        '<i id="close" class="ion-navicon-round burger-style"></i>' +
                        '</div>' +
                        '<div id="testing" class="kompetanse-cell testing">' +
                        komp +
                        '</div>' +
                        '</div>'+
                        '<div id="test" class="test">' +
                        vaktRad +
                        '</div>' +
                        '</div>';

                    $("#superDiv").append(kompetansegruppe);

                }



                /* Andre functions */

                $(".testing, #testing, #testingg").click(function() {
                    if(($(this).parent().siblings(".test").css("display") == "none")) {
                        $(this).parent().siblings(".test").css("display", "inline");
                    } else {
                        $(this).parent().siblings(".test").css("display", "none");
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
    /*
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
/**
 * Created by Kenan on 1/17/2017.
 */
$(document).ready(() => {

    $('#inputFrom').datetimepicker();
    $('#inputTo').datetimepicker({
        useCurrent: false
    });
    $("#inputFrom").on("dp.change", function (e) {
        $('#inputTo').data("DateTimePicker").minDate(e.date);
    });
    $("#inputTo").on("dp.change", function (e) {
        $('#inputFrom').data("DateTimePicker").maxDate(e.date);
    });

});
/**
 * Created by Kenan on 1/17/2017.
 */
$(document).ready(() => {
    $('#inputFrom').datetimepicker();
    $('#inputTil').datetimepicker({
        useCurrent: false
    });
    $("#inputFrom").on("dp.change", function (e) {
        $('#inputTil').data("DateTimePicker").minDate(e.date);
    });
    $("#inputTil").on("dp.change", function (e) {
        $('#inputFrom').data("DateTimePicker").maxDate(e.date);
    });
    // $("#availability").form(json => {
    //     console.log(json)
    // })
});
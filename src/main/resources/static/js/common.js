/**
 * Created by alan on 17/01/2017.
 */


$(document).ready(() => {
    $("#menuBtn").click(function () {
        $("#menuOverlay").slideToggle(200);
    });
});

$.fn.form = function (callback) {
    this.submit(event => {
        event.preventDefault();

        callback(this.serializeJSON());
        return false;
    });
};
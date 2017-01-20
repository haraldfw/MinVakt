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

        callback(this.serializeObject());
        return false;
    });
};

// https://stackoverflow.com/a/1186309
$.fn.serializeObject = function () {
    let o = {};
    let a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    return o;
};
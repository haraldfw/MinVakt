/**
 * Created by alan on 17/01/2017.
 */

$(document).ready(() => {
    $("#menuBtn").click(function () {
        $("#menuOverlay").slideToggle(200);
    });
});

const entityMap = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;',
    '/': '&#x2F;',
    '`': '&#x60;',
    '=': '&#x3D;'
};

function escapeHtml(string) {
    //https://stackoverflow.com/a/12034334
    return String(string).replace(/[&<>"'`=\/]/g, function (s) {
        return entityMap[s];
    });
}
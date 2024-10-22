/**
 * Created by HAWI on 23.01.2017.
 */

function uploadImage(form, redirectUrl, fileId) {
    let file = document.getElementById(fileId).files[0];
    if (!file) {
        return false;
    }

    form.elements["content_type"].value = file.type;

    let reader = new FileReader();

    reader.onload = function (readerEvt) {
        let binaryString = readerEvt.target.result;
        form.elements["b64_content"].value = btoa(binaryString);
        return jsonSubmit(form, redirectUrl);
    };

    reader.readAsBinaryString(file);
    return false;
}

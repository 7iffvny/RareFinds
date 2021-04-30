// validates text only
function Validate(txt) {
    txt.value = txt.value.replace(/[^a-zA-Z-'\n\r.]+/g, '');
}

// validate email
function email_validate(email) {
var regMail = /^([_a-zA-Z0-9-]+)(\.[_a-zA-Z0-9-]+)*@([a-zA-Z0-9-]+\.)+([a-zA-Z]{2,3})$/;
    if (regMail.test(email) == false) {
    document.getElementById("status").innerHTML = "<span class='warning'>Email address is not valid.</span>"; 
} else {
    document.getElementById("status").innerHTML	= "<span class='valid'></span>";	
    }
}
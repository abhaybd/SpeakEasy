// Send the message to the TTS
function send() {
    let voiceName = document.getElementById("voiceSelector").value;
    let messageBox = document.getElementById("message");
    let message = messageBox.value;
    let errorMessage = document.getElementById("error-message");
    messageBox.value = "";

    // Only send message if it contains alphanumeric characters
    if (/[A-Za-z0-9]/.test(message)) {
        sayText(voiceName, message);
        errorMessage.style.color = "transparent";
    } else {
        errorMessage.style.color = "red";
    }
}

getVoices().then(voices => {
    if (voices !== null) {
        let dropdown = document.getElementById("voiceSelector");
        voices.sort((a, b) => {
            if (a.gender !== b.gender) {
                return a.gender > b.gender ? 1 : -1;
            } else {
                return a.name > b.name ? 1 : -1;
            }
        });
        voices.forEach((voice, i) => {
            let gender = voice.gender.toLowerCase();
            gender = gender.charAt(0).toUpperCase() + gender.substring(1);
            let child = document.createElement("option");
            child.setAttribute("value", voice.name);
            if (i === 0) {
                child.setAttribute("selected", "");
            }
            child.appendChild(document.createTextNode(`Voice ${i + 1} - ${gender}`));
            dropdown.appendChild(child);
        });

        dropdown.onchange = function () {
            playSample(this.value);
        }
    } else {
        alert("Error contacting server!");
    }
});

document.getElementById("submit").onclick = send;

// Send when enter is pressed
document.onkeydown = function (e) {
    if (e.key === "Enter" && document.activeElement === document.getElementById("message")) {
        e.preventDefault();
        send();
    }
}

(function () {
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

    let submitButton = document.getElementById("submit");
    submitButton.onclick = function () {
        let voiceName = document.getElementById("voiceSelector").value;
        let messageBox = document.getElementById("message");
        let message = messageBox.value;
        messageBox.value = "";
        sayText(voiceName, message);
    }
})();
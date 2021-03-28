const BACKEND_URL = "http://localhost:4567";

async function getVoices() {
    let resp = await fetch(BACKEND_URL + "/voices");
    if (!resp.ok) {
        return null;
    } else {
        return await resp.json();
    }
}

function playSample(voiceName) {
    fetch(BACKEND_URL + "/sample", {
        method: "post",
        body: JSON.stringify({voiceName: voiceName})
    }).then(resp => {
        if (!resp.ok) {
            alert("There was an error contacting the server!");
        }
    });
}

function sayText(voiceName, userName, message) {
    let data = {
        name: userName,
        voiceName: voiceName,
        message: message
    };

    fetch(BACKEND_URL + "/speak", {
        method: "post",
        body: JSON.stringify(data)
    }).then(resp => {
        if (!resp.ok) {
            alert("There was an error contacting the server!");
        }
    });
}

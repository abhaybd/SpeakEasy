async function getVoices() {
    let resp = await fetch("/voices");
    if (!resp.ok) {
        return null;
    } else {
        return await resp.json();
    }
}

function playSample(voiceName) {
    fetch("/sample", {
        method: "post",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(voiceName)
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

    fetch("/speak", {
        method: "post",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    }).then(resp => {
        if (!resp.ok) {
            alert("There was an error contacting the server!");
        }
    });
}

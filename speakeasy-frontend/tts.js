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
        body: JSON.stringify({voiceName: voiceName})
    }).then(resp => {
        if (!resp.ok) {
            alert("There was an error contacting the server!");
        }
    });
}

function sayText(voiceName, message) {
    let data = {
        voiceName: voiceName,
        message: message
    };

    fetch("/speak", {
        method: "post",
        body: JSON.stringify(data)
    }).then(resp => {
        if (!resp.ok) {
            alert("There was an error contacting the server!");
        }
    });
}

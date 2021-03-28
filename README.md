![SpeakEasy Logo](assets/speakeasy-small.png)

# SpeakEasy

Submission to HooHacks hackathon

## Important

To run this, you'll need to have [VB-Audio Cable](https://vb-audio.com/Cable/) installed. This handles piping the audio into your call program.

## Installation

1. As mentioned above, you must first install [VB-Audio Cable](https://vb-audio.com/Cable/)
2. Download the latest version of the executable JAR file of SpeakEasy from [the releases page](https://github.com/abhaybd/SpeakEasy/releases)
3. Set up Google Cloud Platform (you'll have to use your own API keys)
   1. Create a new GCP project
   2. Link it to a billing account as appropriate
   3. Enable the Text-To-Speech service
   4. Create a service account for Text-To-Speech, with the appropriate permissions
   5. Download the key file for that service account
   6. Name the key file *exactly* `speakeasy-gcp-tts.key.json`
   7. Place the key file in the same directory as the downloaded JAR file

## Usage

1. Run the downloaded executable JAR file (with the key file in the same directory)
2. In your meeting program (Zoom, Discord, etc.) configure your microphone input device to be `CABLE Input (VB-Audio Virtual Cable)`
3. In the SpeakEasy window, type your message and press enter or click the submit button to speak your message into the meeting!

## Configuration

You can tweak SpeakEasy to your liking. In the SpeakEasy window, you can choose from among ten unique high quality voices.

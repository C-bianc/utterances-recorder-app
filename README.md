# Recording App Controller

This is a Java-based sound recording application with a simple user interface. The main component is the `RecordingAppController` class, which manages the recording process, audio file handling, and the integration with a CSV file to store the utterance statuses.

## Features
- **CSV Integration**: Load a CSV file containing utterances and update the statuses of recorded files.

![image|500](https://github.com/user-attachments/assets/b0fb04d5-1a2d-4bcd-afb5-ea2ade01a2c1)

- **Record Audio**: Start and stop audio recording for a specific utterance.
- **Play Audio**: Play back previously recorded audio files.
- **Delete Audio**: Delete recorded audio files.


## Setup

1. Clone the repository using the following:
   ```bash
   https://github.com/C-bianc/utterances-recorder-app.git
   ```

   or

   ```bash
   git@github.com:C-bianc/utterances-recorder-app.git
   ```

## Improvements
Ideally, one could optimize the updating/loading process of the utterances.
- Left panel not implemented yet. It should contain the list of already recorded utterances and the user should be able to click and visualize the item.
- Add sound bar for visualizing the audio.
- Add file opener and enable to select column data for displaying the utterances. Right now it is done manually.


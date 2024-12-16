# Recording App Controller

This is a Java-based sound recording application with a simple user interface. The main component is the `RecordingAppController` class, which manages the recording process, audio file handling, and the integration with a CSV file to store the utterance statuses.

## Features
- **CSV Integration**: Load a CSV file containing utterances and update the statuses of recorded files.

<img src="https://github.com/user-attachments/assets/b0fb04d5-1a2d-4bcd-afb5-ea2ade01a2c1" width="300">

- **Record Audio**: Start and stop audio recording for a specific utterance.
<img src="https://github.com/user-attachments/assets/69c6a3bc-a2a2-4f76-86a9-ae64d58e8875" width="300">


- **Play Audio**: Play back previously recorded audio files.
- **Delete Audio**: Delete recorded audio files.
<img src="https://github.com/user-attachments/assets/baeaa050-7197-49b4-a523-1eb01b6049f1" width="300">

## Setup

1. Clone the repository using the following:
   ```bash
   git clone https://github.com/C-bianc/utterances-recorder-app.git
   ```

   or

   ```bash
   git clone git@github.com:C-bianc/utterances-recorder-app.git
   ```

## Improvements
Ideally, one could optimize the updating/loading process of the utterances.
- Left panel not implemented yet. It should contain the list of already recorded utterances and the user should be able to click and visualize the item.
- Add sound bar for visualizing the audio.
- Add file opener and enable to select column data for displaying the utterances. Right now it is done manually.


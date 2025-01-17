# Image Captioning on Android App

This project aims to assist visually impaired individuals by providing an Android application that generates descriptive captions for images captured through the device's camera. The app utilizes a pre-trained image captioning model to interpret and describe the surrounding environment, enhancing users' awareness of their surroundings.

---

## Project Overview

The application comprises two main components:

1. **Model Conversion**:
   - **Objective**: Convert a pre-trained image captioning model into a format suitable for mobile deployment.
   - **Process**:
     - Obtain a pre-trained model, such as the Show and Tell model.
     - Freeze the model to convert the weights from a `.ckpt` file to a `.pb` file.
     - Convert the `.pb` file to a TensorFlow Lite (`.tflite`) model for compatibility with Android devices.

2. **Android Application**:
   - **Objective**: Develop an Android app that utilizes the converted `.tflite` model to perform image captioning on-device.
   - **Features**:
     - Capture images using the device's camera.
     - Process the image through the TensorFlow Lite model to generate descriptive captions.
     - Provide audio feedback of the generated captions to the user.

---

## Getting Started

To set up and run the project locally, follow these steps:

### 1. Clone the Repository

```bash
git clone https://github.com/MahmoudKhaled007/Image-Captioning-on-Android-app.git
cd Image-Captioning-on-Android-app
```
### 2. Model Preparation
- **Download Pre-trained Model:**
    Obtain the pre-trained Show and Tell model from the official TensorFlow repository.
- **Freeze the Model:**
    Use TensorFlow to freeze the model, converting the .ckpt file to a .pb file.
- **Convert to TensorFlow Lite:**
    Utilize TensorFlow Lite Converter to transform the .pb file into a .tflite model.
### 3. Android Application Setup
- **Import Project:**
    Open Android Studio and import the project located in the AndroidApp directory.
- **Add TensorFlow Lite Model:**
    Place the .tflite model file into the assets directory of the Android project.
- **Build and Run:**
    Build the project and deploy it to an Android device running Android 5.0 (Lollipop) or higher.

## Usage
1- **Capturing Images**

Launch the application and use the camera feature to capture an image of the surrounding environment.

2- **Generating Captions**

The app processes the captured image through the TensorFlow Lite model to generate a descriptive caption.

3- **Audio Feedback**

The generated caption is read aloud to the user, providing auditory feedback about the environment.


## Acknowledgements
- **Pre-trained Model:**
    The Show and Tell model utilized in this project is sourced from TensorFlow's official models repository.
- **Android Application Development:**
    The Android app development is inspired by existing implementations of image captioning on mobile devices.

## Contributing
Contributions to enhance the functionality or performance of the application are welcome. Please fork the repository and submit a pull request with your proposed changes.

## License
This project is licensed under the Apache-2.0 License. See the LICENSE file for more details.

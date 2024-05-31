# Dynamsoft Document Normalizer samples for Android and iOS editions

This repository contains multiple samples that demonstrate how to use the [Dynamsoft Document Normalizer](https://www.dynamsoft.com/document-normalizer/docs/core/introduction) Android and iOS Editions.

- User Guide
  - [Android](https://www.dynamsoft.com/document-normalizer/docs/mobile/programming/android/user-guide.html)
  - [iOS](https://www.dynamsoft.com/document-normalizer/docs/mobile/programming/ios/user-guide.html)
- API Reference
  - [Android](https://www.dynamsoft.com/document-normalizer/docs/mobile/programming/android/api-reference/)
  - [iOS](https://www.dynamsoft.com/document-normalizer/docs/mobile/programming/ios/api-reference/)

## Requirements

### Android

- Supported OS: Android 5.0 (API Level 21) or higher.
- Supported ABI: **armeabi-v7a**, **arm64-v8a**, **x86** and **x86_64**.
- Development Environment: Android Studio 2022.2.1 or higher.

### iOS

- Supported OS: iOS 11 or higher (iOS 13 and higher recommended).
- Supported ABI: **arm64** and **x86_64**.
- Development Environment: Xcode 13.0 and above (Xcode 14.1+ recommended), CocoaPods 1.11.0+

## Samples

| Sample Name | Description | Programming Language(s) |
| ----------- | ----------- | ----------------------- |
| `HelloWorld` | This is a sample that illustrates the simplest way to detect quad from video streaming and perform image normalization with Dynamsoft Document Normalizer SDK and Dynamsoft Camera Enhancer SDK. | Java(Android)/Objective-C/Swift |

### How to build (For iOS Editions)

#### Include the Framework via CocoaPods

1. Enter the sample folder, install DBR SDK through `pod` command

    ```bash
    pod install
    ```

2. Open the generated file `[SampleName].xcworkspace`

#### Include the Framework Manually

1. Download the Dynamsoft Document Normalizer SDK from <a href="https://download2.dynamsoft.com/ddn/dynamsoft-document-normalizer-ios-2.2.1100.zip" target="_blank">Dynamsoft website</a>.

2. Drag and drop the `DynamsoftCaptureVisionRouter.xcframework`, `DynamsoftDocumentNormalizer.xcframework`, `DynamsoftCore.xcframework`, `DynamsoftImageProcessing.xcframework`, `DynamsoftLicense.xcframework`, `DynamsoftUtility.xcframework`, and `DynamsoftCameraEnhancer.xcframework` into your Xcode project. Make sure to check `Copy items if needed` and `Create groups` to copy the framework into your projects' folder.

3. Click on the project. Go to the `General --> Frameworks --> Libraries and Embedded Content`. Set the embed type to `Embed & Sign`.

## License

- If you want to use an offline license, please contact [Dynamsoft Support](https://www.dynamsoft.com/company/contact/)
- You can also request a 30-day trial license via the [Request a Trial License](https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=github&package=mobile) link.

## Contact Us

https://www.dynamsoft.com/company/contact/

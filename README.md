# Dynamsoft Document Normalizer samples for Android and iOS editions

This repository contains multiple samples that demonstrates how to use the [Dynamsoft Document Normalizer](https://www.dynamsoft.com/document-normalizer/docs/) Android and iOS Editions.

## Requirements

### Android

- Operating systems:
  - Supported OS: Android 5 or higher (Android 7 or higher recommended)
  - Supported ABI: armeabi-v7a, arm64-v8a, x86, x86_64
- Environment: Android Studio 3.4+

### iOS

- Operating systems:
  - iOS 9.0 and above.
- Environment: Xcode 7.1 - 11.5 and above.
- Recommended: macOS 10.15.4+, Xcode 11.5+, iOS 11+, CocoaPods 1.11.0

## Samples

| Sample Name | Description | Programming Language(s) |
| ----------- | ----------- | ----------------------- |
| `HelloWorld` | This is a sample that illustrates the simplest way to detect quad from video streaming with Dynamsoft Document Normalizer SDK and Dynamsoft Camera Enhancer SDK. | Java(Android)/Objective-C/Swift |

### How to build (For iOS Editions)

#### Include the Framework via CocoaPods

1. Enter the sample folder, install DBR SDK through `pod` command

    ```bash
    pod install
    ```

2. Open the generated file `[SampleName].xcworkspace`

#### Include the Framework Manually

1. Download the Dynamsoft Document Normalizer SDK from <a href="https://www.dynamsoft.com/document-normalizer/downloads/?utm_source=docs" target="_blank">Dynamsoft website</a>.

2. Drag and drop the `DynamsoftDocumentNormalizer.framework`, `DynamsoftCore.framewrok`, `DynamsoftImageProcessing.framework`, `DynamsoftIntermediaResult.framework` and `DynamsoftCameraEnhancer.framework` into your Xcode project. Make sure to check `Copy items if needed` and `Create groups` to copy the framework into your projects' folder.

3. Click on the project. Go to the `General --> Frameworks --> Libraries and Embedded Content`. Set the embed type to `Embed & Sign`.

## License

- If you want to use an offline license, please contact [Dynamsoft Support](https://www.dynamsoft.com/company/contact/)
- You can also request an extension for your trial license in the [customer portal](https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=github)

## Contact Us

https://www.dynamsoft.com/company/contact/

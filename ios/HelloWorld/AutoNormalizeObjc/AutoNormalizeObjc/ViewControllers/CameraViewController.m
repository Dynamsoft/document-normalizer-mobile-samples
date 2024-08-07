/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import "CameraViewController.h"

@interface CameraViewController ()<DSCapturedResultReceiver, DSLicenseVerificationListener>
{
    BOOL isNeedToQuadEdit;
}
@property (nonatomic, strong) DSCaptureVisionRouter *dcv;

@property (nonatomic, strong) DSCameraEnhancer *dce;

@property (nonatomic, strong) DSCameraView *cameraView;

@property (nonatomic, strong) UILabel *tipLabel;

@end

@implementation CameraViewController

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.dce open];
    [self.dcv startCapturing:DSPresetTemplateDetectAndNormalizeDocument completionHandler:^(BOOL isSuccess, NSError * _Nullable error) {
        if (!isSuccess && error != nil) {
            [self showResult:@"Error" message:error.localizedDescription completion:nil];
        }
    }];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.dce close];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor whiteColor];
    [self setLicense];
    [self configureDDN];
    [self addISA];
    [self setupUI];
    [self updateSettings];
}

- (void)setLicense {
    // Initialize the license.
    // The license string here is a trial license. Note that network connection is required for this license to work.
    // You can request an extension via the following link: https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=samples&package=ios
    [DSLicenseManager initLicense:@"DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9" verificationDelegate:self];
}

- (void)displayLicenseMessage:(NSString *)message {
    UILabel *label = [[UILabel alloc] init];
    label.text = message;
    label.textAlignment = NSTextAlignmentCenter;
    label.numberOfLines = 0;
    label.textColor = [UIColor redColor];
    label.translatesAutoresizingMaskIntoConstraints = NO;
    [self.view addSubview:label];
    
    [NSLayoutConstraint activateConstraints:@[
        [label.centerXAnchor constraintEqualToAnchor:self.view.centerXAnchor],
        [label.bottomAnchor constraintEqualToAnchor:self.view.safeAreaLayoutGuide.bottomAnchor constant:-20],
        [label.leadingAnchor constraintGreaterThanOrEqualToAnchor:self.view.leadingAnchor constant:20],
        [label.trailingAnchor constraintLessThanOrEqualToAnchor:self.view.trailingAnchor constant:-20]
    ]];
}

- (void)configureDDN {
    self.dcv = [[DSCaptureVisionRouter alloc] init];
    // Add CapturedResultReceiver to receive result callback.
    [self.dcv addResultReceiver:self];
}

- (void)addISA {
    self.cameraView = [[DSCameraView alloc] initWithFrame:self.view.bounds];
    // Initialize Dynamsoft Camera Enhancer.
    self.dce = [[DSCameraEnhancer alloc] init];
    self.dce.cameraView = self.cameraView;
    self.dce.cameraView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self.dce enableEnhancedFeatures:DSEnhancedFeatureFrameFilter];
    [self.view addSubview:self.cameraView];
    
    // You can use the following code to get normalized images with higher resolution.
    //[self.dce setResolution:DSResolution4K];
    
    // Get the layer of DDN and set it visible.
    DSDrawingLayer *ddnDrawingLayer = [self.dce.cameraView getDrawingLayer:DSDrawingLayerIdDDN];
    ddnDrawingLayer.visible = true;
  
    // Set Dynamsoft Camera Enhancer as the input
    [self.dcv setInput:self.dce error:nil];
}

- (void)setupUI {
    [self.view addSubview:self.tipLabel];
}

- (void)updateSettings {
    self.title = @"Auto Scan";
    self.tipLabel.text = @"Please keep your device stable.";
    
    // Enable multi-frame result cross filter to receive more accurate boundaries.
    DSMultiFrameResultCrossFilter *resultFilter = [[DSMultiFrameResultCrossFilter alloc] init];
    [resultFilter enableResultCrossVerification:DSCapturedResultItemTypeNormalizedImage isEnabled:YES];
    [self.dcv addResultFilter:resultFilter];
}

- (void)captureAction {
    isNeedToQuadEdit = YES;
}

- (void)showResult:(NSString *)title message:(nullable NSString *)message completion:(void (^ __nullable)(void))completion {
    dispatch_async(dispatch_get_main_queue(), ^{
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleAlert];
        [alert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            if (completion) {
                completion();
            }
        }]];
        [self presentViewController:alert animated:YES completion:nil];
    });
}

// MARK: LicenseVerificationListener
- (void)onLicenseVerified:(BOOL)isSuccess error:(nullable NSError *)error {
    if (!isSuccess && error != nil) {
        NSLog(@"error: %@", error);
        dispatch_async(dispatch_get_main_queue(), ^{
            [self displayLicenseMessage:[NSString stringWithFormat:@"License initialization failed: %@", error.localizedDescription]];
        });
    }
}

// MARK: - DSCapturedResultReceiver.

// Implement the following method to receive the callback of normalized image.
- (void)onNormalizedImagesReceived:(DSNormalizedImagesResult *)result {
    if (result.items.count == 0) {
        return;
    }
    
    // Get the original image.
    NSError *convertError;
    UIImage *image = [result.items.firstObject.imageData toUIImage:&convertError];
    
    DSQuadDrawingItem *quadDrawingItem = [[DSQuadDrawingItem alloc]  initWithDrawingStyleId:DSDrawingLayerIdDDN state:DSDrawingItemStateDefault quadrilateral:result.items[0].location];
    if (image != nil) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.dcv stopCapturing];
            [self.dce clearBuffer];
            DDNNormalizeViewController *ddnNormalizerVC = [[DDNNormalizeViewController alloc] init];
            ddnNormalizerVC.dcv = self.dcv;
            ddnNormalizerVC.resultImage = image;
            ddnNormalizerVC.selectedItem = quadDrawingItem;
            [self.navigationController pushViewController:ddnNormalizerVC animated:YES];
        });
    }
}

// MARK: - Lazy

- (UILabel *)tipLabel {
    if (!_tipLabel) {
        _tipLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, kScreenHeight - 100, kScreenWidth, 50)];
        _tipLabel.textColor = UIColor.whiteColor;
        _tipLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _tipLabel;
}

@end

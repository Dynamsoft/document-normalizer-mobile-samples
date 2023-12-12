/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import "CameraViewController.h"

NSString *const DDNCustomizedTemplateDetectDocumentBoundaries = @"DetectDocumentBoundaries_Default";
NSString *const DDNCustomizedTemplateDetectAndNormalizeDocument = @"DetectAndNormalizeDocument_Default";
NSString *const DDNCustomizedTemplateNormalizeDocument = @"NormalizeDocument_Default";
NSString *const DDNCustomizedTemplateNormalizeDocumentGrayscale = @"normalize-document-grayscale";
NSString *const DDNCustomizedTemplateNormalizeDocumentBinary = @"normalize-document-binary";

@interface CameraViewController ()<DSCapturedResultReceiver>
{
    BOOL isNeedToQuadEdit;
}
@property (nonatomic, strong) DSCaptureVisionRouter *dcv;

@property (nonatomic, strong) DSCameraEnhancer *dce;

@property (nonatomic, strong) DSCameraView *cameraView;

@property (nonatomic, strong) UIButton *captureButton;

@property (nonatomic, strong) UILabel *tipLabel;

@end

@implementation CameraViewController

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.dce open];
    switch (self.ddnVideoType) {
        case EnumDDNVideoTypeScan:
        {
            // Start Capturing. If success, you will be able to receive the capturedResult from the CapturedResultReceiver.
            [self.dcv startCapturing:DDNCustomizedTemplateDetectDocumentBoundaries completionHandler:nil];
            break;
        }
        case EnumDDNVideoTypeAutoScan:
        {
            // Start Capturing. If success, you will be able to receive the capturedResult from the CapturedResultReceiver.
            [self.dcv startCapturing:DDNCustomizedTemplateDetectAndNormalizeDocument completionHandler:nil];
            break;
        }
        default:
            break;
    }
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.dce close];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor whiteColor];

    [self configureDDN];
    [self addISA];
    [self setupUI];
    [self updateSettingsWithDDNVideoType];
}

- (void)configureDDN {
    self.dcv = [[DSCaptureVisionRouter alloc] init];
    // Add CapturedResultReceiver to receive result callback.
    [self.dcv addResultReceiver:self];
    
    // Initialize the settings from the template file.
    // The template file is located in the Resource folder.
    NSString *customizedJsonPath = [[NSBundle mainBundle] pathForResource:@"ddn-mobile-sample" ofType:@"json"];
    NSError *templateError;
    [self.dcv initSettingsFromFile:customizedJsonPath error:&templateError];
    if (templateError) NSLog(@"templateError:%@", templateError);
}

- (void)addISA {
    self.cameraView = [[DSCameraView alloc] initWithFrame:self.view.bounds];
    // Initialize Dynamsoft Camera Enhancer.
    self.dce = [[DSCameraEnhancer alloc] init];
    self.dce.cameraView = self.cameraView;
    self.dce.cameraView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self.view addSubview:self.cameraView];
    
    // Get the layer of DDN and set it visible.
    DSDrawingLayer *ddnDrawingLayer = [self.dce.cameraView getDrawingLayer:DSDrawingLayerIdDDN];
    ddnDrawingLayer.visible = true;
  
    // Set Dynamsoft Camera Enhancer as the input
    [self.dcv setInput:self.dce error:nil];
}

- (void)setupUI {
    [self.view addSubview:self.captureButton];
    [self.view addSubview:self.tipLabel];
}

- (void)updateSettingsWithDDNVideoType {
    switch (self.ddnVideoType) {
        case EnumDDNVideoTypeScan:
        {
            self.title = @"Scan & Edit";
            self.captureButton.hidden = NO;
            self.tipLabel.text = @"Click the 'Capture' button to start editing.";
            break;
        }
        case EnumDDNVideoTypeAutoScan:
        {
            self.title = @"Auto Scan";
            self.captureButton.hidden = YES;
            self.tipLabel.text = @"Please keep your device stable.";
            
            // Enable multi-frame result cross filter to receive more accurate boundaries.
            DSMultiFrameResultCrossFilter *resultFilter = [[DSMultiFrameResultCrossFilter alloc] init];
            [resultFilter enableResultCrossVerification:DSCapturedResultItemTypeNormalizedImage isEnabled:YES];
            [self.dcv addResultFilter:resultFilter];
            break;
        }
        default:
            break;
    }
}

- (void)captureAction {
    isNeedToQuadEdit = YES;
}

// MARK: - DSCapturedResultReceiver.
// Implement the following method to receive the callback of detected boundaries.
- (void)onDetectedQuadsReceived:(DSDetectedQuadsResult *)result {
    if (result.items.count == 0) {
        return;
    }
    if (isNeedToQuadEdit == NO) {
        return;
    }
    
    isNeedToQuadEdit = NO;
    NSError *convertError;
    // Get the original image.
    UIImage *image = [[[self.dcv getIntermediateResultManager] getOriginalImage:result.originalImageHashId] toUIImage:&convertError];
    
    if (image != nil) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.dcv stopCapturing];
            DDNEditorViewController *ddnEditorVC = [[DDNEditorViewController alloc] init];
            ddnEditorVC.resultImage = image;
            ddnEditorVC.detectedQuadResultsArr = result.items;
            ddnEditorVC.dcv = self.dcv;
            [self.navigationController pushViewController:ddnEditorVC animated:YES];
        });
    }
}

// Implement the following method to receive the callback of normalized image.
- (void)onNormalizedImagesReceived:(DSNormalizedImagesResult *)result {
    if (result.items.count == 0) {
        return;
    }
    
    NSError *convertError;
    // Get the original image.
    UIImage *image = [[[self.dcv getIntermediateResultManager] getOriginalImage:result.originalImageHashId] toUIImage:&convertError];
    
    DSQuadDrawingItem *quadDrawingItem = [[DSQuadDrawingItem alloc]  initWithDrawingStyleId:DSDrawingLayerIdDDN state:DSDrawingItemStateDefault quadrilateral:result.items[0].location];
    if (image != nil) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.dcv stopCapturing];
            DDNNormalizeViewController *ddnNormalizerVC = [[DDNNormalizeViewController alloc] init];
            ddnNormalizerVC.dcv = self.dcv;
            ddnNormalizerVC.resultImage = image;
            ddnNormalizerVC.selectedItem = quadDrawingItem;
            [self.navigationController pushViewController:ddnNormalizerVC animated:YES];
        });
    }
}

// MARK: - Lazy
- (UIButton *)captureButton {
    if (!_captureButton) {
        _captureButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _captureButton.frame = CGRectMake((kScreenWidth - 150) / 2.0, kScreenHeight - 100, 150, 50);
        _captureButton.backgroundColor = [UIColor grayColor];
        _captureButton.layer.cornerRadius = 10;
        _captureButton.layer.borderColor = [UIColor darkGrayColor].CGColor;
        [_captureButton setTitle:@"Capture" forState:UIControlStateNormal];
        [_captureButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_captureButton addTarget:self action:@selector(captureAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _captureButton;
}

- (UILabel *)tipLabel {
    if (!_tipLabel) {
        _tipLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, self.captureButton.top - 100, kScreenWidth, 50)];
        _tipLabel.textColor = UIColor.whiteColor;
        _tipLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _tipLabel;
}

@end

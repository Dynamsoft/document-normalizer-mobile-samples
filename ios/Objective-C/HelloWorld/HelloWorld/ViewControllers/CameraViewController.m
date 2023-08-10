/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import "CameraViewController.h"

NSString *const DDNCustomizedTemplateDetectDocumentBoundaries = @"detect-document-boundaries";
NSString *const DDNCustomizedTemplateDetectAndNormalizeDocument = @"detect-and-normalize-document";
NSString *const DDNCustomizedTemplateNormalizeDocument = @"normalize-document";
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
    NSError *captureError;
    switch (self.ddnVideoType) {
        case EnumDDNVideoTypeScan:
        {
            [self.dcv startCapturing:DDNCustomizedTemplateDetectDocumentBoundaries error:&captureError];
            break;
        }
        case EnumDDNVideoTypeAutoScan:
        {
            [self.dcv startCapturing:DDNCustomizedTemplateDetectAndNormalizeDocument error:&captureError];
            break;
        }
        default:
            break;
    }
    
    if (captureError) NSLog(@"captureError:%@", captureError);
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
    [self.dcv addResultReceiver:self];
    
    // Configure customized Json template.
    NSString *customizedJsonPath = [[NSBundle mainBundle] pathForResource:@"ddn-mobile-sample" ofType:@"json"];
    NSError *templateError;
    [self.dcv initSettingsFromFile:customizedJsonPath error:&templateError];
    if (templateError) NSLog(@"templateError:%@", templateError);
}

- (void)addISA {
    self.cameraView = [[DSCameraView alloc] initWithFrame:self.view.bounds];
    self.dce = [[DSCameraEnhancer alloc] init];
    self.dce.cameraView = self.cameraView;
    self.dce.cameraView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self.view addSubview:self.cameraView];
    
    // DDN layer.
    DSDrawingLayer *ddnDrawingLayer = [self.dce.cameraView getDrawingLayer:DSDrawingLayerIdDDN];
    ddnDrawingLayer.visible = true;
  
    // Bind ISA to DCV.
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
            
            // Add Verification filter.
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
- (void)onDetectedQuadsReceived:(DSDetectedQuadsResult *)result {
    if (result.items.count == 0) {
        return;
    }
    if (isNeedToQuadEdit == NO) {
        return;
    }
    
    isNeedToQuadEdit = NO;
    NSError *convertError;
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

- (void)onNormalizedImagesReceived:(DSNormalizedImagesResult *)result {
    if (result.items.count == 0) {
        return;
    }
    
    NSError *convertError;
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

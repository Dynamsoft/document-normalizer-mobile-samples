/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import "DDNNormalizeViewController.h"

@interface DDNNormalizeViewController ()

@property (nonatomic, strong) UIImage *normalizedImage;

@property (nonatomic, strong) UIImageView *normalizedImageV;

@end

@implementation DDNNormalizeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = UIColor.blackColor;
    self.title = @"Result";
    [self setupUI];
}

- (void)setupUI {
    [self.view addSubview:self.normalizedImageV];
    self.normalizedImageV.image = self.normalizedImage;
    
    UISegmentedControl *segmentControl = [[UISegmentedControl alloc] initWithItems:@[@"Binary", @"Grayscale", @"Colour"]];
    segmentControl.backgroundColor = [UIColor whiteColor];
    segmentControl.frame = CGRectMake((kScreenWidth - 300) / 2.0, 100, 300, 50);
    segmentControl.selectedSegmentIndex = 2;
    segmentControl.selectedSegmentTintColor = [[UIColor blueColor] colorWithAlphaComponent:0.2];
    [segmentControl addTarget:self action:@selector(segmentValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.view addSubview:segmentControl];
    
    [self normalizeWithType:EnumNormalizeTypeColour];
}

- (void)segmentValueChanged:(UISegmentedControl *)control {
    switch (control.selectedSegmentIndex) {
        case 0:
        {
            [self normalizeWithType:EnumNormalizeTypeBinary];
            break;
        }
        case 1:
        {
            [self normalizeWithType:EnumNormalizeTypeGrayscale];
            break;
        }
        case 2:
        {
            [self normalizeWithType:EnumNormalizeTypeColour];
            break;
        }
        default:
            break;
    }
}

- (void)normalizeWithType:(EnumNormalizeType)normalizeType {
    NSString *normalizeTemplateName = DDNCustomizedTemplateNormalizeDocument;
    switch (normalizeType) {
        case EnumNormalizeTypeBinary:
        {
            normalizeTemplateName = DDNCustomizedTemplateNormalizeDocumentBinary;
            break;
        }
        case EnumNormalizeTypeGrayscale:
        {
            normalizeTemplateName = DDNCustomizedTemplateNormalizeDocumentGrayscale;
            break;
        }
        case EnumNormalizeTypeColour:
        {
            normalizeTemplateName = DDNCustomizedTemplateNormalizeDocument;
            break;
        }
        default:
            break;
    }
    
    // 1. Set ROI.
    NSError *cvrSettingError;
    DSSimplifiedCaptureVisionSettings *cvrSettings = [self.dcv getSimplifiedSettings:normalizeTemplateName error:&cvrSettingError];
    if (cvrSettings != nil) {
        cvrSettings.roi = self.selectedItem.quad;
        cvrSettings.roiMeasuredInPercentage = NO;

        NSError *cvrUpdateSettingError = nil;
        [self.dcv updateSettings:normalizeTemplateName settings:cvrSettings error:&cvrUpdateSettingError];
        if (cvrUpdateSettingError) NSLog(@"cvrUpdateSettingError:%@", cvrUpdateSettingError);
    }
    if (cvrSettingError) NSLog(@"cvrSettings:%@", cvrSettings);

    // 2. Normalize.
    NSError *ddnCaptureError;
    DSCapturedResult *result = [self.dcv captureFromImage:self.resultImage templateName:normalizeTemplateName error:&ddnCaptureError];
    if (ddnCaptureError) NSLog(@"ddnCaptureError:%@", ddnCaptureError);

    if (result.items.count > 0) {
        DSNormalizedImageResultItem *normalizedResultItem = (DSNormalizedImageResultItem *)result.items[0];
        self.normalizedImageV.image = [normalizedResultItem.imageData toUIImage:nil];
    }
}

// MARK: - Lazy
- (UIImageView *)normalizedImageV {
    if (!_normalizedImageV) {
        _normalizedImageV = [[UIImageView alloc] initWithFrame:self.view.bounds];
        _normalizedImageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _normalizedImageV;
}

@end

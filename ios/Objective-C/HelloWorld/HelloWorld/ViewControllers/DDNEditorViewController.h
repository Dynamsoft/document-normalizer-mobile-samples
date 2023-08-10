/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface DDNEditorViewController : UIViewController

@property (nonatomic, strong) DSCaptureVisionRouter *dcv;

@property (nonatomic, strong) UIImage *resultImage;

@property (nonatomic, copy) NSArray *detectedQuadResultsArr;

@end

NS_ASSUME_NONNULL_END

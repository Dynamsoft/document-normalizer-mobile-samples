/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface DDNNormalizeViewController : UIViewController

@property (nonatomic, strong) DSCaptureVisionRouter *dcv;

@property (nonatomic, strong) UIImage *resultImage;

@property (nonatomic, strong) DSQuadDrawingItem *selectedItem;

@end

NS_ASSUME_NONNULL_END

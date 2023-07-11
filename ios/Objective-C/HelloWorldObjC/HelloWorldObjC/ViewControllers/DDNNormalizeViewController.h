//
//  DDNNormalizeViewController.h
//  HelloWorldObjC
//
//  Created by dynamsoft's mac on 2023/7/10.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface DDNNormalizeViewController : UIViewController

@property (nonatomic, strong) DSCaptureVisionRouter *dcv;

@property (nonatomic, strong) UIImage *resultImage;

@property (nonatomic, strong) DSQuadDrawingItem *selectedItem;

@end

NS_ASSUME_NONNULL_END

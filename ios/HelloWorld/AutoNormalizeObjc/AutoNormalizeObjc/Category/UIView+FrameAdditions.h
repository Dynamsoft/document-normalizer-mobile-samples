/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIView (FrameAdditions)

/// Hegiht
@property (nonatomic,assign) CGFloat height;
/// Width
@property (nonatomic,assign) CGFloat width;

/// Y
@property (nonatomic,assign) CGFloat top;
/// X
@property (nonatomic,assign) CGFloat left;

/// Y + Height
@property (nonatomic,assign) CGFloat bottom;
/// X + Width
@property (nonatomic,assign) CGFloat right;

@end

NS_ASSUME_NONNULL_END

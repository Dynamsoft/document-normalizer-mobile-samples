//
//  HelloWorld.h
//  HelloWorld
//
//  Created by admin on 2022/3/4.
//  Copyright © 2022 dynamsoft. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <DynamsoftDocumentNormalizer/DynamsoftDocumentNormalizer.h>

NS_ASSUME_NONNULL_BEGIN

@interface DDNDataManager : NSObject

@property (nonatomic, strong) DynamsoftDocumentNormalizer *ddn;

@property (nonatomic, strong) UIImage *resultImage;

@property (nonatomic, strong) NSArray<iDetectedQuadResult *> *quadArr;

@property (nonatomic, strong) iImageData *imageData; 

+ (DDNDataManager *)instance;

@end

NS_ASSUME_NONNULL_END

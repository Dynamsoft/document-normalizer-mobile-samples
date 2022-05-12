//
//  HelloWorld.m
//  HelloWorld
//
//  Created by admin on 2022/3/4.
//  Copyright © 2022 dynamsoft. All rights reserved.
//

#import "DDNDataManager.h"

@implementation DDNDataManager

+ (DDNDataManager *)instance{
    static DDNDataManager *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [super allocWithZone:NULL];
    });
    return instance;
}

@end

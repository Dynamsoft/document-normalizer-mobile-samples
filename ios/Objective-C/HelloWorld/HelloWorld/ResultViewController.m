//
//  ResultViewController.m
//  HelloWorld
//
//  Created by admin on 2022/2/25.
//  Copyright © 2022 dynamsoft. All rights reserved.
//

#import "ResultViewController.h"
#import "DDNDataManager.h"

@interface ResultViewController ()

@end

@implementation ResultViewController

@synthesize imageView;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self configResultView];
    // Do any additional setup after loading the view.
}

- (void)configResultView{
    self.view.backgroundColor = [UIColor blackColor];
    CGFloat w = [[UIScreen mainScreen] bounds].size.width;
    CGFloat h = [[UIScreen mainScreen] bounds].size.height;
    
    imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, w, h)];
    imageView.userInteractionEnabled = YES;
    imageView.contentMode = UIViewContentModeScaleAspectFit;
    [imageView setImage:[DDNDataManager instance].resultImage];
    [self.view addSubview:self->imageView];
}

@end

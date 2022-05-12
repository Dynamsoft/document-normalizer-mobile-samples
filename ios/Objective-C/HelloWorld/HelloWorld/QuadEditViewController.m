//
//  QuadEditViewController.m
//  HelloWorld
//
//  Created by admin on 2022/3/4.
//  Copyright © 2022 dynamsoft. All rights reserved.
//

#import "QuadEditViewController.h"
#import "DDNDataManager.h"
#import <DynamsoftCameraEnhancer/DynamsoftCameraEnhancer.h>

@interface QuadEditViewController ()

@end

@implementation QuadEditViewController{
    DCEImageEditorView *editorView;
    DCEDrawingLayer* layer;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self configImageEditorView];
    [self configUI];
}

- (void)configImageEditorView {
    editorView = [[DCEImageEditorView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    [editorView setOriginalImage:[DDNDataManager instance].imageData];
    
    layer = [editorView getDrawingLayer:DDN_LAYER_ID];
    
    NSMutableArray<DrawingItem *> *array = [NSMutableArray array];
    for (iDetectedQuadResult *detectedQuadResult in [DDNDataManager instance].quadArr) {
        iQuadrilateral *quad = detectedQuadResult.location;
        QuadDrawingItem *quadItem = [[QuadDrawingItem alloc] initWithQuad:quad];
        [array addObject:quadItem];
    }
    layer.drawingItems = array;
    
    [self.view addSubview:editorView];
}

- (void)configUI{
    CGFloat w = [[UIScreen mainScreen] bounds].size.width;
    CGFloat h = [[UIScreen mainScreen] bounds].size.height;
    CGFloat SafeAreaBottomHeight = [[UIApplication sharedApplication] statusBarFrame].size.height > 20 ? 34 : 0;
    
    UIButton *button = [[UIButton alloc] initWithFrame:CGRectMake(w / 2 - 60, h - 100 - SafeAreaBottomHeight, 120, 60)];

    [button setTitle:@"Normalize" forState:UIControlStateNormal];
    [button setBackgroundColor:[UIColor greenColor]];
    [button addTarget:self action:@selector(normalizeImage) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:button];
}

- (void)normalizeImage{
    QuadDrawingItem *item = (QuadDrawingItem *)[editorView getSelectedDrawingItem];
    if(nil == item) {
        item = (QuadDrawingItem *)layer.drawingItems[0];
    }
    
    NSError *error;
    iNormalizedImageResult *imageData = [[DDNDataManager instance].ddn normalizeBuffer:[DDNDataManager instance].imageData quad:item.quad error:&error];
    
    [DDNDataManager instance].resultImage = [imageData.image toUIImage:&error];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self performSegueWithIdentifier:@"pushResultView" sender:nil];
    });
}

@end

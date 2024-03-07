/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import "DDNEditorViewController.h"

@interface DDNEditorViewController ()
{
    DSImageEditorView *editorView;
    DSDrawingLayer *layer;
}

@property (nonatomic, strong) UIButton *normalizeButton;

@end

@implementation DDNEditorViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = @"Edit";
    [self configImageEditorView];
    [self setupUI];
}

- (void)configImageEditorView {
    // Initialize the image editor view. On the Image editor view, you can display and edit the detected document boundaries.
    editorView = [[DSImageEditorView alloc] initWithFrame:self.view.bounds];
    // Set the image that displayed on the editor view.
    editorView.image = self.resultImage;
    [self.view addSubview:editorView];
    
    // Get the layer of DDN and draw detected boundaries on the layer. 
    layer = [editorView getDrawingLayer:DSDrawingLayerIdDDN];
    NSMutableArray<DSDrawingItem *> *drawingArray = [NSMutableArray array];
    for (DSDetectedQuadResultItem *quadResultItem in self.detectedQuadResultsArr) {
        DSQuadDrawingItem *quadDrawingItem = [[DSQuadDrawingItem alloc]  initWithDrawingStyleId:DSDrawingLayerIdDDN state:DSDrawingItemStateDefault quadrilateral:quadResultItem.location];
        [drawingArray addObject:quadDrawingItem];
    }
    layer.drawingItems = drawingArray;
}

- (void)setupUI {
    [self.view addSubview:self.normalizeButton];
}

- (void)normalizeAction {    
    // Get the selected quad and use the quad as the ROI for further process.
    DSQuadDrawingItem *selectedItem = (DSQuadDrawingItem *)[editorView getSelectedDrawingItem];
    if(selectedItem == nil) {
        selectedItem = layer.drawingItems.count > 0 ? (DSQuadDrawingItem *)layer.drawingItems[0] : nil;
    }
    
    if (selectedItem == nil) {
        return;
    }
    
    DDNNormalizeViewController *ddnNormalizerVC = [[DDNNormalizeViewController alloc] init];
    ddnNormalizerVC.dcv = self.dcv;
    ddnNormalizerVC.resultImage = self.resultImage;
    ddnNormalizerVC.selectedItem = selectedItem;
    [self.navigationController pushViewController:ddnNormalizerVC animated:YES];
}

// MARK: - Lazy
- (UIButton *)normalizeButton {
    if (!_normalizeButton) {
        _normalizeButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _normalizeButton.frame = CGRectMake((kScreenWidth - 150) / 2.0, kScreenHeight - 100, 150, 50);
        _normalizeButton.backgroundColor = [UIColor grayColor];
        _normalizeButton.layer.cornerRadius = 10;
        _normalizeButton.layer.borderColor = [UIColor darkGrayColor].CGColor;
        [_normalizeButton setTitle:@"Normalize" forState:UIControlStateNormal];
        [_normalizeButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_normalizeButton addTarget:self action:@selector(normalizeAction) forControlEvents:UIControlEventTouchUpInside];
    }
    return _normalizeButton;
}

@end

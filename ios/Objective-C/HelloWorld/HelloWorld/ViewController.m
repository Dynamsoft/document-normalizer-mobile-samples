
#import "ViewController.h"
#import <DynamsoftDocumentNormalizer/DynamsoftDocumentNormalizer.h>
#import <DynamsoftCameraEnhancer/DynamsoftCameraEnhancer.h>
#import "DDNDataManager.h"

@interface ViewController ()<DetectResultListener>

@property(nonatomic, strong) DynamsoftCameraEnhancer *dce;
@property(nonatomic, strong) DCECameraView *dceView;

@end

@implementation ViewController{
    UIButton *photoButton;
    bool isview;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self configDDN];
    [self configDCE];
    [self configUI];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [[DDNDataManager instance].ddn startDetecting];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [[DDNDataManager instance].ddn stopDetecting];
}

- (void)configDDN{
    [DDNDataManager instance].ddn = [DynamsoftDocumentNormalizer new];
}

- (void)configDCE{
    _dceView = [DCECameraView cameraWithFrame:self.view.bounds];
    [self.view addSubview:_dceView];
    
    [_dceView getDrawingLayer:DDN_LAYER_ID];

    _dce = [[DynamsoftCameraEnhancer alloc] initWithView:_dceView];
    [_dce open];
    [[DDNDataManager instance].ddn setCameraEnhancer:_dce];
    [[DDNDataManager instance].ddn setDetectResultListener:self];
}

- (void)configUI{
    CGFloat w = [[UIScreen mainScreen] bounds].size.width;
    CGFloat h = [[UIScreen mainScreen] bounds].size.height;
    CGFloat SafeAreaBottomHeight = [[UIApplication sharedApplication] statusBarFrame].size.height > 20 ? 34 : 0;
    
    photoButton = [[UIButton alloc] initWithFrame:CGRectMake(w / 2 - 60, h - 100 - SafeAreaBottomHeight, 120, 60)];
    [photoButton setTitle:@"Capture" forState:UIControlStateNormal];
    [photoButton setBackgroundColor:[UIColor greenColor]];
    [photoButton addTarget:self action:@selector(takePictures) forControlEvents:UIControlEventTouchUpInside];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.view addSubview:self->photoButton];
    });
}

- (void)takePictures{
    isview = true;
}

- (void)detectResultCallback:(NSInteger)frameId imageData:(nonnull iImageData *)imageData results:(nonnull NSArray<iDetectedQuadResult *> *)results {
    if (isview && results) {
        isview = false;
        
        [DDNDataManager instance].quadArr = results;
        [DDNDataManager instance].imageData = imageData;
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self performSegueWithIdentifier:@"pushQuadEditView" sender:nil];
        });
    }
}

@end

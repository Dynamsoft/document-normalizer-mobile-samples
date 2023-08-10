/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

#import "ViewController.h"

@interface ViewController ()

@property (nonatomic, strong) UIButton *scanButton;

@property (nonatomic, strong) UIButton *autoScanButton;

@end

@implementation ViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

    self.navigationController.navigationBar.tintColor = [UIColor whiteColor];
    self.navigationController.navigationBar.titleTextAttributes = @{NSForegroundColorAttributeName:[UIColor whiteColor]};
    [self.navigationController.navigationBar setBarTintColor:kNavigationBackgroundColor];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = @"HelloWorld";
    self.view.backgroundColor = UIColor.whiteColor;
    
    [self setupUI];
}

- (void)setupUI {
    [self.view addSubview:self.scanButton];
    [self.view addSubview:self.autoScanButton];
}

- (void)scanAction:(UIButton *)button {
    CameraViewController *cameraVC = [[CameraViewController alloc] init];
    cameraVC.ddnVideoType = button.tag;
    [self.navigationController pushViewController:cameraVC animated:YES];
}

// MARK: - Lazy
- (UIButton *)scanButton {
    if (!_scanButton) {
        _scanButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _scanButton.frame = CGRectMake((kScreenWidth - 150) / 2.0, (kScreenHeight - 50) / 2.0 - 100, 150, 50);
        _scanButton.backgroundColor = [UIColor grayColor];
        _scanButton.layer.cornerRadius = 10;
        _scanButton.layer.borderColor = [UIColor darkGrayColor].CGColor;
        _scanButton.tag = EnumDDNVideoTypeScan;
        [_scanButton setTitle:@"Scan & Edit" forState:UIControlStateNormal];
        [_scanButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_scanButton addTarget:self action:@selector(scanAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _scanButton;
}

- (UIButton *)autoScanButton {
    if (!_autoScanButton) {
        _autoScanButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _autoScanButton.frame = CGRectMake((kScreenWidth - 150) / 2.0, (kScreenHeight - 50) / 2.0 + 100, 150, 50);
        _autoScanButton.backgroundColor = [UIColor grayColor];
        _autoScanButton.layer.cornerRadius = 10;
        _autoScanButton.layer.borderColor = [UIColor darkGrayColor].CGColor;
        _autoScanButton.tag = EnumDDNVideoTypeAutoScan;
        [_autoScanButton setTitle:@"Auto Scan" forState:UIControlStateNormal];
        [_autoScanButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_autoScanButton addTarget:self action:@selector(scanAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _autoScanButton;
}

@end

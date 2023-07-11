//
//  AppDelegate.m
//  HelloWorldObjC
//
//  Created by dynamsoft's mac on 2023/7/10.
//

#import "AppDelegate.h"

@interface AppDelegate ()<DSLicenseVerificationListener>

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    if(@available(ios 15.0,*)){
        UINavigationBarAppearance *appearance = [UINavigationBarAppearance new];
        [appearance configureWithOpaqueBackground];
        appearance.backgroundColor = kNavigationBackgroundColor;
        appearance.titleTextAttributes = @{NSForegroundColorAttributeName:[UIColor whiteColor]};
      

        [[UINavigationBar appearance] setStandardAppearance:appearance];
        [[UINavigationBar appearance] setScrollEdgeAppearance:appearance];
    }
    
    // It is recommended to initialize the License in AppDelegate
    // The license string here is a time-limited trial license. Note that network connection is required for this license to work.
    // You can also request an extension for your trial license in the customer portal: https://www.dynamsoft.com/customer/license/trialLicense?product=dbr&utm_source=installer&package=ios
    [DSLicenseManager initLicense:@"DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9" verificationDelegate:self];
    return YES;
}

- (void)onLicenseVerified:(BOOL)isSuccess error:(NSError *)error {
    [self verificationCallback:error];
}

- (void)verificationCallback:(NSError *)error{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString* msg = @"";
        if(error != nil)
        {
            msg = error.userInfo[NSUnderlyingErrorKey];
            if(msg == nil)
            {
                msg = [error localizedDescription];
            }

            __block UIWindow *topWindow = [[UIWindow alloc] initWithFrame:UIScreen.mainScreen.bounds];
            topWindow.rootViewController = [[UIViewController alloc] init];
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Server license verify failed" message:msg preferredStyle:UIAlertControllerStyleAlert];
            [alert addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                topWindow.hidden = YES;
                topWindow = nil;
            }]];
            [topWindow makeKeyAndVisible];
            [topWindow.rootViewController presentViewController:alert animated:YES completion:nil];
        }
    });
}

@end

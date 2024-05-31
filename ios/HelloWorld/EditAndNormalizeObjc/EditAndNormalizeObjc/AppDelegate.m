/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

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
    // You can also request a 30-day trial license via the Request a Trial License link: https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=github&package=ios
    [DSLicenseManager initLicense:@"DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9" verificationDelegate:self];
    return YES;
}

- (void)onLicenseVerified:(BOOL)isSuccess error:(NSError *)error {
    if (!isSuccess && error != nil) {
        NSLog(@"error: %@", error);
    }
}

@end

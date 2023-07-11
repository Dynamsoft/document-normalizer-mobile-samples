//
//  CameraViewController.h
//  HelloWorldObjC
//
//  Created by dynamsoft's mac on 2023/7/10.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef NSString *DDNCustomizedTemplate;
FOUNDATION_EXPORT DDNCustomizedTemplate const _Nonnull DDNCustomizedTemplateDetectDocumentBoundaries;
FOUNDATION_EXPORT DDNCustomizedTemplate const _Nonnull DDNCustomizedTemplateDetectAndNormalizeDocument;
FOUNDATION_EXPORT DDNCustomizedTemplate const _Nonnull DDNCustomizedTemplateNormalizeDocument;
FOUNDATION_EXPORT DDNCustomizedTemplate const _Nonnull DDNCustomizedTemplateNormalizeDocumentGrayscale;
FOUNDATION_EXPORT DDNCustomizedTemplate const _Nonnull DDNCustomizedTemplateNormalizeDocumentBinary;

@interface CameraViewController : UIViewController

@property (nonatomic, assign) EnumDDNVideoType ddnVideoType;

@end

NS_ASSUME_NONNULL_END

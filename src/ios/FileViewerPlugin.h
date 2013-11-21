#import <Cordova/CDV.h>
#import "FileViewerPluginViewController.h"

@interface FileViewerPlugin : CDVPlugin
{
    FileViewerPluginViewController* previewViewController;
    UIActivityViewController *activityViewController;
}

@property (retain, nonatomic) FileViewerPluginViewController* previewViewController;
@property (retain, nonatomic) UIActivityViewController *activityViewController;

- (void)view:(CDVInvokedUrlCommand*)command;
- (void)share:(CDVInvokedUrlCommand*)command;

@end

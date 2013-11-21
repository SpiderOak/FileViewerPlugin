#import <Cordova/CDV.h>
#import "FileViewerPluginViewController.h"

@interface FileViewerPlugin : CDVPlugin
{
    FileViewerPluginViewController* previewViewController;
}

@property (retain, nonatomic) FileViewerPluginViewController* previewViewController;

- (void)view:(CDVInvokedUrlCommand*)command;
- (void)share:(CDVInvokedUrlCommand*)command;

@end

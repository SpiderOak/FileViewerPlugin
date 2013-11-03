#import <Cordova/CDV.h>

@interface FileViewerPlugin : CDVPlugin

- (void)view:(CDVInvokedUrlCommand*)command;
- (void)share:(CDVInvokedUrlCommand*)command;

@end

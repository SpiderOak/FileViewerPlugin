#import "FileViewerPlugin.h"
#import "FileViewerPluginViewController.h"
#import <Cordova/CDV.h>

@implementation FileViewerPlugin

- (void)view:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    BOOL pluginSuccess = NO;
    
    NSDictionary* arguments = [command.arguments objectAtIndex:0];
    NSString* filePath = [arguments objectForKey:@"url"];
    
    FileViewerPluginViewController* viewController = [[FileViewerPluginViewController alloc] init];
    pluginSuccess = [viewController viewFile:filePath usingViewController:[super viewController]];
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:pluginSuccess];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)share:(CDVInvokedUrlCommand*)command
{
    // ...
}

@end

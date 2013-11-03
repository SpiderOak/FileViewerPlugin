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
    NSDictionary* arguments = [command.arguments objectAtIndex:0];
    NSString* filePath = [arguments objectForKey:@"url"];
    NSString *shareString = @"";
    NSURL *fileUrl = nil;
    
    if (!filePath) {
        NSDictionary* extras = [arguments objectForKey:@"extras"];
        if ([extras objectForKey:@"android.intent.extra.STREAM"]) {
            filePath = [extras objectForKey:@"android.intent.extra.STREAM"];
            if (filePath) {
                fileUrl = [NSURL fileURLWithPath:filePath];
            }
        }
        if ([extras objectForKey:@"android.intent.extra.TEXT"]) {
            shareString = [extras objectForKey:@"android.intent.extra.TEXT"];
        }
    }
    
    NSArray *activityItems = [NSArray arrayWithObjects:shareString, fileUrl, nil];
    
    UIActivityViewController *activityViewController = [[UIActivityViewController alloc] initWithActivityItems:activityItems applicationActivities:nil];
    activityViewController.modalTransitionStyle = UIModalTransitionStyleCoverVertical;
    activityViewController.excludedActivityTypes = @[UIActivityTypePrint, UIActivityTypeCopyToPasteboard, UIActivityTypeAssignToContact, UIActivityTypeSaveToCameraRoll];
    
    [[super viewController] presentViewController:activityViewController animated:YES completion:nil];
}

@end

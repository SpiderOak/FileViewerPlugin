@interface FileViewerPluginViewController : UIViewController <UIDocumentInteractionControllerDelegate>
{
    UIDocumentInteractionController* documentInteractionController;
    UIViewController* myViewController;
}

@property (retain, nonatomic) UIDocumentInteractionController* documentInteractionController;
@property (retain, nonatomic) UIViewController* myViewController;

- (BOOL)viewFile:(NSString *)filePath usingViewController: (UIViewController *) viewController;

@end

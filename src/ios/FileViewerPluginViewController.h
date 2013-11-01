@interface FileViewerPluginViewController : UIViewController <UIDocumentInteractionControllerDelegate>
{
    UIDocumentInteractionController* documentInteractionController;
}

@property (retain, nonatomic) UIDocumentInteractionController* documentInteractionController;

- (BOOL)viewFile:(NSString *)filePath usingViewController: (UIViewController *) viewController;

@end

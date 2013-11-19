FileViewerPlugin
================

Cordova File Viewer (as well as Sharing) Plugin for Android and iOS.

The Android side is based on the WebIntent plugin by Boris Smus (@borismus) - [https://github.com/phonegap/phonegap-plugins/tree/master/Android/WebIntent](https://github.com/phonegap/phonegap-plugins/tree/master/Android/WebIntent). This has been modified to only contain the parts needed and some methods were renamed, etc.

The iOS side uses `UIActivityViewController` and `UIDocumentInteractionController`.

It was created for the SpiderOak Mobile Client project - [https://github.com/SpiderOak/SpiderOakMobileClient](https://github.com/SpiderOak/SpiderOakMobileClient)

One downside of having been written for just Android first is that there are some Androidisms in the iOS plugin (such as passing `android.intent.extra.STREAM` in the extras params etc). Fixing this, and better documentation, will be done soon.

Follows the [Cordova Plugin spec](https://github.com/alunny/cordova-plugin-spec), so that it works with the [Cordova CLI tools](https://github.com/apache/cordova-cli/).

`cordova plugin add https://github.com/SpiderOak/FileViewerPlugin.git`

`phonegap local plugin add https://github.com/SpiderOak/FileViewerPlugin.git`

import UIKit
import Flutter
import GoogleMaps

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    GMSServices.provideAPIKey("AIzaSyCFR3CQU6aUx0_Q02pP43X0ivKgtDjnPBs")
    let controller : FlutterViewController = window?.rootViewController as! FlutterViewController
    let mapChannel = FlutterMethodChannel(name: "com.novugrid.flutter_marker_animation",
                                              binaryMessenger: controller.binaryMessenger)
    mapChannel.setMethodCallHandler({
      (call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
        if(call.method == "openMap") {
            let vc = MapViewController()
            vc.modalPresentationStyle = .overFullScreen;
            controller.present(vc, animated: true, completion: nil)
        } else {
            print("not implemented")
        }
    })
    GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}

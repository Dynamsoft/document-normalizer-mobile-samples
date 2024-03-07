/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

import UIKit
import DynamsoftCore
import DynamsoftCameraEnhancer
import DynamsoftDocumentNormalizer
import DynamsoftCaptureVisionRouter
import DynamsoftUtility

// This is the class of automatical document scan.
class AutoScanViewController: UIViewController, CapturedResultReceiver {
    
    var cameraView:CameraView!
    var dce:CameraEnhancer!
    var cvr:CaptureVisionRouter!
    private var data:ImageData!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setUpCamera()
        setUpDCV()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        // Open the camera.
        dce.open()
        // Start Capturing. If success, you will be able to receive the capturedResult from the CapturedResultReceiver.
        cvr.startCapturing(PresetTemplate.detectAndNormalizeDocument.rawValue) { isSuccess, error in
            if (!isSuccess) {
                if let error = error {
                    self.showResult("Error", error.localizedDescription)
                }
            }
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        dce.close()
    }

    func setUpCamera() {
        cameraView = .init(frame: view.bounds)
        cameraView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        view.insertSubview(cameraView, at: 0)
        dce = CameraEnhancer()
        dce.cameraView = cameraView
        dce.enableEnhancedFeatures(.frameFilter)
        
        // You can use the following code to get normalized images with higher resolution.
        // dce.setResolution(.resolution4K)
        
        // Get the layer of DDN and set it visible.
        let layer = cameraView.getDrawingLayer(DrawingLayerId.DDN.rawValue)
        layer?.visible = true
    }
    
    func setUpDCV() {
        cvr = CaptureVisionRouter()

        // Set Dynamsoft Camera Enhancer as the input
        try? cvr.setInput(dce)

        // Add CapturedResultReceiver to receive result callback.
        cvr.addResultReceiver(self)

        // Enable multi-frame result cross filter to receive more accurate boundaries.
        let filter = MultiFrameResultCrossFilter()
        filter.enableResultCrossVerification(.normalizedImage, isEnabled: true)
        cvr.addResultFilter(filter)
    }
    
    // Implement the following method to receive the callback of normalized image.
    func onNormalizedImagesReceived(_ result: NormalizedImagesResult) {
        if let items = result.items, items.count > 0 {
            guard let data = cvr.getIntermediateResultManager().getOriginalImage(result.originalImageHashId ?? "") else {
                return
            }
            DispatchQueue.main.async(execute: {
                self.cvr.stopCapturing()
            })
            self.data = data
            let quad = items.first?.location
            DispatchQueue.main.async {
                self.performSegue(withIdentifier: "showImageViewController", sender: quad)
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showImageViewController" {
            let vc = segue.destination as? ImageViewController
            vc?.quad = sender as? Quadrilateral
            vc?.cvr = self.cvr
            vc?.data = self.data
        }
    }
    
    private func showResult(_ title: String, _ message: String?, completion: (() -> Void)? = nil) {
        DispatchQueue.main.async {
            let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { _ in completion?() }))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
}

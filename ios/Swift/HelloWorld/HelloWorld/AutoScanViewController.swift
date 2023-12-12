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
        cvr.startCapturing(DetectAndNormalizeTemplate)
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
        // Get the layer of DDN and set it visible.
        let layer = cameraView.getDrawingLayer(DrawingLayerId.DDN.rawValue)
        layer?.visible = true
    }
    
    func setUpDCV() {
        cvr = CaptureVisionRouter()

        // Initialize the settings from the template file.
        // The template file is located in the resource folder.
        let path = Bundle.main.path(forResource: "ddn-mobile-sample", ofType: "json")
        if let path = path {
            try? cvr.initSettingsFromFile(path)
        }

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
    
}

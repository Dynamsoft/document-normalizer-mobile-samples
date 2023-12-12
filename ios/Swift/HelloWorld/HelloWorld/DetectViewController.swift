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

class DetectViewController: UIViewController, CapturedResultReceiver {
    
    var cameraView:CameraView!
    var dce:CameraEnhancer!
    var cvr:CaptureVisionRouter!
    private var buttonTouched = false
    private var data:ImageData!

    @IBOutlet weak var button: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setUpCamera()
        setUpDCV()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        dce.open()
        // Start Capturing. If success, you will be able to receive the capturedResult from the CapturedResultReceiver.
        cvr.startCapturing(DetectTemplate)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        dce.close()
        cvr.stopCapturing()
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
        cvr.addResultReceiver(self)
    }
    
    @IBAction func touchEvent(_ sender: Any) {
        buttonTouched = true
    }
    
    // Implement the following method to receive callback when the video frame is processed.
    func onDetectedQuadsReceived(_ result: DetectedQuadsResult) {
        if buttonTouched {
            buttonTouched = false
            if let items = result.items, items.count > 0 {
                // Get the original image.
                guard let data = cvr.getIntermediateResultManager().getOriginalImage(result.originalImageHashId ?? "") else {
                    return
                }
                self.data = data
                DispatchQueue.main.async {
                    self.performSegue(withIdentifier: "showEditorViewController", sender: items)
                }
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showEditorViewController" {
            let vc = segue.destination as? EditorViewController
            vc?.data = self.data
            vc?.items = sender as? [DetectedQuadResultItem]
            vc?.cvr = self.cvr
        }
    }
    
}

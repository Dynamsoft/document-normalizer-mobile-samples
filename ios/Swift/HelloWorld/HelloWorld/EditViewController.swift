/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

import UIKit
import DynamsoftCore
import DynamsoftDocumentNormalizer
import DynamsoftCaptureVisionRouter
import DynamsoftCameraEnhancer

class EditorViewController: UIViewController {
    
    var data:ImageData!
    var items:[DetectedQuadResultItem]!
    var cvr:CaptureVisionRouter!
    var editorView:ImageEditorView!
    var layer:DrawingLayer!
    
    @IBOutlet weak var button: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        view.backgroundColor = .white
        setUpView()
    }

    func setUpView() {
        editorView = .init(frame: view.bounds)
        view.insertSubview(editorView, at: 0)
        editorView.imageData = data
        layer = editorView.getDrawingLayer(DrawingLayerId.DDN.rawValue)
        var array:[QuadDrawingItem] = .init()
        for item in items {
            array.append(.init(quadrilateral: item.location))
        }
        layer.drawingItems = array
    }
    
    @IBAction func touchEvent(_ sender: Any) {
        var drawingItem = editorView.getSelectedDrawingItem()
        if drawingItem == nil {
            for item in layer.drawingItems! {
                if item.mediaType == .quadrilateral {
                    drawingItem = item
                    break
                }
            }
        }
        if let drawingItem = drawingItem {
            let quadItem:QuadDrawingItem = drawingItem as! QuadDrawingItem
            DispatchQueue.main.async {
                self.performSegue(withIdentifier: "showImageViewController", sender: quadItem.quad)
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

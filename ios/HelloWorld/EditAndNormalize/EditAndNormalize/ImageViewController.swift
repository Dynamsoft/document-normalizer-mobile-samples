/*
 * This is the sample of Dynamsoft Document Normalizer.
 *
 * Copyright © Dynamsoft Corporation.  All rights reserved.
 */

import UIKit
import DynamsoftCore
import DynamsoftCaptureVisionRouter
import DynamsoftDocumentNormalizer

class ImageViewController: UIViewController {
    
    enum ImageType {
        case Binary
        case Gray
        case Color
    }
    var data:ImageData!
    var quad:Quadrilateral!
    var cvr:CaptureVisionRouter!

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var binaryButton: UIButton!
    @IBOutlet weak var grayButton: UIButton!
    @IBOutlet weak var colorButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        setUpView()
    }

    func setUpView() {
        normalize(type: .Color)
        colorButton.isSelected = true
    }
    
    @IBAction func touchColorButton(_ sender: Any) {
        if !colorButton.isSelected {
            colorButton.isSelected  = true
            grayButton.isSelected = false
            binaryButton.isSelected = false
            touchButton(type: .Color)
        }
    }
    
    @IBAction func touchGrayButton(_ sender: Any) {
        if !grayButton.isSelected {
            grayButton.isSelected = true
            colorButton.isSelected = false
            binaryButton.isSelected = false
            touchButton(type: .Gray)
        }
    }
    
    @IBAction func touchBinaryButton(_ sender: Any) {
        if !binaryButton.isSelected {
            binaryButton.isSelected = true
            colorButton.isSelected = false
            grayButton.isSelected = false
            touchButton(type: .Binary)
        }
    }
    
    // Colour mode selection.
    func touchButton(type: ImageType) -> Void {
        normalize(type: type)
    }
    
    // Select the template and normalize the image. 
    func normalize(type: ImageType) {
        let name = PresetTemplate.normalizeDocument.rawValue
        // Get the current simplified settings.
        if let settings = try? cvr.getSimplifiedSettings(name) {
            switch type {
            case .Binary:
                settings.documentSettings?.colourMode = .binary
            case .Gray:
                settings.documentSettings?.colourMode = .grayscale
            case .Color:
                settings.documentSettings?.colourMode = .colour
            }
            // Set the previously detected boundary as the new ROI.
            settings.roi = quad
            settings.roiMeasuredInPercentage = false
            // Update the settings.
            try? cvr.updateSettings(name, settings: settings)
        }
        // Capture the image again with the new ROI.
        let result = cvr.captureFromBuffer(data, templateName: name)
        if let items = result.items, items.count > 0 {
            if let item = items.first, item.type == .normalizedImage {
                let imageItem:NormalizedImageResultItem = item as! NormalizedImageResultItem
                // Get the normalized image and display it on the view.
                let image = try? imageItem.imageData?.toUIImage()
                DispatchQueue.main.async { [self] in
                    imageView.image = image
                }
            }
        }
    }
    
}

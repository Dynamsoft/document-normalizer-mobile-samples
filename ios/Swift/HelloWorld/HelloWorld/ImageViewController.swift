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
        normalize(name: ColorNormalizeTemplate)
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
    
    func touchButton(type: ImageType) -> Void {
        var name:String
        switch type {
        case .Binary:
            name = BinartNormalizeTemplate
        case .Gray:
            name = GrayNormalizeTemplate
        case .Color:
            name = ColorNormalizeTemplate
        }
        normalize(name: name)
    }
    
    func normalize(name:String) {
        if let settings = try? cvr.getSimplifiedSettings(name) {
            settings.roi = quad
            settings.roiMeasuredInPercentage = false;
            try? cvr.updateSettings(name, settings: settings)
        }
        guard let result = try? cvr.captureFromBuffer(data, templateName: name), let items = result.items, items.count > 0 else {
            print("normalize failed")
            return
        }
        if let item = items.first, item.type == .normalizedImage {
            let imageItem:NormalizedImageResultItem = item as! NormalizedImageResultItem
            let image = try? imageItem.imageData?.toUIImage()
            DispatchQueue.main.async { [self] in
                imageView.image = image
            }
        }
    }
    
}

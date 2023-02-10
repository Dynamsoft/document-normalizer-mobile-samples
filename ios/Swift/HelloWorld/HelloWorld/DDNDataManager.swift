//
//  DDNDataManager.swift
//  HelloWorld
//
//  Copyright © Dynamsoft. All rights reserved.
//

import Foundation
import DynamsoftDocumentNormalizer

class DDNDataManager: NSObject {
    var ddn: DynamsoftDocumentNormalizer!
    var resultImage: UIImage!
    var quadArr: [iDetectedQuadResult]!
    var imageData: iImageData!

    static let instance = DDNDataManager()
}

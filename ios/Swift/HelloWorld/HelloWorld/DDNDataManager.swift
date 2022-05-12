//
//  DDNDataManager.swift
//  HelloWorld
//
//  Created by dynamsoft on 2022/5/6.
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

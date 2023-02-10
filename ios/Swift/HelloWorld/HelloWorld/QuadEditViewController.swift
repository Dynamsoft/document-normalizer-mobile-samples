//
//  QuadEditViewController.swift
//  HelloWorld
//
//  Copyright © Dynamsoft. All rights reserved.
//

import Foundation
import DynamsoftCameraEnhancer

class QuadEditViewController: UIViewController {
    private var editorView: DCEImageEditorView!
    private var layer: DCEDrawingLayer!

    override func viewDidLoad() {
        super.viewDidLoad()
        configImageEditorView()
        configUI()
    }

    func configImageEditorView() {
        editorView = DCEImageEditorView(frame: CGRect(x: 0, y: 0, width: view.frame.size.width, height: view.frame.size.height))
        editorView.setOriginalImage(DDNDataManager.instance.imageData)

        layer = editorView.getDrawingLayer(Int(DDN_LAYER_ID))

        var array: [DrawingItem]? = []
        for detectedQuadResult in DDNDataManager.instance.quadArr {
            let quad = detectedQuadResult.location
            let quadItem = QuadDrawingItem(quad: quad)
            array?.append(quadItem)
        }
        layer.drawingItems = array

        view.addSubview(editorView)
    }

    func configUI() {
        let w = UIScreen.main.bounds.size.width
        let h = UIScreen.main.bounds.size.height
        let SafeAreaBottomHeight: CGFloat = UIApplication.shared.statusBarFrame.size.height > 20 ? 34 : 0

        let button = UIButton(frame: CGRect(x: w / 2 - 60, y: h - 100 - SafeAreaBottomHeight, width: 120, height: 60))

        button.setTitle("Normalize", for: .normal)
        button.backgroundColor = UIColor.green
        button.addTarget(self, action: #selector(normalizeImage), for: .touchUpInside)
        view.addSubview(button)
    }
    
    @objc func normalizeImage() {
        var item = editorView.getSelectedDrawingItem() as? QuadDrawingItem
        if nil == item {
            item = layer.drawingItems?[0] as? QuadDrawingItem
        }

        let imageData = try? DDNDataManager.instance.ddn.normalizeBuffer(DDNDataManager.instance.imageData, quad: item!.quad)

        DDNDataManager.instance.resultImage = try? imageData?.image.toUIImage()
        DispatchQueue.main.async(execute: { [self] in
            performSegue(withIdentifier: "pushResultView", sender: nil)
        })
    }
}

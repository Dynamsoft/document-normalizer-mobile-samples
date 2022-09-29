//
//  ViewController.swift
//  HelloWorld
//
//  Created by dynamsoft on 2022/5/6.
//

import UIKit
import DynamsoftCameraEnhancer
import DynamsoftDocumentNormalizer

class ViewController: UIViewController, DetectResultListener {
    private var isNeedToQuadEdit = false
    private var dce: DynamsoftCameraEnhancer!
    private var dceView: DCECameraView!

    override func viewDidLoad() {
        super.viewDidLoad()
        configDDN()
        configDCE()
        configUI()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        DDNDataManager.instance.ddn.startDetecting()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        DDNDataManager.instance.ddn.stopDetecting()
    }

    func configDDN() {
        DDNDataManager.instance.ddn = DynamsoftDocumentNormalizer()
    }

    func configDCE() {
        dceView = DCECameraView(frame: view.bounds)
        view.addSubview(dceView)

        dceView.getDrawingLayer(Int(DDN_LAYER_ID))
        
        dce = DynamsoftCameraEnhancer(view: dceView)
        dce.open()
        DDNDataManager.instance.ddn.setImageSource(dce)
        DDNDataManager.instance.ddn.setDetectResultListener(self)
    }

    func configUI() {
        let w = UIScreen.main.bounds.size.width
        let h = UIScreen.main.bounds.size.height
        let SafeAreaBottomHeight: CGFloat = UIApplication.shared.statusBarFrame.size.height > 20 ? 34 : 0

        let photoButton = UIButton(frame: CGRect(x: w / 2 - 60, y: h - 100 - SafeAreaBottomHeight, width: 120, height: 60))
        photoButton.setTitle("Capture", for: .normal)
        photoButton.backgroundColor = UIColor.green
        photoButton.addTarget(self, action: #selector(takePictures), for: .touchUpInside)
        DispatchQueue.main.async(execute: { [self] in
            view.addSubview(photoButton)
        })
    }

    @objc func takePictures() {
        isNeedToQuadEdit = true
    }
    
    func detectResultCallback(_ frameId: Int, imageData: iImageData, results: [iDetectedQuadResult]) {
        if isNeedToQuadEdit && !results.isEmpty {
            isNeedToQuadEdit = false

            DDNDataManager.instance.quadArr = results
            DDNDataManager.instance.imageData = imageData

            DispatchQueue.main.async(execute: { [self] in
                performSegue(withIdentifier: "pushQuadEditView", sender: nil)
            })
        }
    }
}

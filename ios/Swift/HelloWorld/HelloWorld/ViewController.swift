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
        StaticClass.instance.ddn.startDetecting()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        StaticClass.instance.ddn.stopDetecting()
    }

    func configDDN() {
        StaticClass.instance.ddn = DynamsoftDocumentNormalizer()
    }

    func configDCE() {
        dceView = DCECameraView(frame: view.bounds)
        view.addSubview(dceView)

        dce = DynamsoftCameraEnhancer(view: dceView)
        dce.open()
        StaticClass.instance.ddn.setCameraEnhancer(dce)
        StaticClass.instance.ddn.setDetectResultListener(self)
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

            StaticClass.instance.quadArr = results
            StaticClass.instance.imageData = imageData

            DispatchQueue.main.async(execute: { [self] in
                performSegue(withIdentifier: "pushQuadEditView", sender: nil)
            })
        }
    }
}

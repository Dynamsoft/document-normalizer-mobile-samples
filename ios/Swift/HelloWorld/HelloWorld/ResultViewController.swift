//
//  ResultViewController.swift
//  HelloWorld
//
//  Created by dynamsoft on 2022/5/6.
//

import UIKit

class ResultViewController : UIViewController {
    private var imageView: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        configResultView()
    }

    func configResultView() {
        view.backgroundColor = UIColor.black
        let w = UIScreen.main.bounds.size.width
        let h = UIScreen.main.bounds.size.height

        imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: w, height: h))
        imageView.isUserInteractionEnabled = true
        imageView.contentMode = .scaleAspectFit
        imageView.image = StaticClass.instance.resultImage
        view.addSubview(imageView)
    }
}

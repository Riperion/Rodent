//
//  AppViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/24/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit

class AppViewController: UITabBarController {
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if !API.sharedInstance.isAuthorized {
            self.present(authenticationViewController(), animated: animated, completion: nil)
        }
    }
    
    // MARK: - Authentication
    func authenticationViewController() -> UIViewController {
        return self
            .storyboard!
            .instantiateViewController(withIdentifier: AUTHENTICATION_CONTROLLER)
    }
}

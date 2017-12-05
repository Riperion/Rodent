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
        
        NotificationCenter.default.addObserver(forName: AUTH_CHANGE_NOTIFICATION,
                                               object: nil,
                                               queue: OperationQueue.main) { _ in
            self.presentAuthenticationControllerIfNeeded()
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        presentAuthenticationControllerIfNeeded()
    }
    
    // MARK: - Authentication
    func presentAuthenticationControllerIfNeeded() {
        if !API.sharedInstance.isAuthorized {
            self.present(authenticationViewController(), animated: true, completion: nil)
        }
    }
    
    func authenticationViewController() -> UIViewController {
        return self
            .storyboard!
            .instantiateViewController(withIdentifier: AUTHENTICATION_CONTROLLER)
    }
}

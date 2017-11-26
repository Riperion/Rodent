//
//  AppViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/24/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit

class AppViewController: UITabBarController {
    var loggedIn = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if !loggedIn {
            self.present(authenticationViewController(), animated: animated) {
                self.loggedIn = true
            }
        }
    }
    
    // MARK: - Authentication
    func authenticationViewController() -> UIViewController {
        return self.storyboard!.instantiateViewController(withIdentifier: AUTHENTICATION_CONTROLLER)
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

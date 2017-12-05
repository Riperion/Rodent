//
//  LoginTableViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/24/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit
import CoreLocation

class LoginTableViewController: UITableViewController {
    @IBOutlet weak var emailField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    let LOGIN_BUTTON_INDEX_PATH = IndexPath(item: 0, section: 1)
    let REGISTER_BUTTON_INDEX_PATH = IndexPath(item: 1, section: 1)
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
    
    override func viewDidAppear(_ animated: Bool) {
        let locationManager = CLLocationManager()
        locationManager.requestWhenInUseAuthorization()
    }
    
    // MARK: - Functionality
    func validateFields(email: String?, password: String?) -> Bool {
        return email?.count != 0 && password?.count != 0
    }
    
    func loginWithUser(user: User) {
        API.sharedInstance.authenticate(user: user) { token in
            if let token = token {
                UserDefaults.standard.set(token.token, forKey: AUTH_TOKEN_KEY)
                self.dismiss(animated: true, completion: nil)
            }
        }
    }
    
    override func tableView(_ tableView: UITableView, shouldHighlightRowAt indexPath: IndexPath) -> Bool {
        return indexPath.section != 0
    }
    
    // MARK: - Table View Delegate
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard let email = emailField.text, let password = passwordField.text else { return }
        if !validateFields(email: email, password: password) {
            return
        }
        
        let user = User(username: email, password: password)
        
        switch indexPath {
        case LOGIN_BUTTON_INDEX_PATH:
            loginWithUser(user: user)
            break
        case REGISTER_BUTTON_INDEX_PATH:
            API.sharedInstance.register(user: user) { user in
                if let user = user {
                    self.loginWithUser(user: user)
                }
            }
            break
        default:
            // Do nothing
            break
        }
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

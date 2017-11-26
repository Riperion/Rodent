//
//  LoginTableViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/24/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit

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
    
    // MARK: - Functionality
    func validateFields(email: String?, password: String?) -> Bool {
        return email?.count != 0 && password?.count != 0
    }
    
    // MARK: - Table View Delegate
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let email = emailField.text
        let password = passwordField.text
        
        switch indexPath {
        case LOGIN_BUTTON_INDEX_PATH:
            if !validateFields(email: email, password: password) {
                break
            }
            
            guard let email = email, let password = password else {
                return
            }
            
            let user = User(username: email, password: password)
            
            API().authenticate(user: user) { authToken in
                if let _ = authToken {
                    self.dismiss(animated: true, completion: nil)
                }
            }

            break
        case REGISTER_BUTTON_INDEX_PATH:
            print("Clicked register.")
            break
        default:
            // Do nothing
            break
        }
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

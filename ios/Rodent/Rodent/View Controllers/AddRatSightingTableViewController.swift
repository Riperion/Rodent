//
//  AddRatSightingTableViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 12/4/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit

class AddRatSightingTableViewController: UITableViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    @IBOutlet weak var addressField: UITextView!
    @IBOutlet weak var boroughPicker: UIPickerView!
    @IBOutlet weak var locationTypeField: UITextField!
    @IBOutlet weak var cityField: UITextField!
    @IBOutlet weak var latitudeField: UITextField!
    @IBOutlet weak var longitudeField: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.boroughPicker.dataSource = self
        self.boroughPicker.delegate = self
    }
    
    // MARK: - Borough picker
    private let boroughs = [
        "MANHATTAN",
        "STATEN ISLAND",
        "BROOKLYN",
        "BRONX",
        "QUEENS"
    ]
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return boroughs.count
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return boroughs[row]
    }
}

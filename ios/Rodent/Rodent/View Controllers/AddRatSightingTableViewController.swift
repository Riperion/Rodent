//
//  AddRatSightingTableViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 12/4/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit
import CoreLocation

class AddRatSightingTableViewController: UITableViewController, UIPickerViewDataSource, UIPickerViewDelegate, UITextFieldDelegate, UITextViewDelegate {
    @IBOutlet weak var addressField: UITextView!
    @IBOutlet weak var boroughPicker: UIPickerView!
    @IBOutlet weak var locationTypeField: UITextField!
    @IBOutlet weak var cityField: UITextField!
    @IBOutlet weak var zipCodeField: UITextField!
    @IBOutlet weak var latitudeField: UITextField!
    @IBOutlet weak var longitudeField: UITextField!

    var geocoder: CLGeocoder = CLGeocoder()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.boroughPicker.dataSource = self
        self.boroughPicker.delegate = self
        
        self.addressField.delegate = self
        self.cityField.delegate = self
        self.latitudeField.delegate = self
        self.zipCodeField.delegate = self
        self.longitudeField.delegate = self
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
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        updateCoordinateGuess()
    }
    
    var selectedBorough: String {
        return boroughs[boroughPicker.selectedRow(inComponent: 0)]
    }
    
    // MARK: - Text update delegates
    func textViewDidEndEditing(_ textView: UITextView) {
        updateCoordinateGuess()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        updateCoordinateGuess()
    }
    
    // MARK: - Functionality
    func updateCoordinateGuess() {
        geocodeCoordinates() { coordinates in
            if let coordinates = coordinates {
                self.latitudeField.text = String(coordinates.latitude)
                self.longitudeField.text = String(coordinates.longitude)
            }
        }
    }
    
    func geocodeCoordinates(completion: @escaping (CLLocationCoordinate2D?) -> ()) {
        let address = "\(addressField.text ?? ""), \(selectedBorough), \(zipCodeField.text ?? ""), \(cityField.text ?? "")"
        geocoder.geocodeAddressString(address) { placemarks, error in
            if let placemarks = placemarks, let coordinates = placemarks.first?.location?.coordinate {
                completion(coordinates)
            } else {
                completion(nil)
            }
        }
    }
    
    func makeRatSighting() -> Sighting? {
        let address = addressField.text
        let borough = boroughs[boroughPicker.selectedRow(inComponent: 0)]
        let locationType = locationTypeField.text
        let zipCodeText = zipCodeField.text
        let city = cityField.text
        let latitudeText = latitudeField.text
        let longitudeText = longitudeField.text
        
        if let address = address,
            let locationType = locationType,
            let city = city,
            let zipCodeText = zipCodeText,
            let zipCode = Int(zipCodeText),
            let latitudeText = latitudeText,
            let longitudeText = longitudeText,
            let latitude = Double(latitudeText),
            let longitude = Double(longitudeText) {
            return Sighting(
                identifier: 0,
                dateCreated: Date(),
                owner: "",
                locationType: locationType,
                zipCode: zipCode,
                address: address,
                city: city,
                borough: borough,
                latitudeString: String(format: "%.03f", latitude),
                longitudeString: String(format: "%.03f", longitude)
            )
        }
        
        
        return nil
    }
    
    @IBAction func addSighting(_ sender: UIBarButtonItem) {
        guard let sighting = makeRatSighting() else { return }

        API.sharedInstance.add(sighting: sighting) { sighting in
            if sighting != nil {
                self.navigationController?.popViewController(animated: true)
                NotificationCenter.default.post(name: UPDATE_FEED_NOTIFICATION, object: nil)
            }
        }
    }
}

//
//  RatSightingDetailViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 12/4/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit
import MapKit

class RatSightingDetailViewController: UIViewController {
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var addressLabel: UILabel!
    @IBOutlet weak var boroughLabel: UILabel!
    @IBOutlet weak var cityLabel: UILabel!
    @IBOutlet weak var zipCodeLabel: UILabel!
    @IBOutlet weak var ownerLabel: UILabel!
    @IBOutlet weak var addedInLabel: UILabel!
    
    var sighting: Sighting? = nil
    let dateFormatter = DateFormatter()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .medium
        
        if let sighting = sighting {
            let annotation = SightingPin(sighting: sighting)
            
            mapView.addAnnotation(annotation)
            mapView.showAnnotations([annotation], animated: false)
            
            addressLabel.text = sighting.address
            boroughLabel.text = sighting.borough
            cityLabel.text = sighting.city
            zipCodeLabel.text = String(sighting.zipCode)
            ownerLabel.text = sighting.owner
            addedInLabel.text = dateFormatter.string(from: sighting.dateCreated)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    public func loadSighting(sighting: Sighting) {
        self.sighting = sighting
    }
}

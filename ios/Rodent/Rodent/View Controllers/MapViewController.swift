//
//  MapViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 12/4/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit
import MapKit

class MapViewController: UIViewController {
    @IBOutlet weak var mapView: MKMapView!
    var sightings: [Sighting]?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NotificationCenter.default.addObserver(forName: AUTHENTICATION_CHANGE_NAME,
                                               object: nil,
                                               queue: OperationQueue.main) { notification in
                                                self.updateSightings()
        }
        
        self.updateSightings()
    }

    func updateSightings() {
        API.sharedInstance.getSightings(API.Pagination(offset: 0, limit: 0)) {
            sightings in
            self.sightings = sightings
            
            if let sightings = self.sightings {
                let annotations = sightings.map() {SightingPin(sighting: $0)}
                self.mapView.addAnnotations(annotations)
                self.mapView.showAnnotations(annotations, animated: true)
            }
        }
    }
}

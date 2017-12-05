//
//  MapViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 12/4/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit
import MapKit

class MapViewController: UIViewController, MKMapViewDelegate {
    @IBOutlet weak var mapView: MKMapView!

    var sightings: [Sighting]?
    let NUMBER_PINS = 300
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mapView.delegate = self
        
        NotificationCenter.default.addObserver(forName: UPDATE_FEED_NOTIFICATION,
                                               object: nil,
                                               queue: OperationQueue.main) { notification in
                                                self.updateSightings()
        }
        
        self.updateSightings()
    }
    
    func updateSightings() {
        API.sharedInstance.getSightings(API.Pagination(offset: 0, limit: NUMBER_PINS)) {
            sightings in
            self.sightings = sightings
            
            if let sightings = self.sightings {
                let annotations = sightings.map() {SightingPin(sighting: $0)}
                self.mapView.addAnnotations(annotations)
                self.mapView.showAnnotations(annotations, animated: true)
            }
        }
    }
    
    // MARK: - Map View Delegate
    func mapView(_ mapView: MKMapView, didSelect view: MKAnnotationView) {
        guard let sightingPin = view.annotation as? SightingPin else { return }
        
        let detailViewController = self.storyboard?.instantiateViewController(withIdentifier: SIGHTING_DETAIL_STORYBOARD_ID) as! RatSightingDetailViewController
        detailViewController.loadSighting(sighting: sightingPin.sighting)
        self.navigationController?.pushViewController(detailViewController, animated: true)
        
        mapView.deselectAnnotation(sightingPin, animated: false)
    }
}

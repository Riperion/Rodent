//
//  RatSightingTableViewController.swift
//  Rodent
//
//  Created by Mert Dumenci on 12/4/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import UIKit

class RatSightingTableViewController: UITableViewController {
    let SIGHTING_CELL_REUSE_ID = "sighting_cell"
    var LOAD_SIZE = 100
    
    var sightings: [Sighting]?
    var currentOffset = 0
    var loadingMoreSightings = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        NotificationCenter.default.addObserver(forName: UPDATE_FEED_NOTIFICATION,
                                               object: nil,
                                               queue: OperationQueue.main) { notification in
                                                   self.reloadSightings()
                                               }
        self.reloadSightings()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func reloadSightings() {
        API.sharedInstance.getSightings(API.Pagination(offset: 0, limit: LOAD_SIZE)) {
            sightings in
            self.sightings = sightings
            self.tableView.reloadData()
        }
    }
    
    func loadMoreSightings() {
        // Don't load more sightings if we're currently loading more sightings, or if there are no
        // existing sightings.
        if loadingMoreSightings || self.sightings?.count == 0 {
            return
        }

        self.loadingMoreSightings = true
        
        API.sharedInstance.getSightings(API.Pagination(offset: currentOffset, limit: LOAD_SIZE)) {
            sightings in
            if let existingSightings = self.sightings, let sightings = sightings {
                self.sightings = existingSightings + sightings
                self.tableView.reloadData()
                
                self.loadingMoreSightings = false
            }
        }
    }
    
    @IBAction func logout(_ sender: Any) {
        API.sharedInstance.logout()
        UserDefaults.standard.removeObject(forKey: AUTH_TOKEN_KEY)
        NotificationCenter.default.post(Notification(name: AUTH_CHANGE_NOTIFICATION))
    }
    
    func pushDetailViewController(forSighting sighting: Sighting) {
        let detailController = self.storyboard!.instantiateViewController(withIdentifier: SIGHTING_DETAIL_STORYBOARD_ID) as! RatSightingDetailViewController
        detailController.loadSighting(sighting: sighting)
        self.navigationController?.pushViewController(detailController, animated: true)
    }

    // MARK: - Loading
    override func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollView != self.tableView {
            return
        }
        
        // If we're at the bottom of the scroll view, load more sightings.
        if scrollView.bounds.size.height + scrollView.contentOffset.y >= scrollView.contentSize.height {
            loadMoreSightings()
        }
    }
    
    // MARK: - Table view data source
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.sightings != nil ? self.sightings!.count : 0
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let sighting = self.sightings![indexPath.row]
        let cell = tableView.dequeueReusableCell(withIdentifier: SIGHTING_CELL_REUSE_ID, for: indexPath)
                    as! SightingTableViewCell
        cell.addressLabel.text = sighting.address
        cell.locationLabel.text = "\(sighting.borough), \(sighting.city)"
        cell.userLabel.text = sighting.owner
        
        return cell
    }
    
    // MARK: - Table view delegate
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let sighting = sightings![indexPath.row]
        pushDetailViewController(forSighting: sighting)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

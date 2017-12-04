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

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func updateSightings() {
        API.sharedInstance.getSightings(API.Pagination(offset: 0, limit: 0)) {
            sightings in
            self.sightings = sightings
            self.tableView.reloadData()
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

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

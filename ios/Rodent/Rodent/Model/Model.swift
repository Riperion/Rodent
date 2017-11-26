//
//  Model.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/25/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import Foundation

typealias AuthToken = String

struct User: Codable {
    let identifier: Int?

    let username: String
    let password: String
    
    let admin: Bool?
    
    init(username: String, password: String) {
        self.identifier = nil
        self.username = username
        self.password = password
        self.admin = nil
    }
    
    private enum CodingKeys: String, CodingKey {
        case identifier = "id"
        case username
        case password
        case admin
    }
}

struct Sighting: Codable {
    let identifier: Int
    let dateCreated: Date
    
    // TODO(mert): Make an enum?
    let locationType: String
    let zipCode: Int
    let address: String
    let city: String
    let borough: String
    let latitude: Double
    let longitude: Double
    
    private enum CodingKeys: String, CodingKey {
        case identifier = "id"
        case dateCreated = "date_created"
        case locationType = "location_type"
        case zipCode = "zip_code"
        case address
        case city
        case borough
        case latitude
        case longitude
    }
}

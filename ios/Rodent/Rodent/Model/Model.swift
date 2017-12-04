//
//  Model.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/25/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import Foundation
import MapKit

let dateFormatter = DateFormatter()

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
    
    // MARK: - Codable
    
    private enum CodingKeys: String, CodingKey {
        case identifier = "id"
        case username
        case email
        case password
        case admin
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)

        try container.encode(identifier, forKey: .identifier)
        try container.encode(username, forKey: .username)
        // The API is set up to expect an email, but we don't use this field.
        try container.encode("", forKey: .email)
        try container.encode(password, forKey: .password)
    }
    
    init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        
        identifier = try values.decode(Int.self, forKey: .identifier)
        username = try values.decode(String.self, forKey: .username)
        password = try values.decode(String.self, forKey: .password)
        admin = nil
    }
}

struct Sighting: Codable {
    let identifier: Int
    let dateCreated: Date
    let owner: String
    
    let locationType: String
    let zipCode: Int
    let address: String
    let city: String
    let borough: String
    let latitudeString: String
    let longitudeString: String

    var latitude: Double {
        return Double(latitudeString)!
    }
    var longitude: Double {
        return Double(longitudeString)!
    }
    
    // MARK: - Codable
    
    private enum CodingKeys: String, CodingKey {
        case identifier = "id"
        case dateCreated = "date_created"
        case owner
        case locationType = "location_type"
        case zipCode = "zip_code"
        case address
        case city
        case borough
        case latitudeString = "latitude"
        case longitudeString = "longitude"
    }
}

class SightingPin: NSObject, MKAnnotation {
    let sighting: Sighting
    
    init(sighting: Sighting) {
        self.sighting = sighting
    }
    
    var coordinate: CLLocationCoordinate2D {
        return CLLocationCoordinate2D(latitude: sighting.latitude, longitude: sighting.longitude)
    }
    
    var title: String? {
        return sighting.address
    }
    
    var subtitle: String? {
        return dateFormatter.string(from: sighting.dateCreated)
    }
}

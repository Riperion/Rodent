//
//  API.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/25/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import Foundation
import Alamofire

private struct ListResponse<T: Decodable>: Decodable {
    let count: Int
    let list: [T]
    
    private enum CodingKeys: String, CodingKey {
        case count = "count"
        case list = "results"
    }
}

public struct AuthToken: Decodable {
    let token: String
    var authHeader: String {
        return "Token \(token)"
    }
    
    private enum CodingKeys: String, CodingKey {
        case token = "auth_token"
    }
}

let AUTHENTICATION_CHANGE_NAME = Notification.Name.init(rawValue: "net.riperion.rodent.authChange")

public class API {
    let BASE_URL = "https://rodent.riperion.net/api/"
    let RESULTS_KEY = "results"
    
    typealias Response = Dictionary<String, Any>
    
    struct Pagination {
        let offset: Int
        let limit: Int
    }
    
    fileprivate func url(path: String) -> URLConvertible {
        return BASE_URL + path
    }
    
    static let sharedInstance = API(authToken: nil)
    
    // MARK: - Constructors

    var authToken: AuthToken? = nil
    let encoder: JSONEncoder
    let decoder: JSONDecoder
    
    init(authToken: AuthToken?) {
        self.encoder = JSONEncoder()
        self.encoder.dateEncodingStrategy = .iso8601
        
        self.decoder = JSONDecoder()
        self.decoder.dateDecodingStrategy = .iso8601

        self.authToken = authToken
    }

    // MARK: - Helpers

    var authorizedHeaders: HTTPHeaders {
        guard let authToken = authToken else {
            return [:]
        }

        return ["Authorization": authToken.authHeader]
    }
    
    var isAuthorized: Bool {
        return self.authToken != nil
    }
    
    private func sendAuthChangeNotification() {
        NotificationCenter.default.post(name: AUTHENTICATION_CHANGE_NAME, object: nil)
    }
    
    // MARK: - Data serialization
    
    fileprivate func encode<T : Encodable>(encodable: T) throws -> [String: Any] {
        // This is nasty, I know.
        let encoded = try encoder.encode(encodable)
        let data = try JSONSerialization.jsonObject(with: encoded, options: .init(rawValue: 0))
        return data as! [String: Any]
    }
    
    // MARK: - User facing methods
    
    func authenticate(user: User, completion: @escaping (AuthToken?) -> ()) {
        let parameters: Parameters = [
            "username": user.username,
            "password": user.password
        ]
        
        Alamofire.request(url(path: "auth/token/create/"),
                          method: .post,
                          parameters: parameters,
                          encoding: JSONEncoding.default)
            .response() { response in
                if response.response?.statusCode != 200 {
                    completion(nil)
                    return
                }
            
                let token = try! self.decoder.decode(AuthToken.self, from: response.data!)
                self.authToken = token
                self.sendAuthChangeNotification()

                completion(token)
            }
    }
    
    func logout() {
        self.authToken = nil
        self.sendAuthChangeNotification()
    }
    
    func register(user: User, completion: @escaping (User?) -> ()) {
        let parameters: Parameters = try! encode(encodable: user)
        
        Alamofire.request(url(path: "auth/users/create/"),
                          method: .post,
                          parameters: parameters,
                          encoding: JSONEncoding.default)
            .response() { response in
                if response.response?.statusCode != 200 {
                    completion(nil)
                    return
                }
                
                let user = try! self.decoder.decode(User.self, from: response.data!)
                completion(user)
            }
    }
    
    func add(sighting: Sighting,
             completion: @escaping (Sighting?) -> ()) {
        if !isAuthorized {
            completion(nil)
            return
        }
        
        let parameters: Parameters = try! encode(encodable: sighting)
        
        Alamofire.request(url(path: "ratsightings/"),
                          method: .post,
                          parameters: parameters,
                          encoding: JSONEncoding.default)
            .response() { response in
                let successful = response.response?.statusCode != 200
                completion(successful ? sighting : nil)
            }
    }
    
    func getSightings(_ pagination: Pagination,
                      completion: @escaping ([Sighting]?) -> ()) {
        if !isAuthorized {
            completion(nil)
            return
        }
        
        let parameters: Parameters = [
            "offset": pagination.offset,
            "limit": pagination.limit
        ]
        
        Alamofire.request(url(path: "ratsightings/"),
                          method: .get,
                          parameters: parameters,
                          encoding: URLEncoding.default,
                          headers: self.authorizedHeaders)
            .response() { response in
                if response.response?.statusCode != 200 {
                    completion(nil)
                    return
                }
                
                let listResponse = try! self.decoder.decode(ListResponse<Sighting>.self,
                                                            from: response.data!)
                completion(listResponse.list)
            }
    }
    
    func getSightings(inRange range: Range<Date>,
                      completion: @escaping ([Sighting]?) -> ()) {
        if !isAuthorized {
            completion(nil)
            return
        }
        
        let parameters: Parameters = [
            "date_created_0": range.lowerBound,
            "date_created_1": range.upperBound
        ]
        
        Alamofire.request(url(path: "ratsightings/"),
                          method: .get,
                          parameters: parameters,
                          encoding: URLEncoding.default,
                          headers: self.authorizedHeaders)
            .response() { response in
                if response.response?.statusCode != 200 {
                    completion(nil)
                    return
                }
                
                let listResponse = try! self.decoder.decode(ListResponse<Sighting>.self,
                                                            from: response.data!)
                completion(listResponse.list)
        }
    }
    
    func getSighting(withId identifier: Int,
                     completion: @escaping (Sighting?) -> ()) {
        if !isAuthorized {
            completion(nil)
            return
        }

        Alamofire.request(url(path: "ratsightings/\(identifier)/"),
                          method: .get,
                          encoding: URLEncoding.default,
                          headers: self.authorizedHeaders)
            .response() { response in
                if response.response?.statusCode != 200 {
                    completion(nil)
                    return
                }
                
                let sighting = try! self.decoder.decode(Sighting.self,
                                                        from: response.data!)
                completion(sighting)
        }
    }
}

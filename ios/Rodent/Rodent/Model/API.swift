//
//  API.swift
//  Rodent
//
//  Created by Mert Dumenci on 11/25/17.
//  Copyright © 2017 Mert Dumenci. All rights reserved.
//

import Foundation
import Alamofire

extension Request {
    public func debugLog() -> Self {
        #if DEBUG
            debugPrint(self)
        #endif
        return self
    }
}

struct API {
    let BASE_URL = "https://rodent.riperion.net/api/"
    typealias Response = Dictionary<String, Any>

    fileprivate func url(path: String) -> URLConvertible {
        return BASE_URL + path
    }
    
    fileprivate func encoder() -> JSONEncoder {
        return JSONEncoder()
    }
    
    fileprivate func decoder() -> JSONDecoder {
        return JSONDecoder()
    }
    
    // MARK: - User facing methods
    // TODO(mert): Use `Codable` for encoding data
    
    func authenticate(user: User, completion: @escaping (AuthToken?) -> ()) {
        let parameters: Parameters = [
            "username": user.username,
            "password": user.password
        ]
        
        Alamofire.request(url(path: "auth/token/create/"),
                          method: .post,
                          parameters: parameters,
                          encoding: JSONEncoding.default)
            .validate()
            .responseJSON() { response in
                guard let responseValue = response.result.value else {
                    completion(nil)
                    return
                }
                
                let responseDict = responseValue as! Response
                let authToken = responseDict["auth_token"] as! AuthToken
                
                completion(authToken)
            }
    }
    
    func register(user: User, completion: @escaping (User?) -> ()) {
        let parameters: Parameters = [
            "email": "",
            "username": user.username,
            "password": user.password
        ]
        
        Alamofire.request(url(path: "auth/users/create/"),
                          method: .post,
                          parameters: parameters,
                          encoding: JSONEncoding.default)
            .validate()
            .response() { response in
                let user = try! self.decoder().decode(User.self, from: response.data!)
                completion(user)
        }
        
        completion(nil)
    }
    
    func logout(token: AuthToken) -> Bool {
        return false
    }
    
    func add(sighting: Sighting) -> Bool {
        return false
    }
    
    func getSightings(withOffset offset: Int) -> [Sighting]? {
        return nil
    }
    
    func getSightings(inRange range: Range<Date>) -> [Sighting]? {
        return nil
    }
    
    func getSighting(withId identifier: Int) -> Sighting? {
        return nil
    }
}

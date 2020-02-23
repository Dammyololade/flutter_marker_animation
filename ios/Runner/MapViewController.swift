//
//  MapViewController.swift
//  Runner
//
//  Created by Adedamola Adeyemo on 19/02/2020.
//

import UIKit
import GoogleMaps

class MapViewController: UIViewController, GMSMapViewDelegate {
    
    var mapView: GMSMapView!
    var carIcon: UIImageView?
    var marker: GMSMarker!


    override func viewDidLoad() {
        super.viewDidLoad()
        self.view = mapView
        // Do any additional setup after loading the view.
    }
    
    override func loadView() {
        let initalLoc = CLLocationCoordinate2DMake(6.5212402, 3.3679965)
        let camera = GMSCameraPosition(latitude: 6.5212402, longitude: 3.3679965, zoom: 12)
        mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        mapView.isTrafficEnabled = true
        mapView.isMyLocationEnabled = true
        mapView.settings.compassButton = true
        mapView.settings.myLocationButton = true
        mapView.settings.zoomGestures = true
        mapView.settings.tiltGestures = true
        mapView.settings.rotateGestures = true
        
        mapView.delegate = self
        marker = GMSMarker(position: initalLoc)
        marker.title = "Animatable marker"
        marker.iconView = UIImageView(image: UIImage(named: "ic_car"))
        marker.map = mapView
    
    }
    
    func animateMarker() {
        DispatchQueue.global(qos: .background).async {
            DispatchQueue.main.async {
                
                let toLocation = CLLocationCoordinate2D(latitude: 6.595680, longitude: 3.337030)
                CATransaction.begin()
                CATransaction.setAnimationDuration(200)
                CATransaction.setCompletionBlock({
                    // later implement
                })
                self.marker.rotation = 2
                self.marker.groundAnchor = CGPoint(x: CGFloat(0.5), y: CGFloat(0.5))
                CATransaction.commit()
                
                CATransaction.begin()
                CATransaction.setAnimationDuration(20)
                self.marker.position = toLocation
                
                //let camera = GMSCameraUpdate.setTarget(destination, zoom: 16)
                //mapView.animate(with: camera)
            
                CATransaction.commit()
            }
        }
    }
    
    /// This function decodes a `String` to a `[CLLocationCoordinate2D]?`
    ///
    /// - parameter encodedPolyline: `String` representing the encoded Polyline
    /// - parameter precision: The precision used to decode coordinates (default: `1e5`)
    ///
    /// - returns: A `[CLLocationCoordinate2D]` representing the decoded polyline if valid, `nil` otherwise
    public func decodePolyline(_ encodedPolyline: String, precision: Double = 1e5) -> [CLLocationCoordinate2D]? {
        
        let data = encodedPolyline.data(using: String.Encoding.utf8)!
        
        let byteArray = (data as NSData).bytes.assumingMemoryBound(to: Int8.self)
        let length = Int(data.count)
        var position = Int(0)
        
        var decodedCoordinates = [CLLocationCoordinate2D]()
        
        var lat = 0.0
        var lon = 0.0
        
        while position < length {
          
            do {
                let resultingLat = try decodeSingleCoordinate(byteArray: byteArray, length: length, position: &position, precision: precision)
                lat += resultingLat
                
                let resultingLon = try decodeSingleCoordinate(byteArray: byteArray, length: length, position: &position, precision: precision)
                lon += resultingLon
            } catch {
                return nil
            }

            decodedCoordinates.append(CLLocationCoordinate2D(latitude: lat, longitude: lon))
        }
        
        return decodedCoordinates
    }
    
    func mapViewDidStartTileRendering(_ mapView: GMSMapView) {
        animateMarker()
    }
    
    private func decodeSingleCoordinate(byteArray: UnsafePointer<Int8>, length: Int, position: inout Int, precision: Double = 1e5) throws -> Double {
        
//        guard position < length else {
//            return nil
//        }
        
        let bitMask = Int8(0x1F)
        
        var coordinate: Int32 = 0
        
        var currentChar: Int8
        var componentCounter: Int32 = 0
        var component: Int32 = 0
        
        repeat {
            currentChar = byteArray[position] - 63
            component = Int32(currentChar & bitMask)
            coordinate |= (component << (5*componentCounter))
            position += 1
            componentCounter += 1
        } while ((currentChar & 0x20) == 0x20) && (position < length) && (componentCounter < 6)
        
        if (coordinate & 0x01) == 0x01 {
            coordinate = ~(coordinate >> 1)
        } else {
            coordinate = coordinate >> 1
        }
        
        return Double(coordinate) / precision
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

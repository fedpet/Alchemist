package it.unibo.alchemist.sensory

import it.unibo.alchemist.model.interfaces.Position2D
import it.unibo.alchemist.shapes.GeometricShape2D
import java.awt.geom.Arc2D

// To be better implemented following https://legends2k.github.io/2d-fov/design.html
class FieldOfView2D<P : Position2D<P>>(
    originX: Double,
    originY: Double,
    direction: Double,
    aperture: Double,
    distance: Double
) : InfluenceSphere2D<P>(GeometricShape2D<P>(
    Arc2D.Double(originX - distance, originY - distance, distance * 2, distance * 2, -direction, -aperture, Arc2D.PIE)
))
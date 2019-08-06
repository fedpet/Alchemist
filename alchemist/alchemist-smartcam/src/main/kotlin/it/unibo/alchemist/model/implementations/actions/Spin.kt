package it.unibo.alchemist.model.implementations.actions

import it.unibo.alchemist.model.implementations.geometry.asAngle
import it.unibo.alchemist.model.implementations.positions.Euclidean2DPosition
import it.unibo.alchemist.model.interfaces.Context
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.Reaction
import it.unibo.alchemist.model.interfaces.environments.EuclideanPhysics2DEnvironment
import kotlin.math.cos
import kotlin.math.sin

/**
 * Spins a node around itself.
 * @param <T> concentration type
 * @param node the node
 * @param reaction the reaction hosting this action
 * @param env the environment containing the node
 * @param speed angular speed in radians
 */
class Spin<T>(
    node: Node<T>,
    private val reaction: Reaction<T>,
    private val env: EuclideanPhysics2DEnvironment<T>,
    private val speed: Double
) : AbstractAction<T>(node) {
    override fun cloneAction(n: Node<T>, r: Reaction<T>) =
        Spin(n, r, env, speed)

    /**
     * Spins the node around itself.
     */
    override fun execute() {
        val realSpeed = speed / reaction.rate
        val headingAngle = env.getHeading(node).asAngle() + realSpeed
        env.setHeading(node, Euclidean2DPosition(cos(headingAngle), sin(headingAngle)))
    }

    override fun getContext() = Context.LOCAL
}
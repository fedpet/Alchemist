package it.unibo.alchemist.loader.export

import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Molecule
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.Position
import it.unibo.alchemist.model.interfaces.Reaction
import it.unibo.alchemist.model.interfaces.Time
import it.unibo.alchemist.model.interfaces.VisibleNode

/**
 * Exports the percentage of targets covered by at least 1 camera, up to [maxCamerasPerTarget].
 * For instance if [maxCamerasPerTarget] is 2 then it exports 1-coverage and 2-coverage percentages.
 * It exports NaN if there are no targets
 * A target is any [Node] containing [targetMolecule].
 * A camera is any [Node] containing [visionMolecule].
 * [visionMolecule] is expected to contain a collection of [VisibleNode].
 */
class CamerasKCoverage<P : Position<P>>(
    private val visionMolecule: Molecule,
    private val targetMolecule: Molecule,
    private val maxCamerasPerTarget: Int
) : Extractor {
    private val names = maxCamerasPerTarget.downTo(1).map { "$it-coverage" }

    override fun extractData(environment: Environment<*, *>, r: Reaction<*>?, time: Time?, step: Long): DoubleArray {
        @Suppress("UNCHECKED_CAST") val env = environment as Environment<*, P>
        val nodes: List<Node<*>> = env.nodes
        val numTargets = nodes.filter { it.isTarget() }.count()
        val kCoverageToNumberOfNodesMap = nodes
            .filter { it.isCamera() }
            .flatMap { it.getVisibleTargets() }
            .groupingBy { it.node.id }
            .eachCount()
            .values
            .groupingBy { it }
            .eachCount()
        val kCoverages = maxCamerasPerTarget.downTo(1)
            .map { kCoverageToNumberOfNodesMap.getOrDefault(it, 0) }
        return kCoverages
            .mapIndexed { idx, value -> value + kCoverages.getOrElse(idx - 1) { 0 } }
            .map { if (numTargets == 0) Double.NaN else it.toDouble() / numTargets }
            .toDoubleArray()
    }

    override fun getNames() = names

    private fun Node<*>.isTarget() = contains(targetMolecule)

    private fun Node<*>.isCamera() = contains(visionMolecule)

    private fun Node<*>.getVisibleTargets() =
        with(getConcentration(visionMolecule)) {
            require(this is List<*>) { "Expected a List but got $this of type ${this::class}" }
            if (!isEmpty()) {
                get(0)?.also {
                    require(it is VisibleNode<*, *>) {
                        "Expected a List<VisibleNode> but got List<${it::class}> = $this"
                    }
                }
            }
            @Suppress("UNCHECKED_CAST")
            (this as Iterable<VisibleNode<*, *>>).filter { it.node.isTarget() }
        }
}
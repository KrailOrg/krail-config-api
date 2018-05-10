package uk.q3c.krail.config

import java.io.File
import java.io.Serializable

/**
 * Identifies path to a particular type of directory - usually implementation is dependent on application environment
 *
 *
 * Created by David Sowerby on 22 Aug 2017
 */
interface PathLocator : Serializable {

    fun configurationDirectory(): File

    fun applicationDirectory(): File
}

package uk.q3c.krail.config;

import java.io.File;
import java.io.Serializable;

/**
 * Identifies path to a particular type of directory - usually implementation is dependent on application environment
 * <p>
 * Created by David Sowerby on 22 Aug 2017
 */
public interface PathLocator extends Serializable {

    File configurationDirectory();

    File applicationDirectory();
}

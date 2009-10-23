/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-12-01
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.reproduction.ICrossoverPipe.java
 * Last modification: 2006-12-01
 *                by: Thomas Weise
 *
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to theFree Software
 *                    Foundation, Inc. 51 Franklin Street, Fifth Floor,
 *                    Boston, MA 02110-1301, USA or download the license
 *                    under http://www.gnu.org/licenses/lgpl.html or
 *                    http://www.gnu.org/copyleft/lesser.html.
 *
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package org.sigoa.spec.go.reproduction;

import java.io.Serializable;

/**
 * The parameters common to things that are able to perform crossover to
 * streams of individuals.
 *
 * @author Thomas Weise
 */
public interface ICrossoverParameters extends Serializable {
  /**
   * Set the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover.
   *
   * @param rate
   *          the crossover rate which must be in <code>[0,1]</code>
   * @throws IllegalArgumentException
   *           if <code>rate</code> not in <code>[0,1]</code>
   */
  public abstract void setCrossoverRate(final double rate);

  /**
   * Get the crossover rate. The crossover rate denotes the fraction of
   * individuals that will undergo a crossover.
   *
   * @return the mutation rate
   */
  public abstract double getCrossoverRate();
}

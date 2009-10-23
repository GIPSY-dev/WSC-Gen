/*
 * Copyright (c) 2006 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : 2006-11-26
 * Creator          : Thomas Weise
 * Original Filename: org.sigoa.spec.go.embryogeny.IEmbryogeny.java
 * Version          : 1.0.0
 * Last modification: 2006-11-26
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

package org.sigoa.spec.go.embryogeny;

import java.io.Serializable;

/**
 * The embryogeny interface is used in order to translate genotypes into
 * phenotypes.
 *
 * @param <G>
 *          The genotype.
 * @param <PP>
 *          The phenotype.
 * @author Thomas Weise
 * @version 1.0.0
 */
public interface IEmbryogeny<G extends Serializable, PP extends Serializable>
    extends Serializable {
  /**
   * Compute an instance of the phenotype from an instance of the genotype.
   *
   * @param genotype
   *          The genotype instance to breed a phenotype from.
   * @return The phenotype hatched from the genotype instance.
   * @throws NullPointerException
   *           if <code>genotype==null</code>.
   */
  public abstract PP hatch(final G genotype);

  /**
   * Compute a genotype from a phenotype. This method can be used if
   * different optimizers optimize a phenotype using different genotypes
   * and want to exchange individuals.
   * Here it is possible to return <code>null</code> if regression cannot
   * be performed.
   *
   * @param phenotype
   *          the phenotype
   * @return the genotype regressed from the phenotype or <code>null</code>
   *         if regression was not possible
   * @throws NullPointerException
   *           if <code>phenotype==null</code>.
   */
  public abstract G regress(final PP phenotype);

}
